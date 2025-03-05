
import { Component, OnInit } from "@angular/core";
import { DoctorAvailabilityDto } from "src/app/model/DoctorAvailabilityDto";
import { AuthenticationService } from "src/app/service/authentication.service";
import { DoctorAvailabilityService } from "src/app/service/doctor-availability-service.service";

@Component({
  selector: 'app-doctor-availability-status',
  templateUrl: './doctor-availability-status.component.html',
  styleUrls: ['./doctor-availability-status.component.css']
})
export class DoctorAvailabilityStatusComponent implements OnInit {
  userEmail!: string;
  selectedTimeSlot: string = '8am-12pm';
  availabilityFrom!: string;
  availabilityTo!: string;
  bookedTimeSlots: string[] = [];
  unavailableDate!: string;
  selectedDates: string[] = [];
  unavailableDates: string[] = [];
  availableDates: DoctorAvailabilityDto[] = [];
  today: string;
  dateRange: string[] = [];
  minDate!: string;
  maxDate: string;
  showCalendar: boolean = false;
  isAvailableToday: boolean = false;
  userId!: number;
  lastSelectedAvailableRange: { from: Date, to: Date } | null = null;

  availableTimeSlots: string[] = [];
  isEditMode: boolean = false;
  isSavedEnabled: boolean = false;
  showModal: boolean = false;
  selectedAvailabilityDetails!: DoctorAvailabilityDto;

  constructor(
    public authService: AuthenticationService,
    private doctorAvailabilityService: DoctorAvailabilityService
  ) {
    const today = new Date();
    const maxDate = new Date();
    maxDate.setDate(today.getDate() + 30);
    this.today = today.toISOString().split('T')[0];
    this.maxDate = maxDate.toISOString().split('T')[0];
    this.userId = parseInt(localStorage.getItem('authenticatedUserId') ?? '');
  }

  ngOnInit(): void {
    this.userEmail = this.authService.getAuthenticateduserEmail() || '';
    this.getDoctorAvailability();
  }

  ngAfterViewInit() {
    console.log('showModal', this.showModal);
  }

  getDoctorAvailability(): void {
    console.log("load");
    this.doctorAvailabilityService.getAllDates(this.userId).subscribe((response) => {
      console.log("load2");
      this.availableDates = response;
      if (this.availableDates.length <= 2) {
        // window.alert("You can update your calendar now")
      }
      this.updateCalendar();
      this.checkPreviousSelectedRange();
    },

      (error) => {
        console.error("Error Loading Doctor Availability");
      }
    )

  }

  updateCalendar(): void {
    this.availableTimeSlots = [];
    this.availableDates.forEach(item => {
      let timeSlot = '';
      let date: Date;
      if (typeof item.date === 'string') {
        date = new Date(item.date);
      } else if (item.date instanceof Date) {
        date = item.date;
      } else {
        console.error('Unexpected date format in DoctorAvailabilityDto');
        return;
      }
      if (item.status) {
        if (item.shift === 1) {
          timeSlot = '8am-12pm';
        } else if (item.shift === 2) {
          timeSlot = '2pm-6pm';
        } else {
          timeSlot = 'Leave';
        }
      } else {
        timeSlot = 'Unavailable';
      }
      this.availableTimeSlots.push(`${date.toLocaleDateString()} - ${timeSlot}`);
    });
  }

  checkPreviousSelectedRange(): void {
    if (this.lastSelectedAvailableRange) {
      this.availabilityFrom = this.lastSelectedAvailableRange.from.toISOString().split('T')[0];
      this.availabilityTo = this.lastSelectedAvailableRange.to.toISOString().split('T')[0];
      this.updateDateRange();
      this.isEditMode = true;
    }
    else {
      this.isEditMode = false;
    }
  }
  updateDateRange(): void {
    const today = new Date();
    const maxDate = new Date(today.getTime() + 30 * 24 * 60 * 60 * 1000);
    if (this.availabilityFrom && this.availabilityTo) {
      const fromDate = new Date(this.availabilityFrom);
      const toDate = new Date(this.availabilityTo);

      if (fromDate < today) {
        this.availabilityFrom = today.toISOString().split('T')[0];
        fromDate.setDate(today.getDate());
      }

      // if (toDate > maxDate) {
      //   this.availabilityTo = maxDate.toISOString().split('T')[0];
      //   toDate.setDate(maxDate.getDate());
      // }

      if (fromDate > toDate) {
        this.availabilityTo = this.availabilityFrom;
        toDate.setDate(fromDate.getDate());
      }



      this.dateRange = [];
      while (fromDate <= toDate) {
        this.dateRange.push(new Date(fromDate).toISOString().split('T')[0]);
        fromDate.setDate(fromDate.getDate() + 1);
      }
      this.isSavedEnabled = this.dateRange.length > 0;
    }
    else {
      this.isSavedEnabled = false;
    }
  }

  setMaxToDateConstraint() {

    let today = new Date(this.availabilityFrom);
    this.minDate = today.getFullYear() + '-' + (today.getMonth() + 1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');
    today.setDate(today.getDate() + 30);
    this.maxDate = today.getFullYear() + '-' + (today.getMonth() + 1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');
  }


  addUnavailableDate(): void {
    if (this.unavailableDate && !this.unavailableDates.includes(this.unavailableDate)) {
      this.unavailableDates.push(this.unavailableDate);
    }
  }


  showAvailableDetails(availableDate: DoctorAvailabilityDto) {
    console.log("clicked");


    console.log(availableDate);
    this.selectedAvailabilityDetails = { ...availableDate };

    this.showModal = true;
    console.log(this.showModal);

  }

  closeModal() {
    this.showModal = false;
  }


  getShiftLabel(shift: number | undefined): string {
    switch (shift) {
      case 1:
        return '8am-12pm';

      case 2:
        return '2pm-6pm';
      case 0:
        return 'Leave';

      default:
        return 'Unknown';
    }
  }


  updateAvailable(availabilityDetails: DoctorAvailabilityDto) {
    const updatedAvailabilityDetails = { ...availabilityDetails };
    if (updatedAvailabilityDetails.shift === 0) {
      updatedAvailabilityDetails.status = false;
    }
    this.doctorAvailabilityService.updateDate(this.userId, availabilityDetails.date, updatedAvailabilityDetails).subscribe((response) => {
      console.log("Availability Updated", response);
      this.getDoctorAvailability();
      this.closeModal();
    },
      (error) => {
        window.alert("Please change your slot prior to a day or current day before 8AM")
        console.error("Error updating availability:", error);
      }
    )
  }



  saveDoctorDates(): void {
    console.log("clicked");
    console.log(this.availabilityFrom.length);
    console.log(this.availabilityTo.length);

    if (this.availabilityFrom && this.availabilityTo) {
      console.log(this.dateRange);
      const doctorAvailable: DoctorAvailabilityDto[] = this.dateRange.map(date => {
        let shift = this.selectedTimeSlot === '8am-12pm' ? 1 : 2;
        let status = true;
        if (shift === 0) {
          status = false;
        }
        return {
          date: new Date(date),
          status: status,
          shift: shift
        };
      });
      console.log("doctorAvailable", doctorAvailable);
      this.doctorAvailabilityService.saveAllDoctorDates(this.userId, doctorAvailable).subscribe((response) => {
        console.log(this.userId);
        console.log("Doctor Dates saved successfully", response);
        this.availabilityFrom = '';
        this.availabilityTo = '';
        this.dateRange = [];
        this.showCalendar = false;
        this.getDoctorAvailability();
      },
        (error) => {
          console.log("Please fill in all required field.");
        }
      )
    }
    else {
      console.log("error");
    }
  }
}
