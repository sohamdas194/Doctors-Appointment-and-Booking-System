import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { ViewAppointmentsDto } from 'src/app/model/view-appointments-dto';
import { StatusComponent } from './status/status.component';
import { DoctorDashboardService } from 'src/app/service/doctor-dashboard.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Router } from '@angular/router';
// import { ViewAppointmentsService } from 'src/app/service/view-appointments.service';


@Component({
  selector: 'app-doctor-dashboard',
  templateUrl: './doctor-dashboard.component.html',
  styleUrls: ['./doctor-dashboard.component.css']
})


export class DoctorDashboardComponent implements OnInit {

  fromDate:string='';
  toDate:string='';
  currentDate:string='';
  viewAppointmentsDto: ViewAppointmentsDto[]=[];
  userId = parseInt(localStorage.getItem('authenticatedUserId')??'');
  displayedColumns = ['Appointment Id','Patient Name','Appointment Date','Slot Time','Patient Comments','Update Comments'];
  dataSource = new MatTableDataSource<any>(this.viewAppointmentsDto);
 isButtonDisabled: boolean=false;
  isButton1: boolean=false;
   isButton2: boolean=false;
   isButton3: boolean=false;
   isButton4: boolean=false;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  userEmail!: string;

  constructor(public authService: AuthenticationService, public dialog: MatDialog, private doctorDashboardService: DoctorDashboardService) {}

  openStatusDialog(appointment:ViewAppointmentsDto): void{
  const dialogRef = this.dialog.open(StatusComponent, { width: '450px', maxHeight: '500px', data:appointment });

  dialogRef.afterClosed().subscribe(result=> {
  if(result){
    console.log('Dialog result:', result);
    }
  });
  }

    ngOnInit(): void {
    this.userEmail = this.authService.getAuthenticateduserEmail() || '';
    this.getAppointmentsByCurrentDate();
    let today = new Date();
    this.fromDate = today.getFullYear() + '-' + (today.getMonth()+1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');

    }

    ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
      }


  getAppointmentsByCurrentDate(): void {
         this.toDate='';
         this.fromDate='';
      this.isButtonDisabled=false;
      this.isButton3=true;
        this.isButton1=false;
          this.isButton2=false;
           this.isButton4=false;
  this.doctorDashboardService.getAppointmentsByCurrentDate(this.userId).subscribe((data:any)=>{
    this.viewAppointmentsDto = data;
    this.dataSource.data = this.viewAppointmentsDto;
    console.log(this.viewAppointmentsDto);
   },
   (error: any) => {
    console.error('Error fetching today\'s appointments', error);
   });
  }


  getAppointmentsByNextDate(): void {
        this.toDate='';
         this.fromDate='';
    this.isButton3=false;
     this.isButton1=false;
      this.isButton2=true;
       this.isButton4=false;
     this.isButtonDisabled=true;
  this.doctorDashboardService.getAppointmentsByNextDate(this.userId).subscribe((data:any)=>{
    this.viewAppointmentsDto = data;
    this.dataSource.data = this.viewAppointmentsDto;
   },
   (error: any) => {
    console.error('Error fetching today\'s appointments', error);
   });
  }


   getAllAppointments(): void {
         this.toDate='';
          this.fromDate='';
     this.isButton3=false;
      this.isButton1=true;
      this.isButton2=false;
       this.isButton4=false;
      this.isButtonDisabled=true;
   this.doctorDashboardService.getAllAppointments(this.userId).subscribe((data:any)=>{
     this.viewAppointmentsDto = data;
     this.dataSource.data = this.viewAppointmentsDto;
    },
    (error: any) => {
     console.error('Error fetching today\'s appointments', error);
    });
  }

  searchDate(fromDate:any, toDate:any){
       this.isButton1=false;
        this.isButton2=false;
         this.isButton3=false;
     this.isButtonDisabled=true;
    this.doctorDashboardService.getAppointmentBetween(this.userId,fromDate,toDate).subscribe((data:any)=>{
      this.viewAppointmentsDto = data;
      this.dataSource.data = data;
    },
    (error: any) => {
      console.error('Error fetching today\'s appointments', error);
    });
  }

 }
 