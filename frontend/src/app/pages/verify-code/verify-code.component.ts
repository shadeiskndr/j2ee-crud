import { Component, OnInit } from "@angular/core";
import { CommonModule } from "@angular/common";
import { InputIconModule } from "primeng/inputicon";
import { IconFieldModule } from "primeng/iconfield";
import { InputTextModule } from "primeng/inputtext";
import { IftaLabelModule } from "primeng/iftalabel";
import { FormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { Router } from "@angular/router";
import { PasswordResetService } from "../../service/password-reset.service";
import { PasswordResetStateService } from "../../service/password-reset-state.service";
import { MessageModule } from "primeng/message";

@Component({
  selector: "app-verify-code",
  standalone: true,
  templateUrl: "./verify-code.component.html",
  imports: [
    CommonModule,
    InputIconModule,
    IconFieldModule,
    InputTextModule,
    IftaLabelModule,
    FormsModule,
    ButtonModule,
    MessageModule,
  ],
})
export class VerifyCodeComponent implements OnInit {
  verificationCode = "";
  error = "";
  success = "";
  isLoading = false;
  email = "";

  constructor(
    private passwordResetService: PasswordResetService,
    private passwordResetStateService: PasswordResetStateService,
    public router: Router
  ) {}

  ngOnInit() {
    const state = this.passwordResetStateService.getCurrentState();
    if (state.step !== "verify" || !state.email) {
      // If not in correct step or no email, redirect to forgot password
      this.router.navigate(["/forgot-password"]);
      return;
    }
    this.email = state.email;
  }

  verifyCode() {
    this.error = "";
    this.success = "";
    this.isLoading = true;

    this.passwordResetService.verifyCode(this.verificationCode).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.success = response.message;
        // Update state with reset token
        this.passwordResetStateService.setResetToken(response.resetToken);
        // Navigate to reset password page after 1 second
        setTimeout(() => {
          this.router.navigate(["/reset-password"]);
        }, 1000);
      },
      error: (err) => {
        this.isLoading = false;
        this.error =
          err.error?.error || err.error?.message || "Invalid verification code";
      },
    });
  }

  resendCode() {
    this.error = "";
    this.success = "";
    this.isLoading = true;

    this.passwordResetService.initiatePasswordReset(this.email).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.success = "Verification code resent successfully!";
      },
      error: (err) => {
        this.isLoading = false;
        this.error =
          err.error?.error || err.error?.message || "Failed to resend code";
      },
    });
  }

  goToForgotPassword(event: Event) {
    event.preventDefault();
    this.passwordResetStateService.resetState();
    this.router.navigate(["/forgot-password"]);
  }

  goToLogin(event: Event) {
    event.preventDefault();
    this.passwordResetStateService.resetState();
    this.router.navigate(["/login"]);
  }
}
