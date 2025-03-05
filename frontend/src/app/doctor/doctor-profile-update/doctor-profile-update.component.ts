import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { DoctorRegistrationService } from 'src/app/service/doctor-registration.service';
import { DoctorDto } from 'src/app/model/doctor-dto';
import { API_URL } from 'src/app/app.constants';

@Component({
  selector: 'app-doctor-profile-update',
  templateUrl: './doctor-profile-update.component.html',
  styleUrls: ['./doctor-profile-update.component.css']
})
export class DoctorProfileUpdateComponent implements OnInit {

  doctorUpdatePage!:FormGroup;
  public editable:boolean=true;
  userId!:number;
  
  readonly!:boolean;
  specializations!:string[];
  submitButton!:HTMLButtonElement;

  constructor(
    private formBuilder: FormBuilder, 
    // private customValidationService: CustomValidationService, 
    private http: HttpClient, 
    private router: Router, 
    private doctorRegistrationService: DoctorRegistrationService,
  )
     {
      this.userId=parseInt(localStorage.getItem('authenticatedUserId')??'');
     
      console.log('localStorage[\'authenticatedUserId\']:', localStorage['authenticatedUserId']);
     
  }

  ngOnInit(): void {
    this.doctorUpdatePage=this.formBuilder.group({
     
        email: ['', [Validators.required, Validators.email, this.emailValidator]],
        firstName: ['', [Validators.required, Validators.minLength(3), this.nameValidator]],
        lastName: ['', [Validators.required, Validators.minLength(3), this.nameValidator]],
        gender: ['', [Validators.required]],
        dateOfBirth: ['', [Validators.required, this.validateAge(23)]],
        phoneNumber: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]{10}$')]],
        country: ['', [Validators.required, Validators.minLength(3), this.nameValidator]],
        address1: ['', [Validators.required, Validators.minLength(3)]],
        address2: [''],
        state: ['', [Validators.required, Validators.minLength(3), this.nameValidator]],
        city: ['', [Validators.required, Validators.minLength(3), this.nameValidator]],
        pinCode: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(6), Validators.pattern('^[0-9]{6}$')]],
        consultingFrom: ['', [Validators.required, this.validateConsultingDate]],
        specialization: ['', [Validators.required]],
        bloodGroup: ['', [Validators.required]]
      });
      console.log(this.userId);
      this.getUserDetails();
      this.getAllSpecialization();
      this.checkFormValidity();
  }

  nameValidator(control: FormControl): { [key: string]: boolean } {
    const nameRegex = /^[A-Za-z]+([A-Za-z]+([ ][A-Za-z]+)*)?$/;
    if (!nameRegex.test(control.value)) {
      return { invalidName: true };
    }
    return {};
  }

  emailValidator(control: FormControl): { [key: string]: boolean } {
    const emailRegex = /^[a-zA-Z0-9._]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,}$/;
    if (!emailRegex.test(control.value)) {
      return { invalidEmail: true };
    }
    return {};
  }

  validateAge(minAge: number): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      if (control.value) {
        const birthDate = new Date(control.value);
        const today = new Date();
        let age = today.getFullYear() - birthDate.getFullYear();
        const monthDiff = today.getMonth() - birthDate.getMonth();
        if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birthDate.getDate())) {
          age--;
        }
        return age < minAge ? { invalidAge: true } : null;
      }
      return null;
    };
  }

  validateConsultingDate(control: AbstractControl): ValidationErrors | null {
    const today = new Date();
    const consultingDate = new Date(control.value);
    return consultingDate > today ? { invalidConsultingDate: true } : null;
  }

  checkFormValidity() {
  this.submitButton=document.getElementById('submitBtn') as HTMLButtonElement;
  if(this.doctorUpdatePage.valid){
    this.submitButton.disabled=false;
  }
  else{
    this.submitButton.disabled=true;
  }
  }

  getUserDetails():void{
    this.doctorRegistrationService.getUserByID(this.userId).subscribe((res:DoctorDto)=>{
      console.log(res);
      this.doctorUpdatePage.patchValue(res);
    })
  }

  public updateProfile():void{
    this.doctorRegistrationService.updateUserByUserID(this.userId,this.doctorUpdatePage.value).subscribe((res:DoctorDto)=>{
      console.log(res);
    })
  }

  getAllSpecialization():void{
  this.doctorRegistrationService.getAllSpecializations().subscribe(
    (speicalization)=>{
      this.specializations=speicalization;
    },
    (error)=>{
      console.error('Error on retrieving specialization:',error);
    }
  )
}


  onPaste(event: ClipboardEvent) {
    event.preventDefault();
  }
  
  onCopy(event: ClipboardEvent) {
    event.preventDefault();
  }
  

  onReset(): void {
this.ngOnInit();

   this.getUserDetails();
  }

  closeModal() {
    this.router.navigate(['doctor/dashboard']);
  }

  onSubmit() {
 
    if(this.doctorUpdatePage.valid){
 
    let doctorDto = new DoctorDto(this.doctorUpdatePage.value);
    this.doctorRegistrationService.updateUserByUserID(this.userId,this.doctorUpdatePage.value).subscribe((res:DoctorDto)=>{
      console.log(res);
      window.alert("details updated")
        this.router.navigate(['doctor/dashboard']);
     
    })
   
  }
 
  }
}


