import { Component, OnInit } from '@angular/core';

import { AuthenticationService } from '../service/authentication.service';

import { Router } from '@angular/router';

import { ROLE_DOCTOR } from '../app.constants';

import { DoctorProfileUpdateComponent } from '../doctor/doctor-profile-update/doctor-profile-update.component';

@Component({

  selector: 'app-menu',

  templateUrl: './menu.component.html',

  styleUrls: ['./menu.component.css']

})

export class MenuComponent implements OnInit {

  isUserLoggedIn!: boolean;

  showAvailability: boolean = false;

  showUnavailableDates: boolean = false;

  today: string;

  maxDate: string;

  unavailableDate!: string;

  unavailableDates: string[] = [];

  name!: string;

  doctorSelector!: string;

  currentUrl: string = '';


  constructor(

    public authService: AuthenticationService,

    public router: Router,




  ) {

    const today = new Date();

    const maxDate = new Date();

    maxDate.setDate(today.getDate() + 30);

    this.today = today.toISOString().split('T')[0];

    this.maxDate = maxDate.toISOString().split('T')[0];

  }



  ngOnInit(): void {

    this.isUserLoggedIn = this.authService.isUserLoggedIn();
    this.currentUrl = this.router.url;
    // this.setSelectedOptionInDropdown();

  }



  handleDashboardClick() {

    switch (this.authService.getAuthenticatedUserRole()) {

      case 'patient': {

        this.router.navigate(['patient/dashboard']);

        break;

      }

      case 'doctor': {

        this.router.navigate(['doctor/dashboard']);

        break;

      }

      case 'admin': {

        this.router.navigate(['admin/dashboard']);

        break;

      }

      default: {

        this.router.navigate(['error']);

        break;

      }

    }

  }



  // doctorUpdatePage() {

  // if (this.authService.getAuthenticatedUserRole()) {}

  // setSelectedOptionInDropdown() {
  //   if (this.currentUrl.includes('/doctor/dashboard/availability')) {
  //     this.showAvailability = true;
  //   } else if (this.currentUrl.includes('/doctor/dashboard/update')) {
  //     this.showAvailability = true;
  //   } else {
  //     this.showAvailability = false;
  //   }

  // }



  // onDashboardOptionChange(event: Event) {

  //   const selectedValue = (event.target as HTMLSelectElement).value;

  //   this.showAvailability = false;

  //   this.showUnavailableDates = false;



  //   switch (selectedValue) {

  //     case 'goToDashboard':

  //       this.handleDashboardClick();
  //       this.router.navigate(['doctor/dashboard']);

  //       break;

  //     case 'availableStatus':

  //       this.showAvailability = true;
  //       this.router.navigate(['doctor/dashboard/availability']);

  //       break;

  //     case 'doctorUpdate':
  //       this.showAvailability = true;
  //       this.router.navigate(['doctor/dashboard/update']);
  //       break;
  //   }

  // }

  handleUpdateProfileClick() {

    if (this.authService.getAuthenticatedUserRole()) {

      this.router.navigate(['patient/dashboard/updateprofile']);

    }

  }

  familyMemberPage() {

    if (this.authService.getAuthenticatedUserRole() === 'patient') {

      this.router.navigate(['patient/family-member']);
    }

  }

  DoctorUpdateProfile() {
    if (this.authService.getAuthenticatedUserRole() === 'doctor') {
      this.router.navigate(['doctor/dashboard/update']);
    }
  }

  appointmentHistory() {
    if (this.authService.getAuthenticatedUserRole() === 'patient') {
      this.router.navigate(['patient/appointments/history']);
    }
  }

  DoctorAvailabilityStatus() {

    this.router.navigate(['doctor/dashboard/availability']);

  }

  DoctorConsultationHistory() {
    this.router.navigate(['doctor/consultation/history']);
  }

}
