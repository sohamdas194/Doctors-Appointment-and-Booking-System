import { Component, OnInit } from '@angular/core';
import { Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ViewAppointmentsDto } from 'src/app/model/view-appointments-dto';
import { DoctorDashboardService } from 'src/app/service/doctor-dashboard.service';
import { DoctorDashboardComponent } from 'src/app/doctor/doctor-dashboard/doctor-dashboard.component';
import { Router } from '@angular/router';



@Component({
  selector: 'app-status',
  templateUrl: './status.component.html',
  styleUrls: ['./status.component.css']
})
export class StatusComponent implements OnInit {

  appointmentId!: number;
  Status: string[] = ['CONSULTED', 'NO_SHOW'];
  viewAppointmentsDto: ViewAppointmentsDto[] = [];
  userId = parseInt(localStorage.getItem('authenticatedUserId') ?? '');
  constructor(
    public dialogRef: MatDialogRef<StatusComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: ViewAppointmentsDto, private doctorDashboardService: DoctorDashboardService, private router: Router) { }


  onCancel(): void {
    this.dialogRef.close();
  }


  ngOnInit(): void {
  }

  updateDoctorComment() {
    this.doctorDashboardService.updateDoctorComment(this.data.appointmentId, {
      "doctorComment": this.data.doctorComment,
      "appointmentStatus": this.data.currentStatus
    }).subscribe((data: any) => {
      this.viewAppointmentsDto = data;
      console.log("worked!!");
      this.dialogRef.close();
      this.router.navigate(['doctor/dashboard']);
      location.reload();

    },
      (error: any) => {
        console.error('Error fetching today\'s appointments', error);
      });

  }
}

