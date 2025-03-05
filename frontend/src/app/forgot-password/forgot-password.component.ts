import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PasswordResetService } from '../service/password-reset.service';
import { Router } from '@angular/router';
import { ForgotPasswordBody } from '../model/forgot-password-body';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.css']
})
export class ForgotPasswordComponent implements OnInit {

  forgotPasswordForm!: FormGroup;

  formSubmitted: boolean = false;

  errorState!: boolean;
  errorMessage!: string;

  successState!: boolean;
  successMessage!: string;

  infoState!: boolean;
  infoMessage: string = 'Sending Reset Link';

  constructor(
    private router: Router,
    private formBuilder: FormBuilder,
    private passwordResetService: PasswordResetService
  ) { }

  ngOnInit(): void {
    this.forgotPasswordForm = this.formBuilder.group({
      userEmail: ['', [Validators.required, Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$")]]
    })
  }

  sendPasswordResetLink(): void {
    this.formSubmitted = true;
    if(this.forgotPasswordForm.invalid) {
      return;
    }

    let forgotPasswordFormValue = this.forgotPasswordForm.value;

    this.errorState = false;
    this.infoState = true;

    let forgotPasswordReq: ForgotPasswordBody = new ForgotPasswordBody(
      forgotPasswordFormValue.userEmail
    );

    this.passwordResetService.executeForgotPasswordService(forgotPasswordReq)
      .subscribe(
        forgotPasswordRes => {
          this.infoState = false;
          this.successState = true;
          this.successMessage = 'Link sent to ' + forgotPasswordRes.userEmail;
          
          setTimeout(() => 
            {
                this.router.navigate(['login']);
            },
          700);

        },
        error => {
          // console.log(error);
          this.infoState = false
          this.errorState = true;
          this.successState = false;
          this.errorMessage = error.error.message;
          this.formSubmitted = false;
        }
      );
  }

}
