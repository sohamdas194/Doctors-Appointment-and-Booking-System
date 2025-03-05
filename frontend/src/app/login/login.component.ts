import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from '../service/authentication.service';
import { Router } from '@angular/router';
import { UserCredentials } from '../model/user-credentials';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  errorMessage: string = '';
  errorState!: boolean;

  loginForm!: FormGroup

  formSubmitted: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthenticationService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      userEmail: ['', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]],
      userPassword: ['', Validators.required]
    })
  }

  // get loginFormControl() {
  //   return this.loginForm.controls;
  // }

  handleLogin(): void {
    this.formSubmitted = true;
    if(this.loginForm.invalid) {
      return;
    }

    let loginFormValue = this.loginForm.value;

    let userCreds: UserCredentials = new UserCredentials(
      loginFormValue.userEmail,
      loginFormValue.userPassword
    );

    this.authService.executeAuthenticationService(userCreds)
      .subscribe(
        authenticatedUser => {
          this.errorState = false;
          switch(authenticatedUser.userRole.toLowerCase()) {
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
        },
        error => {
          //console.log(error);
          this.errorState = true;
          this.errorMessage = error.error.message;
        }
      )
    } 

}
