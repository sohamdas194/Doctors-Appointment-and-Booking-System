import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarConfig, MatSnackBarHorizontalPosition, MatSnackBarVerticalPosition } from '@angular/material/snack-bar';
import { AppointmentSlots } from 'src/app/model/appointment-slots';
import { FamilyMemberSummary } from 'src/app/model/fam-mem-summ';
import { FilteredDoctor } from 'src/app/model/filtered-doctor';
import { AppointmentService } from 'src/app/service/appointment.service';
import { CONFIG_ERROR, CONFIG_SUCCESS } from 'src/app/util/snack-bar-configs';

@Component({
  selector: 'app-book-appointment-popup',
  templateUrl: './book-appointment-popup.component.html',
  styleUrls: ['./book-appointment-popup.component.css']
})
export class BookAppointmentPopupComponent implements OnInit {

  bookingDateFormSubmitted: boolean = false;
  bookingFormSubmitted: boolean = false;

  bookingDateForm!: FormGroup;
  bookingForm!: FormGroup;

  minDate!: any;
  maxDate!: any;

  doctor: FilteredDoctor;

  isBookingDateSet: boolean = false;

  enableFamilySelectorBool: boolean = false;

  apmntDate!: string;
  familyMembers: FamilyMemberSummary[];

  appointmentSlots!: AppointmentSlots;

  firstHalfSlotLookup: string[] = ['8:00-8:30', '8:30-9:00', '9:00-9:30', '9:30-10:00', '10:00-10:30', '10:30-11:00', '11:00-11:30', '11:30-12:00'];
  secondHalfSlotLookup: string[] = ['14:00-14:30', '14:30-15:00', '15:00-15:30', '15:30-16:00', '16:00-16:30', '16:30-17:00', '17:00-17:30', '17:30-18:00'];

  constructor(
    @Inject(MatDialogRef<BookAppointmentPopupComponent>) private dialogRef: any,
    @Inject(MAT_DIALOG_DATA) private data: any,
    private formBuilder: FormBuilder,
    private appointmentService: AppointmentService,
    private snackBar: MatSnackBar
  ) {
    this.doctor = data.doctor;
    this.apmntDate = data.appointmentDate;
    this.familyMembers = data.famMembers;
  }

  ngOnInit(): void {
    let today = new Date();
    this.minDate = today.getFullYear() + '-' + (today.getMonth() + 1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');
    today.setDate(today.getDate() + 30);
    this.maxDate = today.getFullYear() + '-' + (today.getMonth() + 1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');

    this.bookingDateForm = this.formBuilder.group({
      apmntDate: [this.apmntDate === '_' ? '' : this.apmntDate, Validators.required]
    });

    this.bookingForm = this.formBuilder.group({
      familySelector: [''],
      famMemId: [''],
      apmntSlot: ['', Validators.required],
      patientCmnt: ['', Validators.required]
    }, {
      validator: this.memberValidator('familySelector', 'famMemId')
    });
  }

  memberValidator(famSelector: string, famMemberId: string) {
    return (formGroup: FormGroup) => {
      const familySelector = formGroup.controls[famSelector];
      const famMemId = formGroup.controls[famMemberId];

      if (famMemId.value === '' && familySelector.value){
        famMemId.setErrors({ memberRequired: true });
      }

      return null;
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onForSelfClick(): void {
    this.enableFamilySelectorBool = false;
    let bookingVals = this.bookingForm.value;
    bookingVals.famMemId = "";
    this.bookingForm.reset(bookingVals);
  }

  onBookClick(): void {
    this.bookingFormSubmitted = true;
    if (this.bookingForm.invalid) return;

    let bookingVals = this.bookingForm.value;
    this.appointmentService.bookAppointment(
      bookingVals.famMemId,
      this.appointmentSlots.doctorAvailabilityId,
      bookingVals.apmntSlot,
      bookingVals.patientCmnt
    ).subscribe(
      response => {
        this.openSnackBar("Appointment scheduled!", CONFIG_SUCCESS);
      }, error => {
        this.openSnackBar(error.error.message, CONFIG_ERROR);
      }
    )
    this.onNoClick();

  }

  onBackClick(): void {
    this.enableFamilySelectorBool = false;
    let bookingVals = this.bookingForm.value;
    bookingVals.familySelector=false;
    bookingVals.famMemId = '';
    bookingVals.apmntSlot = '';
    bookingVals.patientCmnt = '';
    this.bookingDateForm.reset(bookingVals);
    this.isBookingDateSet = false;
    this.bookingFormSubmitted = false;
    this.bookingDateFormSubmitted = false;
  }

  onDateSetClick(): void {
    if (this.bookingDateForm.invalid) return;

    this.appointmentService.isDoctorAvailableOnDate(this.doctor.doctorId, this.bookingDateForm.value.apmntDate).subscribe(
      response => {
        this.isBookingDateSet = response.isDoctorAvailable;
        if (this.isBookingDateSet) {
          this.appointmentService.getAvailableAppointmentSlots(this.doctor.doctorId, this.bookingDateForm.value.apmntDate).subscribe(
            response => {
              this.appointmentSlots = response;
            },
            error => {
              this.openSnackBar(error.error.message, CONFIG_ERROR);
              this.onBackClick();
            }
          )
        } else {
          this.openSnackBar("Doctor not available on this day", CONFIG_ERROR);
        }
      }
    )
  }

  openSnackBar(message: string, config: MatSnackBarConfig) {
    this.snackBar.open(message, "close", config);
  }

}
