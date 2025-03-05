import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormGroup, FormControl, Validators, ValidatorFn, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router';
import { FamilyDetailsService } from 'src/app/service/family-members.service';
import { FamilyDto } from 'src/app/model/family-dto';
// import {  ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-family-member',
  templateUrl: './family-member.component.html',
  styleUrls: ['./family-member.component.css']
})
export class FamilyMemberComponent implements OnInit {

  userId = parseInt(localStorage.getItem('authenticatedUserId')??'');
  memberId:any;
  familyDto:any;
  selectedFamilyMember:any={};
  family:any[]=[];
  editingIndex = -1;

  bloodGroups: string[] = ['A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-'];
  relationship: string[] = ['Mother', 'Father', 'Son', 'Daughter', 'Wife','Husband'];
  genders: string[] = ['Male', 'Female', 'Other'];
  Form: any;
  familyRegForm!:FormGroup;
  
  onSubmit() {
    console.log("Getting userid");
    console.warn(this.userId);

}

  constructor(private familyDetailsService: FamilyDetailsService, private router: Router,private http:HttpClient) { }
 
  ngOnInit(): void {
    this.familyRegForm = new FormGroup({
      firstname: new FormControl("", [Validators.required, Validators.minLength(4)]),
      lastname: new FormControl("", [Validators.required, Validators.minLength(1)]),
      gender: new FormControl("", [Validators.required]),
      bloodGroup: new FormControl("", [Validators.required]),
      dob: new FormControl("", [Validators.required,Validators.minLength(10), Validators.maxLength(10)]),
      contactNo: new FormControl("", [Validators.required, Validators.minLength(10), Validators.maxLength(10), Validators.pattern('^[0-9]*$')]),
      relationship: new FormControl("", [Validators.required]),
      weight: new FormControl("", [Validators.required, Validators.minLength(1), Validators.maxLength(3), Validators.pattern('^[0-9]*$')]),
      height: new FormControl("", [Validators.required, Validators.minLength(2), Validators.maxLength(3), Validators.pattern('^[0-9]*$')]),
      healthHistory: new FormControl("", [Validators.required, Validators.minLength(3), Validators.maxLength(300)]),
      
    });
     this.getFamily(this.userId);
     
  }
 
  saveFamilyDetails() {
    console.log("saving details");
    let familyDto: FamilyDto = new FamilyDto(this.familyRegForm.value);
    this.familyDetailsService.addFamilyDetails(this.userId,familyDto).subscribe((response:any) => {
      if (response.success) {
        console.log(response);
        this.getFamily(this.userId);
        window.alert('Family Member Successfully Added');
        this.closeAddFamilyMember();
        this.familyRegForm.reset();
      } else {
        console.error('registration family member got failed');
      }
    });
  }

  getFamily(userId:number){
    this.familyDetailsService.getFamilyDetails(userId).subscribe((response:any) => {
        this.family=response;
    },
  );
  }
  
    updateFamily(memberId:number): void{
    let familyDto: FamilyDto = new FamilyDto(this.familyRegForm.value);
      this.familyDetailsService.updateFamilyDetails(memberId,familyDto).subscribe((response: any) =>{
        if (response.success) {
          console.log(response);
          this.getFamily(this.userId);
          window.alert('Family Member Successfully Updated');
          
          this.closeAddFamilyMember();
        } 
      });
      this.editingIndex = -1;
    }
    getFamilyMember(memberId:number){
      this.familyDetailsService.getFamilyMember(memberId).subscribe((response:any)=>{
        this.selectedFamilyMember=response;
      });
      
    }
  
  hasError(controlName: string, errorName: string): boolean {
    const control = this.familyRegForm.get(controlName);
    return control ?
      control.hasError(errorName) &&
      control.touched : false;
  }

  openAddFamilyMember(){
    console.log("open popup");
    document.getElementById("familyModal")!.style.display = "block";
    document.getElementById("addFamilymodal")!.style.display = "block";
    document.getElementById("editFamilymodal")!.style.display = "none";
    this.familyRegForm.reset();

  }
  
  editFamilymodal(id: number){
    console.log("open popup");
    document.getElementById("familyModal")!.style.display = "block";
    document.getElementById("addFamilymodal")!.style.display = "none";
    document.getElementById("editFamilymodal")!.style.display = "block";
    this.getFamilyMember(id);
  }

  closeAddFamilyMember(){
    console.log("close popup");
    document.getElementById("familyModal")!.style.display = "none";
    
  }

  editMember(index: number):void{
    this.editingIndex=index;
  }

}
