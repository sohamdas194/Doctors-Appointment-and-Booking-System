import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort, Sort } from '@angular/material/sort';
import { MatTable, MatTableDataSource } from '@angular/material/table';
import { BookAppointmentPopupComponent } from './book-appointment-popup/book-appointment-popup.component';
import { AppointmentService } from 'src/app/service/appointment.service';
import { FilteredDoctor } from 'src/app/model/filtered-doctor';
import { FamilyMemberSummary } from 'src/app/model/fam-mem-summ';
import { DoctorRegistrationService } from 'src/app/service/doctor-registration.service';


@Component({
  selector: 'app-appointment',
  templateUrl: './appointment.component.html',
  styleUrls: ['./appointment.component.css'],
})
export class AppointmentComponent implements OnInit, AfterViewInit,OnDestroy {

  minDate!: any;
  maxDate!: any;

  searchDataForm!: FormGroup;

  // specialities: string[] = ["Orthopedics", "Internal Medicine", "Obstetrics and Gynecology", "Dermatology", "Pediatrics", "Radiology", "General Surgery", "Ophthalmology", "Family Medicine", "Chest Medicine", "Anesthesia", "Pathology", "ENT"];
  specialities!: string[];

  displayedColumns: string[] = ['name', 'specialization', 'experience', 'city', 'pincode', 'fee', 'book'];
  dataSource: any = new MatTableDataSource();

  searchformSubmitted: boolean = false;

  familyMembers!: FamilyMemberSummary[];

  // @ViewChild(MatTable) table!: MatTable<any>;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private dialogRef: MatDialog,
    private formBuilder: FormBuilder,
    private appointmentService: AppointmentService,
    private doctorRegistrationService: DoctorRegistrationService
  ) {}
  ngOnDestroy(): void {
    this.dialogRef.closeAll();
  }
  ngOnInit(): void {
    let today = new Date();
    this.minDate = today.getFullYear() + '-' + (today.getMonth()+1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');
    today.setDate(today.getDate() + 30);
    this.maxDate = today.getFullYear() + '-' + (today.getMonth()+1).toString().padStart(2, '0') + '-' + today.getDate().toString().padStart(2, '0');
    // this.table.renderRows();

    this.searchDataForm = this.formBuilder.group({
      docName: [''],
      docSpecialization: ['', Validators.required],
      apmntDate: [''],
      docCity: [''],
      docPincode: [''],
      docExp: [0]
    });

    this.appointmentService.getPatientFamilyMembers().subscribe(
      response => {
        this.familyMembers = response;
      }
    )

    this.doctorRegistrationService.getAllSpecializations().subscribe(
      response => {
        this.specialities = response;
      }
    )
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  resetSearchDataFormValues():void {
    let searchVals = this.searchDataForm.value;
    searchVals.docName = '';
    searchVals.docSpecialization = '';
    searchVals.apmntDate = '';
    searchVals.docCity = '';
    searchVals.docPincode = '';
    searchVals.docExp = 0;
    this.searchDataForm.reset(searchVals);
  }

  showAllDoctors():void {
    this.searchformSubmitted = false;
    this.resetSearchDataFormValues();
    this.searchDoctors();
  }

  resetTable():void {
    this.searchformSubmitted = false;
    this.dataSource = new MatTableDataSource();
    this.resetSearchDataFormValues();
    this.ngAfterViewInit();
  }
  

  openBookingDialog(doc: FilteredDoctor):void {
    this.dialogRef.open(BookAppointmentPopupComponent, {
      width: '50%', maxHeight: '75%',
      data: {
        doctor: doc,
        famMembers : this.familyMembers,
        appointmentDate: this.searchDataForm.value.apmntDate
      }
    });
  }

  onlyNumberKey(evt: any) {
    // Only ASCII character in that range allowed
    let ASCIICode = (evt.which) ? evt.which : evt.keyCode
    if (ASCIICode > 31 && (ASCIICode < 48 || ASCIICode > 57))
        return false;
    return true;
  }

  onSearchClick(): void {
    this.searchformSubmitted = true;
    if(this.searchDataForm.valid) this.searchDoctors();
  }

  searchDoctors(): void {
    this.appointmentService.searchDoctors(this.searchDataForm.value).subscribe(
      response => {
        this.dataSource = new MatTableDataSource(response);
        this.ngAfterViewInit();
      }
    );
  }

}

// const ELEMENT_DATA: FilteredDoctor[] = [
//   {doctorId: 1, name: 'Hydrogen', specialization: 'ENT', experience: 1, city: 'Pune', pinCode: '123456', consultationFee: 1000},
//   {doctorId: 2, name: 'Helium', specialization: 'Orthopedics', experience: 2, city: 'Bangalore', pinCode: '234567', consultationFee: 1500},
//   {doctorId: 3, name: 'Lithium', specialization: 'Obstetrics and Gynecology', experience: 3.3, city: 'Kolkata', pinCode: '345678', consultationFee: 1300},
//   {doctorId: 4, name: 'Beryllium', specialization: 'Dermatology', experience: 4.2, city: 'Hyderabad', pinCode: '456789', consultationFee: 1500},
//   {doctorId: 5, name: 'Boron', specialization: 'Pediatrics', experience: 5.5, city: 'Lucknow', pinCode: '231465', consultationFee: 1020},
//   {doctorId: 6, name: 'Carbon', specialization: 'Radiology', experience: 6.5, city: 'Delhi', pinCode: '234153', consultationFee: 2100},
//   {doctorId: 7, name: 'Nitrogen', specialization: 'General Surgery', experience: 7, city: 'Chennai', pinCode: '908789', consultationFee: 1600},
//   {doctorId: 8, name: 'Oxygen', specialization: 'Ophthalmology', experience: 1, city: 'Kochi', pinCode: '309285', consultationFee: 1030},
//   {doctorId: 9, name: 'Fluorine', specialization: 'Anesthesia', experience: 9.6, city: 'Patna', pinCode: '230194', consultationFee: 1300},
//   {doctorId: 10, name: 'Neon', specialization: 'Internal Medicine', experience: 18.3, city: 'Jaipur', pinCode: '543632', consultationFee: 1700},
// ];
