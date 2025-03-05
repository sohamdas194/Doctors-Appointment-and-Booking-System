import { AfterViewInit, Component, Input, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Observable, Subject } from 'rxjs';
import { AdminService } from 'src/app/service/admin.service';

@Component({
  selector: 'app-past-appointments',
  templateUrl: './past-appointments.component.html',
  styleUrls: ['./past-appointments.component.css']
})
export class PastAppointmentsComponent implements OnInit, AfterViewInit {

  maxDate!: string;

  displayedColumns: string[] = ['doctorName', 'doctorSpecialization', 'patientName', 'patientGender', 'patientAge', 'appointmentDate', 'appointmentTimeSlot', 'appointmentStatus'];
  appointmentDataSource: any = new MatTableDataSource();

  searchDataForm!: FormGroup;

  @Input() resetToDefaultValues$!: Observable<any>;

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private adminService: AdminService,
    private formBuilder: FormBuilder,
  ) { 
    this.searchDataForm = this.formBuilder.group({
      name: [''],
      fromDate: [''],
      toDate: [this.maxDate]
    });
  }

  ngOnInit(): void {
    let today = new Date();
    this.maxDate = today.getFullYear() + '-' + (today.getMonth()+1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');

    this.onShowAllPastAppointmentsClick();

    this.resetToDefaultValues$.subscribe(value => {
      if(value === true) this.onShowAllPastAppointmentsClick();
    });
  }

  ngAfterViewInit(): void {
    this.appointmentDataSource.sort = this.sort;
    this.appointmentDataSource.paginator = this.paginator;
  }

  onShowAllPastAppointmentsClick():void {
    this.onResetClick(true);
    let searchDataValues = this.searchDataForm.value;

    this.adminService.searchAppointments(searchDataValues, false).subscribe(
      response => {
        this.appointmentDataSource.data = response;
      }
    );
  }

  onResetClick(clearTableFlag: boolean): void {
    let searchDataValues = this.searchDataForm.value;
    searchDataValues.name='';
    searchDataValues.fromDate='';
    searchDataValues.toDate=this.maxDate;
    this.searchDataForm.reset(searchDataValues);

    if(clearTableFlag) this.appointmentDataSource.data=[];
    else this.onShowAllPastAppointmentsClick();
  }

  onSearchClick(): void {
    this.adminService.searchAppointments(this.searchDataForm.value, false).subscribe(
      response => {
        this.appointmentDataSource.data = response;
      }
    );
  }
  

}
