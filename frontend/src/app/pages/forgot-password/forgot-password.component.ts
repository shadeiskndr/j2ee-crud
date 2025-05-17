import { Component } from "@angular/core";
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
  selector: "app-forgot-password",
  standalone: true,
  templateUrl: "./forgot-password.component.html",
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
export class ForgotPasswordComponent {
  email = "";
  error = "";
  success = "";
  isLoading = false;

  constructor(
    private passwordResetService: PasswordResetService,
    private passwordResetStateService: PasswordResetStateService,
    public router: Router
  ) {
    // Reset the state when component loads
    this.passwordResetStateService.resetState();
  }

  sendResetCode() {
    this.error = "";
    this.success = "";
    this.isLoading = true;

    this.passwordResetService.initiatePasswordReset(this.email).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.success = response.message;
        // Update state with email
        this.passwordResetStateService.setEmail(this.email);
        // Navigate to verify code page after 2 seconds
        setTimeout(() => {
          this.router.navigate(["/verify-code"]);
        }, 2000);
      },
      error: (err) => {
        this.isLoading = false;
        this.error =
          err.error?.error || err.error?.message || "Failed to send reset code";
      },
    });
  }

  goToLogin(event: Event) {
    event.preventDefault();
    this.passwordResetStateService.resetState();
    this.router.navigate(["/login"]);
  }
}
