import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { AppointmentHistoryService } from 'src/app/service/appointment-history.service';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { HttpErrorResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-appointment-history',
  templateUrl: './appointment-history.component.html',
  styleUrls: ['./appointment-history.component.css']
})
export class AppointmentHistoryComponent implements OnInit, AfterViewInit {
  isLoading: boolean = false;
  errorMessage: string = '';
  historyData: any[] = [];
  filteredData: any[] = [];
  dummyarray: any[] = [];
  familyname: any[] = [];
  selectedfamilyname: any[] = [];
  userId!: number;
  historyForm!: FormGroup;
  dataSource: MatTableDataSource<any>;
  displayedColumns: string[] = ['appointmentId', 'doctorFirstName', 'doctorLastName', 'patientFirstName', 'patientLastName', 'memberFirstName', 'memberLastName', 'appointmentSlot', 'appointmentDate', 'appointmentStatus', 'doctorComment', 'patientComment'];

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort!: MatSort;
  @ViewChild('familyNameSelect') familyNameSelect!: ElementRef;

  pname: string = '';
  status: boolean = false;

  constructor(
    private historyService: AppointmentHistoryService,
    private authService: AuthenticationService,
    private datePipe: DatePipe,
    private formBuilder: FormBuilder
  ) {
    this.dataSource = new MatTableDataSource<any>(this.historyData);
    this.dataSource = new MatTableDataSource<any>(this.filteredData);
    this.userId = parseInt(localStorage.getItem('authenticatedUserId') ?? '');
    this.historyForm = this.formBuilder.group({
      fromDate: ['', [Validators.required]],
      toDate: ['', [Validators.required]]

    });
    validators: this.validateDateRange
  }

  ngAfterViewInit(): void {
    this.dataSource.sortingDataAccessor = this.sortingDataAccessor;
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }
  validateDateRange(group: FormGroup): { [key: string]: boolean } {
    const { fromDate, toDate } = group.value;
    if (new Date(fromDate) > new Date(toDate)) {
      return { 'invalidDateRange': true };
    }
    return {};
  }

  ngOnInit(): void {
    console.log(this.dataSource)
    this.dataSource = new MatTableDataSource<any>([]);
    this.getPatientAppointmentHistory();
  }

  getPatientAppointmentHistory(): void {
    const patientId = this.authService.getAuthenticatedUserId();
    if (patientId !== null && patientId !== undefined) {
      this.isLoading = true;
      this.historyService.getPatientAppointmentHistory(+patientId).subscribe(
        (response: any) => {
          this.historyData = response;
          this.historyData.sort((a, b) => {
            const dateA = this.sortingDataAccessor(a, 'appointmentDate');
            const dateB = this.sortingDataAccessor(b, 'appointmentDate');
            if (dateA < dateB) return -1;
            if (dateA > dateB) return 1;
            return 0;
          });
          this.pname = this.historyData[0].patientFirstName;
          this.filteredData = this.historyData;
          this.dataSource.data = this.filteredData;

          const familyNames = new Set<string>();
          familyNames.add('ALL');
          familyNames.add(this.pname);
          this.historyData.forEach((e: any) => {
            if (e.memberFirstName !== null) {
              familyNames.add(e.memberFirstName);
            }
          });
          this.familyname = Array.from(familyNames);

          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
          this.isLoading = false;
        },
        (error: HttpErrorResponse) => {
          this.errorMessage = 'Error fetching appointment history.';
          this.isLoading = false;
          console.error('Error fetching appointment history:', error);
        }
      );
    } else {
      this.errorMessage = 'User is not logged in.';
    }
  }

  onSubmit(): void {
    const selectedFamilyName = this.familyNameSelect.nativeElement.value;
    this.filteredData = this.historyData.filter((appointment) => {
      if (selectedFamilyName === 'ALL') {
        return true;
      } else if (selectedFamilyName === this.pname) {
        return appointment.patientFirstName === this.pname && appointment.memberFirstName == null;
      } else {
        return appointment.memberFirstName === selectedFamilyName;
      }
    });
    this.dataSource.data = this.filteredData;
  }


  sortingDataAccessor = (data: any, property: string): string | number => {
    switch (property) {
      case 'appointmentDate':
        return new Date(data.appointmentDate).getTime();
      default:
        return data[property];
    }
  };


  applyFilter(event: Event) {
    let filterValue = (event.target as HTMLInputElement).value;
    filterValue = filterValue.trim().toLowerCase();
    this.dataSource.filter = filterValue;
  }
  onResetDateSearch(): void {
    this.historyForm.reset();
    this.getPatientAppointmentHistory();
  }
  

  onDateSearch(): void {
    if (this.historyForm.valid) {
      const { fromDate, toDate } = this.historyForm.value;
      this.historyService.getAppointmentsByPatientIdInDateRange(this.userId, fromDate, toDate).subscribe(
        (response: any) => {
          this.historyData = response;
          this.onSubmit();
        }
      );
    } else {
      this.historyForm.markAllAsTouched();
    }
  }


}
