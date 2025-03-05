import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { PatientDto } from 'src/app/model/patient-dto';
import { PatientRegistrationService } from 'src/app/service/patient-registration.service';



@Component({
 selector: 'app-patient-signup',
 templateUrl: './patient-signup.component.html',
 styleUrls: ['./patient-signup.component.scss']
})

export class PatientSignupComponent implements OnInit {

 email: any;

 bloodGroups: string[] = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];
 MaritalStatus: string[] = ['Married', 'UnMarried'];
 genders: string[] = ['Male', 'Female', 'Other'];
 maxDate: string = '';
 patientRegForm!:FormGroup;
 birthDate: Date | null = null;
 selectedGender: string = '';

 onSubmit() {
  let patientDto: PatientDto = new PatientDto(this.patientRegForm.value);
  this.savePatient(patientDto);
  window.alert('Patient Successfully Registered!');
 }



 passwordMatchValidator(password: string, confirmPassword: string): ValidatorFn {
  return (formGroup: AbstractControl): ValidationErrors | null => {
   const passControl = formGroup.get(password);
   const confirmPassControl = formGroup.get(confirmPassword);
   if (!passControl || !confirmPassControl) {
    return null;
   }

   const passValue = passControl.value;
   const confirmPassValue = confirmPassControl.value;

   if (passValue !== confirmPassValue) {
    confirmPassControl.setErrors({ passwordMismatch: true });
    return { passwordMismatch: true };
   } else {
    confirmPassControl.setErrors(null);
    return null;
   }
  };
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



 constructor(private patientRegistrationService: PatientRegistrationService, private router: Router) { }
 ngOnInit(): void {
  this.patientRegForm = new FormGroup({
    firstname: new FormControl("", [Validators.required, Validators.minLength(3), Validators.pattern('^[a-zA-Z ]*$')]),
    lastname: new FormControl("", [Validators.required, Validators.minLength(3),Validators.pattern('^[a-zA-Z ]*$')]),
    email: new FormControl("", [Validators.required, Validators.email]),
    password: new FormControl("", [Validators.required, Validators.minLength(7), Validators.maxLength(14), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$')]),
    confirmPassword: new FormControl("", [Validators.required, Validators.minLength(7), Validators.maxLength(14), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$')]),
    gender: new FormControl("", [Validators.required]),
    bloodGroup: new FormControl("", [Validators.required]),
    dob: new FormControl("", [Validators.required, this.validateAge(18)]),
    maritalStatus: new FormControl("", [Validators.required]),
    contactNumber: new FormControl("", [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]),
    emergencyContact: new FormControl("", [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]),
    street1: new FormControl("", [Validators.required, Validators.minLength(4), Validators.maxLength(100)]),
    landmark: new FormControl("", [Validators.required,Validators.minLength(4), Validators.maxLength(100),Validators.pattern('^[a-zA-Z ]*$')]),
    city: new FormControl("", [Validators.required,Validators.minLength(3),Validators.maxLength(100),Validators.pattern('^[a-zA-Z ]*$')]),
    state: new FormControl("", [Validators.required,Validators.minLength(3),Validators.maxLength(100),Validators.pattern('^[a-zA-Z ]*$')]),
    pincode: new FormControl("", [Validators.required, Validators.minLength(6), Validators.maxLength(6), Validators.pattern('^[0-9]*$')])
   }, { validators: this.passwordMatchValidator('password', 'confirmPassword') });

 }


 savePatient(patientDto: PatientDto) {
  this.patientRegistrationService.createPatient(patientDto).subscribe((response: any) => {
   if (response.SUCCESS) {
    this.router.navigate(['login']);
   } else {
    console.error('registration failed');
   }
  });
 }


   usedEmail:boolean=false;
     checkEmail(_event: any) {
     this.usedEmail = false;
     const email = this.patientRegForm.value.email;
     console.log(email);
      this.patientRegistrationService.getPatientByEmail(email).subscribe(
       (usedEmail = true) => {
         console.log('Email exists:', usedEmail);
         this.usedEmail=usedEmail;
         console.log('usedEmail', this.usedEmail);
         },
         (error)=>{
           console.log('Error checking email:', error);
           this.usedEmail = false;
         }
     );
   }


 hasError(controlName: string, errorName: string): boolean {
  const control = this.patientRegForm.get(controlName);
  return control ?
   control.hasError(errorName) &&
   control.touched : false;
 }

}
 