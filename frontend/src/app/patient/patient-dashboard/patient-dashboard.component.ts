import { Component, OnInit, ViewChild } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { ViewAppointmentsDto } from 'src/app/model/view-appointments-dto';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { ViewAppointmentsService } from 'src/app/service/view-appointments.service';
 
@Component({
  selector: 'app-patient-dashboard',
  templateUrl: './patient-dashboard.component.html',
  styleUrls: ['./patient-dashboard.component.css']
})
export class PatientDashboardComponent implements OnInit {
  fromDate:any="";
  toDate:any="";
  date:any[]=[];
  appointment:ViewAppointmentsDto[]=[];
  displayedColumns = ['Appointment Id','Doctor Details','Date Of Appointment','Appointment Slot','Patient Details','Patient Comments','Current Status','Options'];
  dataSource = new MatTableDataSource<any>(this.appointment);
  @ViewChild(MatPaginator) paginator!: MatPaginator;
 
  userId = parseInt(localStorage.getItem('authenticatedUserId')??'');
  userEmail!: string;
  formBuilder: any;
  appointmentForm: any;
  viewAppointments:any;
  appointmentId: any;
 
 
 
  constructor(private viewAppointmentsService:ViewAppointmentsService,
    public authService: AuthenticationService) { }
 
  ngOnInit(): void {
    this.userEmail = this.authService.getAuthenticateduserEmail() || '';
    this.getAppointmentsList(this.userId);
   
  }
  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
}
 
  getAppointmentsList(userId:number){
    this.viewAppointmentsService.getAppointmentsList(userId).subscribe((response:any) => {
      this.appointment=response;
      this.dataSource.data = this.appointment;
    },
 );
  }
  resetRange(): void {
    this.fromDate = "";
    this.toDate = "";
    this.getAppointmentsList(this.userId);
  }
 
  ngAfterViewInit()
    {
      this.dataSource.paginator = this.paginator;
    }
 
    cancelAppointment(appointmentId: number): void{
      this.viewAppointmentsService.cancelAppointment(appointmentId).subscribe((response:any) => {
        if (response.success) {
          console.log(response);
          this.getAppointmentsList(this.userId);
        }
      },
    );
  }
 
  getAppointmentBetween(): void {
    console.log(this.fromDate+this.toDate);
      this.viewAppointmentsService.getDateFilter(this.userId, this.fromDate, this.toDate).subscribe(
        (response: any) => {
          this.appointment=response;
          this.dataSource.data = this.appointment;
        }
      );
  }
 
  
}  
 
 
 