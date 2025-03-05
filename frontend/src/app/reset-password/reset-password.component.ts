import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PasswordResetService } from '../service/password-reset.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomValidationService } from '../service/custom-validation.service';
import { ResetPasswordReq } from '../model/reset-password-req';
import { PasswordResetTokenValidityRes } from '../model/password-reset-token-check-validity';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  resetPasswordForm!: FormGroup;

  formSubmitted: boolean = false;

  userEmail: string = 'test';

  errorState!: boolean;
  errorMessage!: string;

  successState!: boolean;
  successMessage: string = 'Password changed!!';

  passwordResetToken!: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private formBuilder: FormBuilder,
    private passwordResetService: PasswordResetService,
    private customValidator: CustomValidationService
  ) { }

  ngOnInit(): void {
    this.passwordResetToken = this.route.snapshot.params['token'];

    this.passwordResetService.validatePasswordResetToken(this.passwordResetToken)
      .subscribe(
        passwordResetTokenValidityRes => {
          if(passwordResetTokenValidityRes.valid == false) {
            this.router.navigate(['login']);
          }
        },
        error => {
          this.router.navigate(['login']);
        }
      );

    this.resetPasswordForm = this.formBuilder.group({
      newPassword: ['', Validators.compose([Validators.required, this.customValidator.patternValidator()])],
      confirmPassword: ['', [Validators.required]]
    }, {
      validator: this.customValidator.matchPassword('newPassword', 'confirmPassword')
    })
  }

  resetPassword():void {
    this.formSubmitted = true;
    if(this.resetPasswordForm.invalid) {
      return;
    }

    let resetPasswordFormValue = this.resetPasswordForm.value;

    let resetPasswordReq = new ResetPasswordReq(this.passwordResetToken, resetPasswordFormValue.newPassword);
  

    this.passwordResetService.executeResetPasswordService(resetPasswordReq)
      .subscribe(
        response => {
          this.errorState = false;
          this.successState = true;

          setTimeout(() => 
            {
                this.router.navigate(['login']);
            },
          700);

        },
        error => {
          this.errorState = true;
          this.errorMessage = error.error.errorMessage;

          setTimeout(() => 
            {
                this.router.navigate(['login']);
            },
          700);
        }
      );
  }

}
