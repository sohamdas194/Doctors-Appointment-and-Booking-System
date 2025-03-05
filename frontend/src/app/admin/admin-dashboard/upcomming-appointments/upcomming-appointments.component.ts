import { AfterViewInit, Component, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { AdminService } from 'src/app/service/admin.service';
import { CONFIG_SUCCESS, CONFIG_ERROR } from 'src/app/util/snack-bar-configs';
import { PaymentPopupComponent } from './payment-popup/payment-popup.component';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-upcomming-appointments',
  templateUrl: './upcomming-appointments.component.html',
  styleUrls: ['./upcomming-appointments.component.css']
})
export class UpcommingAppointmentsComponent implements OnInit, AfterViewInit ,OnDestroy{

  newAppointmentStatuses: {[key: number]: string} = {};

  minDate!: string;

  appointmentStatuses: string[] = ['COMPLETED', 'NO_SHOW']

  displayedColumns: string[] = ['doctorName', 'doctorSpecialization', 'patientName', 'patientGender', 'patientAge', 'appointmentDate', 'appointmentTimeSlot', 'appointmentStatus','action'];
  appointmentDataSource: any = new MatTableDataSource();

  searchDataForm!: FormGroup;

  @Input() resetToDefaultValues$!: Observable<any>;

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private adminService: AdminService,
    private formBuilder: FormBuilder,
    private snackBar: MatSnackBar,
    private dialogRef: MatDialog,
  ) {
    this.searchDataForm = this.formBuilder.group({
      name: [''],
      fromDate: [this.minDate],
      toDate: ['']
    });

   }
   ngOnDestroy(): void {
    this.dialogRef.closeAll();
  }

  ngOnInit(): void {
    let today = new Date();
    this.minDate = today.getFullYear() + '-' + (today.getMonth()+1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');

    this.onTodayClick();

    this.resetToDefaultValues$.subscribe(value => {
      if(value === true) this.onResetClick(false);
    });
  }

  ngAfterViewInit(): void {
    this.appointmentDataSource.sort = this.sort;
    this.appointmentDataSource.paginator = this.paginator;
  }

  onAllClick(): void {
    this.onResetClick(true);
    let searchDataValues = this.searchDataForm.value;

    this.adminService.searchAppointments(searchDataValues, true).subscribe(
      response => {
        this.appointmentDataSource.data = response;
        console.log(response);
      }
    );
  }

  onTodayClick(): void {
    let searchDataValues = this.searchDataForm.value;
    searchDataValues.fromDate = this.minDate;
    searchDataValues.toDate = this.minDate;

    this.searchDataForm.reset(searchDataValues);

    this.adminService.searchAppointments(searchDataValues, true).subscribe(
      response => {
        this.appointmentDataSource.data = response;
      }
    );
  }

  onTomorrowClick(): void {
    let tomorrowDate = new Date();
    tomorrowDate.setDate(tomorrowDate.getDate() + 1);
    let tomorrowString = tomorrowDate.getFullYear() + '-' + (tomorrowDate.getMonth()+1).toString().padStart(2, '0') + '-' + tomorrowDate.getDate().toString().padStart(2, '0');

    let searchDataValues = this.searchDataForm.value;
    searchDataValues.fromDate = tomorrowString;
    searchDataValues.toDate = tomorrowString;

    this.searchDataForm.reset(searchDataValues);

    this.adminService.searchAppointments(searchDataValues, true).subscribe(
      response => {
        this.appointmentDataSource.data = response;
      }
    );
  }

  onResetClick(clearTableFlag: boolean): void {
    let searchDataValues = this.searchDataForm.value;
    searchDataValues.name='';
    searchDataValues.fromDate=this.minDate;
    searchDataValues.toDate='';
    this.searchDataForm.reset(searchDataValues);

    if(clearTableFlag) this.appointmentDataSource.data=[];
    else this.onTodayClick();
  }

  onSearchClick(): void {
    this.adminService.searchAppointments(this.searchDataForm.value, true).subscribe(
      response => {
        this.appointmentDataSource.data = response;
      }
    );
  }

  addAppointmentStatus(event: any, appointmentId: any) {
    this.newAppointmentStatuses[appointmentId] = event.target.value;
  }

  onUpdateClick(element: any): void {
    // console.log(element.appointmentId, this.newAppointmentStatuses[element.appointmentId]);

    if(!this.newAppointmentStatuses[element.appointmentId]) {
      this.openSnackBar("Please change the value to update!", CONFIG_ERROR);
      return;
    }

    if(this.newAppointmentStatuses[element.appointmentId] === 'COMPLETED') {
      this.openPaymentDialog(element);
    } else {
      this.adminService.updateAppointmentStatus(element.appointmentId, this.newAppointmentStatuses[element.appointmentId]).subscribe(
        response => {
          this.openSnackBar("Updated", CONFIG_SUCCESS);
          this.remove(element);
        }, error => {
          this.openSnackBar("Update Failed!!", CONFIG_ERROR);
        }
      );
    }
  }

  onNotifyClick(appointmentId: number): void {
    this.adminService.notify(appointmentId).subscribe(
      response => {
        this.openSnackBar("Notified!", CONFIG_SUCCESS);
      }, error => {
        this.openSnackBar("Failed!!", CONFIG_ERROR);
      }
    )
  }

  openSnackBar(message: string, config: MatSnackBarConfig) {
    this.snackBar.open(message, "close", config);
  }

  openPaymentDialog(element: any): void {
    this.dialogRef.open(PaymentPopupComponent, {
      maxWidth: '50%', maxHeight: '75%',
      data: {
        appointmentId: element.appointmentId
      }
    }).afterClosed().subscribe(
      result => {
        console.log(result)
        if(result.paymentCompleted === true) {
          this.remove(element);
        } else {
          this.newAppointmentStatuses[element.appointmentId] = '';
        }
      }
    );
  }

  remove(element: any) {
    this.appointmentDataSource.data = this.appointmentDataSource.data.filter((item: any) => item !== element)
  }

  resetStatusSelect(event: any, appointmentStatus: string) {
    event.value = appointmentStatus;
  }
}
