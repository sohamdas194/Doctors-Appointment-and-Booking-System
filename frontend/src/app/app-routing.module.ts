import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { ForgotPasswordComponent } from './forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './reset-password/reset-password.component';
import { ErrorComponent } from './error/error.component';
import { HomeComponent } from './home/home.component';
import { PatientDashboardComponent } from './patient/patient-dashboard/patient-dashboard.component';
import { DoctorDashboardComponent } from './doctor/doctor-dashboard/doctor-dashboard.component';
import { AdminDashboardComponent } from './admin/admin-dashboard/admin-dashboard.component';
import { PatientSignupComponent } from './patient/patient-signup/patient-signup.component';
import { DoctorSignupComponent } from './doctor/doctor-signup/doctor-signup.component';
import { InverseAuthGuard } from './route-guard/inverse-auth.guard';
import { AuthGuard } from './route-guard/auth.guard';
import { RoleGuard } from './route-guard/role.guard';
import { ROLE_ADMIN, ROLE_DOCTOR, ROLE_PATIENT } from './app.constants';
import { DoctorProfileUpdateComponent } from './doctor/doctor-profile-update/doctor-profile-update.component';
import { DoctorAvailabilityStatusComponent } from './doctor/doctor-availability-status/doctor-availability-status.component';
import { AppointmentComponent } from './patient/appointment/appointment.component';
import { UpdatePatientComponent } from './patient/update-patient/update-patient.component';
import { FamilyMemberComponent } from './patient/family-member/family-member.component';
import { AppointmentHistoryComponent } from './patient/appointment-history/appointment-history.component';
import { DoctorConsultationHistoryComponent } from './doctor/doctor-consultation-history/doctor-consultation-history.component';


const routes: Routes = [
  { path: '' , component: HomeComponent, canActivate: [InverseAuthGuard] },
  { path: 'home' , component: HomeComponent, canActivate: [InverseAuthGuard] },
  { path: 'login' , component: LoginComponent, canActivate: [InverseAuthGuard] },
  { path: 'forgotPassword' , component: ForgotPasswordComponent, canActivate: [InverseAuthGuard] },
  { path: 'resetPassword/:token' , component: ResetPasswordComponent, canActivate: [InverseAuthGuard] },
  { path: 'patientSignup' , component: PatientSignupComponent, canActivate: [InverseAuthGuard] },
  { path: 'doctorSignup' , component: DoctorSignupComponent, canActivate: [InverseAuthGuard] },
  { path: 'patient/dashboard' , component: PatientDashboardComponent, canActivate: [AuthGuard, RoleGuard], data: {role: `${ROLE_PATIENT}`} } ,
  { path: 'doctor/dashboard' , component: DoctorDashboardComponent, canActivate: [AuthGuard, RoleGuard], data: {role: `${ROLE_DOCTOR}`} },
  { path: 'admin/dashboard' , component: AdminDashboardComponent, canActivate: [AuthGuard, RoleGuard], data: {role: `${ROLE_ADMIN}`} },
  {path:'doctor/dashboard/update',component:DoctorProfileUpdateComponent, canActivate: [AuthGuard, RoleGuard],data: {role: `${ROLE_DOCTOR}`}},
  {path:'doctor/dashboard/availability',component:DoctorAvailabilityStatusComponent,canActivate: [AuthGuard, RoleGuard],data: {role: `${ROLE_DOCTOR}`}},
  { path: 'patient/appointment' , component: AppointmentComponent, canActivate: [AuthGuard, RoleGuard], data: {role: `${ROLE_PATIENT}`} } ,
  { path: 'patient/dashboard/updateprofile' , component: UpdatePatientComponent, canActivate: [AuthGuard, RoleGuard], data: {role: `${ROLE_PATIENT}`} } ,
  { path: 'patient/family-member' , component: FamilyMemberComponent, canActivate: [AuthGuard, RoleGuard], data: {role: `${ROLE_PATIENT}`} } ,
  { path: 'patient/appointments/history', component: AppointmentHistoryComponent,canActivate:[AuthGuard,RoleGuard],data: {role: `${ROLE_PATIENT}`} },

  { path: 'doctor/dashboard' , component: DoctorDashboardComponent, canActivate: [AuthGuard, RoleGuard], data: {role: `${ROLE_DOCTOR}`} },
  { path:'doctor/dashboard/update',component:DoctorProfileUpdateComponent, canActivate: [AuthGuard, RoleGuard],data: {role: `${ROLE_DOCTOR}`} },
  { path:'doctor/dashboard/availability',component:DoctorAvailabilityStatusComponent,canActivate: [AuthGuard, RoleGuard],data: {role: `${ROLE_DOCTOR}`} },
 
  { path: 'admin/dashboard' , component: AdminDashboardComponent, canActivate: [AuthGuard, RoleGuard], data: {role: `${ROLE_ADMIN}`} },
  { path:'doctor/consultation/history',component:DoctorConsultationHistoryComponent,canActivate: [AuthGuard, RoleGuard],data: {role: `${ROLE_DOCTOR}`} },
  { path: 'error', component: ErrorComponent },
  { path: '**', component: ErrorComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
