import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { ActivatedRoute, Router} from '@angular/router';
import { PatientDto } from 'src/app/model/patient-dto';
import { PatientRegistrationService } from 'src/app/service/patient-registration.service';


@Component({

 selector: 'app-update-patient',
 templateUrl: './update-patient.component.html',
 styleUrls: ['./update-patient.component.scss']
})

export class UpdatePatientComponent implements OnInit {

 userId = parseInt(localStorage.getItem('authenticatedUserId')??'');
 patientData: undefined | PatientDto;

 //patient: PatientDto = new PatientDto('','','', '', '', ,0,0,'','','',0,'','','','','','','','','','','','','','', , new InsuranceDto('', '','',));

 patientMessage: undefined | string;
 constructor(private route: ActivatedRoute, private patientService: PatientRegistrationService, private router:Router ) { }

  onSubmit(){
  let patientDto: PatientDto = new PatientDto(this.patientRegForm.value);
   console.log(patientDto);
   this.updatePatientProfile(patientDto);
   window.alert('Patient Successfully Updated');
   }



  updatePatientProfile(patientDto: PatientDto){
  this.patientService.updateprofile(this.userId,patientDto).subscribe((response: any)=>{
   if(response.SUCCESS){
    this.router.navigate(['patient/dashboard']);
    //this.patientMessage = "Patient profile has updated";
   }
  });

  setTimeout(()=>{
   this.patientMessage = undefined
  }, 5000)
 }


   ngOnInit(): void {
   let userId = parseInt(localStorage.getItem('authenticatedUserId')??'');
   console.warn(userId);
   userId && this.patientService.getPatient(userId).subscribe((data:any)=>{
   console.warn(data);
   this.patientData = data
   })
 }

 isDisabled = true;
 MaritalStatus: string[] = ['Married', 'UnMarried'];
 maxDate: string = '';
 birthDate: Date | null = null;


 patientRegForm = new FormGroup({
  firstname: new FormControl("", [Validators.required, Validators.minLength(3), Validators.pattern('^[a-zA-Z ]*$')]),
  lastname: new FormControl("", [Validators.required, Validators.minLength(3), Validators.pattern('^[a-zA-Z ]*$')]),
  emergencyFirstName: new FormControl("", [Validators.required, Validators.minLength(3), Validators.pattern('^[a-zA-Z ]*$')]),
  emergencyLastName: new FormControl("", [Validators.required, Validators.minLength(3), Validators.pattern('^[a-zA-Z ]*$')]),
  emergencyRelationship: new FormControl("", [Validators.required, Validators.maxLength(50),Validators.pattern('^[a-zA-Z ]*$')]),
  insuranceCompany: new FormControl("", [Validators.minLength(5),Validators.pattern('^[a-zA-Z ]*$')]),
  insuranceId: new FormControl("", [Validators.maxLength(10)]),
  policyHolderName: new FormControl("", [Validators.maxLength(30), Validators.pattern('^[a-zA-Z ]*$')]),
  policyDate: new FormControl("", [this.validateAge(2)]),

  //email: new FormControl("", [Validators.required, Validators.email]),
  reasonForRegistration: new FormControl("", [ Validators.minLength(100),Validators.pattern('^[a-zA-Z ]*$') ]),
  additionalNotes: new FormControl("", [ Validators.minLength(200), Validators.pattern('^[a-zA-Z ]*$')]),
  password: new FormControl("", [Validators.required, Validators.minLength(7), Validators.maxLength(14), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$')]),
  confirmPassword: new FormControl("", [Validators.required, Validators.minLength(7), Validators.maxLength(14), Validators.pattern('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{7,}$')]),
  gender: new FormControl("", [Validators.required]),
  bloodGroup: new FormControl("", [Validators.required]),
  dob: new FormControl("", [Validators.required, this.validateAge(23)]),
  maritalStatus: new FormControl("", [Validators.required]),
  weight: new FormControl("", [Validators.required, Validators.min(0), Validators.max(300),Validators.pattern('^[0-9]*$')]),
  height: new FormControl("", [Validators.required, Validators.min(0), Validators.max(300), Validators.pattern('^[0-9]*$')]),
  contactNumber: new FormControl("", [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]),
  emergencyContact: new FormControl("", [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]),

  // healthHistory: new FormControl("", [Validators.required]),
  street1: new FormControl("", [Validators.required, Validators.maxLength(100)]),
  landmark: new FormControl("", [Validators.required, Validators.maxLength(100),Validators.pattern('^[a-zA-Z ]*$')]),
  city: new FormControl("", [Validators.required, Validators.maxLength(100),Validators.pattern('^[a-zA-Z ]*$')]),
  state: new FormControl("", [Validators.required, Validators.maxLength(100),Validators.pattern('^[a-zA-Z ]*$')]),
  pincode: new FormControl("", [Validators.required, Validators.minLength(6), Validators.maxLength(6), Validators.pattern('^[0-9]*$')])
 });


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



   hasError(controlName: string, errorName: string): boolean {
   const control = this.patientRegForm.get(controlName);
   return control ?
   control.hasError(errorName) &&
   control.touched : false;
 }

}

































