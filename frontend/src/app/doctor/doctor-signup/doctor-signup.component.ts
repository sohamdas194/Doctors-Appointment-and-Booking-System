import { HttpClient } from '@angular/common/http';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, ValidationErrors, ValidatorFn, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { API_URL } from 'src/app/app.constants';
import { DoctorDto } from 'src/app/model/doctor-dto';
import { CustomValidationService } from 'src/app/service/custom-validation.service';
import { DoctorRegistrationService } from 'src/app/service/doctor-registration.service';
import { tap } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';
import { EmailService } from 'src/app/service/email.service';
@Component({
  selector: 'app-doctor-signup',
  templateUrl: './doctor-signup.component.html',
  styleUrls: ['./doctor-signup.component.css']
})
export class DoctorSignupComponent implements OnInit {

  maxDate!: string;

  birthDate: Date | null = null;

  DoctorRegistrationPage!: FormGroup;

  selectedGender: string = "";

  private getjsonvalue: any;

  emailValid:boolean=false;

  specializations!:string[];

  submitButton!:HTMLButtonElement;


  constructor(
    private formBuilder: FormBuilder, 
    private customValidationService: CustomValidationService, 
    private http: HttpClient, 
    private doctorRegistrationService: DoctorRegistrationService, 
    
    private router: Router,
    private emailService: EmailService) 
    {

   
  }

  ngOnInit(): void {
    const today = new Date();
    this.maxDate = today.toISOString().split('T')[0];
    this.DoctorRegistrationPage = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email,this.emailValidators.bind(TouchList)],],
      password: ['', [Validators.required, this.customValidationService.patternValidator()]],
      confirmpassword: ['', [Validators.required]],
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
      consultingFrom: ['', [Validators.required, this.validConsultingDate]],
      specialization: ['', [Validators.required]],
      bloodGroup: ['', [Validators.required]],
   
    }, {
      
      validator: this.customValidationService.matchPassword('password', 'confirmpassword'),
      // validators: this.consultingFutureValidator("dateOfBirth","consultingFrom"), 
    });
    this.getAllSpecialization();
    this.checkFormValidity();
  }
  
  

  onPaste(event: ClipboardEvent) {
    event.preventDefault();
  }
  
  onCopy(event: ClipboardEvent) {
    event.preventDefault();
  }
  

  consultingFutureValidator(dateOfBirth: string, consultingFrom: string) {
    return (formGroup: FormGroup) => {
      const consultngFrmCtrl = formGroup.controls[consultingFrom];
      const dob = new Date(formGroup.controls[dateOfBirth].value);
      const consultingFrm = new Date(consultngFrmCtrl.value);
    
      if (dob >consultingFrm){
        consultngFrmCtrl.setErrors({ invalidConsultingFutureDate: true });
      }
      if(this.getAgeInYears(dob, consultingFrm) <23){
        
        consultngFrmCtrl.setErrors({ invalidConsultingAge: true });
      }
    
      return null;
    }
  }

  nameValidator(control: FormControl): { [key: string]: boolean } {
    const nameRegex = /^[A-Za-z]+([A-Za-z]+([ ][A-Za-z]+)*)?$/;
    if (!nameRegex.test(control.value)) {
      return { invalidName: true };
    }
    return {};
  }

  emailValidator(control: FormControl): { [key: string]: boolean } {
    const emailRegex = /^[a-zA-Z0-9. _%+-]+[@][a-zA-Z0-9. -]+\\. [a-zA-Z]{3,}/;
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

  validConsultingDate(control: AbstractControl): ValidationErrors | null {
    const today = new Date();
    // const birthDate=new Date(this.DoctorRegistrationPage.get('dateOfBirth')?.value);
    const consultingDate = new Date(control.value);
    return (consultingDate > today  )? { invalidConsultingDate: true } : null;
  }

  get firstname() {
    return this.DoctorRegistrationPage.get('firstName');
  }

  checkFormValidity() {
    this.submitButton = document.getElementById('submitBtn') as HTMLButtonElement;
    if (this.DoctorRegistrationPage.valid) {
      this.submitButton.disabled = false;
    } else {
      this.submitButton.disabled = true;
    }
  }


  emailValidators(control:FormControl):{[key:string]:boolean}{
    if(this.emailValid){
      return {unique:true};
    }
    return {};
  }
 

  


  usedEmail:boolean=false;
  checkEmail(_event: any) {
    this.usedEmail = false;
    const email = this.DoctorRegistrationPage.value.email;
    console.log(email);
   this.doctorRegistrationService.getDoctorByEmail(email).subscribe(
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

  validateConsultingDate(control:AbstractControl):ValidationErrors|null{
    const birthDate=new Date(this.DoctorRegistrationPage.get('dateOfBirth')?.value);
    const consultingDate=new Date(control.value);
    const diffInYears=this.getAgeInYears(birthDate,consultingDate);
    if(diffInYears<23){
      return {
        invalidConsultingDate:true
      };
    }
    if(consultingDate<birthDate){
      return{
        invalidConsultingDate: true
      };
    }
    if (consultingDate > new Date()) {
      return { invalidConsultingFutureDate: true };
    }
  
    return null;
  }


  getAgeInYears(birthDate:Date,consultingDate:Date):number
{
  const diffInMs=consultingDate.getTime()-birthDate.getTime();
  const diffInYears=diffInMs/(1000*60*60*24*365.25);
  return Math.floor(diffInYears);
}  

  onSubmit() {
   
   if (this.DoctorRegistrationPage.invalid) {
    window.alert("Invalid");
     
   }
    let doctorDto = new DoctorDto(this.DoctorRegistrationPage.value);

   

    this.doctorRegistrationService.registerDoctor(doctorDto).subscribe((response: any) => {
      if (response.SUCCESS) {
        window.alert("Doctor Registration Sussesufull");
        this.router.navigate(['login']);
      }
      else {
        console.log("Doctor Registration Failed");
      }
    });
  }
}

