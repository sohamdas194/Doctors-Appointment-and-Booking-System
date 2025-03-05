import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { MatTableModule } from '@angular/material/table';  
import { MatSortModule } from '@angular/material/sort';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatChipsModule } from '@angular/material/chips';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatTabsModule } from '@angular/material/tabs';
import { MatIconModule } from '@angular/material/icon';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { MenuComponent } from './menu/menu.component';
import { ErrorComponent } from './error/error.component';
import { HomeComponent } from './home/home.component';
import { PatientDashboardComponent } from './patient/patient-dashboard/patient-dashboard.component';
import { DoctorDashboardComponent } from './doctor/doctor-dashboard/doctor-dashboard.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { HttpInterceptorAuthService } from './service/http-interceptor-auth.service';
import { PatientSignupComponent } from './patient/patient-signup/patient-signup.component';
import { DoctorSignupComponent } from './doctor/doctor-signup/doctor-signup.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DoctorProfileUpdateComponent } from './doctor/doctor-profile-update/doctor-profile-update.component';

import { DoctorAvailabilityStatusComponent } from './doctor/doctor-availability-status/doctor-availability-status.component';
import { AppointmentComponent } from './patient/appointment/appointment.component';
import { BookAppointmentPopupComponent } from './patient/appointment/book-appointment-popup/book-appointment-popup.component';
import { UpdatePatientComponent } from './patient/update-patient/update-patient.component';
import { FamilyMemberComponent } from './patient/family-member/family-member.component';
import { AppointmentHistoryComponent } from './patient/appointment-history/appointment-history.component';
import { CommonModule, DatePipe } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatFormFieldModule } from '@angular/material/form-field';
import { UpcommingAppointmentsComponent } from './admin/admin-dashboard/upcomming-appointments/upcomming-appointments.component';
import { PastAppointmentsComponent } from './admin/admin-dashboard/past-appointments/past-appointments.component';
import { UpdateDoctorConsultationFeeComponent } from './admin/admin-dashboard/update-doctor-consultation-fee/update-doctor-consultation-fee.component';
import { DoctorConsultationHistoryComponent } from './doctor/doctor-consultation-history/doctor-consultation-history.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { StatusComponent } from './doctor/doctor-dashboard/status/status.component';
import { QRCodeModule } from 'angularx-qrcode';
import { PaymentPopupComponent } from './admin/admin-dashboard/upcomming-appointments/payment-popup/payment-popup.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ForgotPasswordComponent,
    ResetPasswordComponent,
    MenuComponent,
    ErrorComponent,
    HomeComponent,
    PatientDashboardComponent,
    DoctorDashboardComponent,
    AdminDashboardComponent,
    PatientSignupComponent,
    DoctorSignupComponent,
    DoctorProfileUpdateComponent,
    DoctorAvailabilityStatusComponent,
    AppointmentComponent,
    BookAppointmentPopupComponent,
    UpdatePatientComponent,
    FamilyMemberComponent,
    AppointmentHistoryComponent,
    UpcommingAppointmentsComponent,
    PastAppointmentsComponent,
    UpdateDoctorConsultationFeeComponent,
    DoctorConsultationHistoryComponent,
    StatusComponent,
    PaymentPopupComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatCardModule,
    MatTableModule,
    MatSortModule,
    MatPaginatorModule,
    MatDialogModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatChipsModule,
    MatSnackBarModule,
    CommonModule,
    FormsModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    CommonModule,
    MatExpansionModule,
    MatTabsModule,
    MatIconModule,
    ScrollingModule,
    QRCodeModule
  ],
  exports: [
    AppointmentHistoryComponent
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: HttpInterceptorAuthService, multi: true},DatePipe
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule { }
