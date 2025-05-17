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
  selector: "app-reset-password",
  standalone: true,
  templateUrl: "./reset-password.component.html",
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
export class ResetPasswordComponent implements OnInit {
  newPassword = "";
  confirmPassword = "";
  error = "";
  success = "";
  isLoading = false;
  resetToken = "";

  passwordVisible = false;
  confirmPasswordVisible = false;

  constructor(
    private passwordResetService: PasswordResetService,
    private passwordResetStateService: PasswordResetStateService,
    public router: Router
  ) {}

  ngOnInit() {
    const state = this.passwordResetStateService.getCurrentState();
    if (state.step !== "reset" || !state.resetToken) {
      // If not in correct step or no reset token, redirect to forgot password
      this.router.navigate(["/forgot-password"]);
      return;
    }
    this.resetToken = state.resetToken;
  }

  resetPassword() {
    this.error = "";
    this.success = "";

    // Validate passwords match
    if (this.newPassword !== this.confirmPassword) {
      this.error = "Passwords do not match";
      return;
    }

    // Validate password length
    if (this.newPassword.length < 6) {
      this.error = "Password must be at least 6 characters long";
      return;
    }

    this.isLoading = true;

    this.passwordResetService
      .resetPassword(this.resetToken, this.newPassword)
      .subscribe({
        next: (response) => {
          this.isLoading = false;
          this.success = response.message;
          // Complete the reset process
          this.passwordResetStateService.completeReset();
          // Navigate to login page after 2 seconds
          setTimeout(() => {
            this.router.navigate(["/login"]);
          }, 2000);
        },
        error: (err) => {
          this.isLoading = false;
          this.error =
            err.error?.error ||
            err.error?.message ||
            "Failed to reset password";
        },
      });
  }

  goToLogin(event: Event) {
    event.preventDefault();
    this.passwordResetStateService.resetState();
    this.router.navigate(["/login"]);
  }
}
