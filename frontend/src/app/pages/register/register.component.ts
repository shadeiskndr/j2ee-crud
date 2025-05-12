import { Component } from "@angular/core";
import { CommonModule } from "@angular/common";
import { InputIconModule } from "primeng/inputicon";
import { IconFieldModule } from "primeng/iconfield";
import { InputTextModule } from "primeng/inputtext";
import { IftaLabelModule } from "primeng/iftalabel";
import { PasswordModule } from "primeng/password";
import { FormsModule } from "@angular/forms";
import { ButtonModule } from "primeng/button";
import { Router } from "@angular/router";
import { AuthService } from "../../service/auth.service";

@Component({
  selector: "app-register",
  standalone: true,
  templateUrl: "./register.component.html",
  imports: [
    CommonModule,
    InputIconModule,
    IconFieldModule,
    InputTextModule,
    IftaLabelModule,
    PasswordModule,
    FormsModule,
    ButtonModule,
  ],
})
export class RegisterComponent {
  username = "";
  email = "";
  password = "";
  confirmPassword = "";
  error = "";
  success = "";
  isLoading = false;

  // Password visibility toggles
  passwordVisible = false;
  confirmPasswordVisible = false;

  // Validation errors
  usernameError = "";
  emailError = "";

  constructor(private auth: AuthService, public router: Router) {}

  validateUsername() {
    // Username: required, 3-20 chars, alphanumeric/underscore only
    if (!this.username) {
      this.usernameError = "Username is required";
    } else if (!/^[a-zA-Z0-9_]{3,20}$/.test(this.username)) {
      this.usernameError =
        "Username must be 3-20 characters, letters, numbers, or underscores";
    } else {
      this.usernameError = "";
    }
  }

  validateEmail() {
    // Email: required, valid format
    if (!this.email) {
      this.emailError = "Email is required";
    } else if (
      !/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/.test(this.email)
    ) {
      this.emailError = "Invalid email format";
    } else {
      this.emailError = "";
    }
  }

  register() {
    this.error = "";
    this.success = "";
    this.validateUsername();
    this.validateEmail();
    if (this.usernameError || this.emailError) {
      return;
    }
    if (this.password !== this.confirmPassword) {
      this.error = "Passwords do not match";
      return;
    }

    this.isLoading = true;

    this.auth
      .register({
        username: this.username,
        email: this.email,
        password: this.password,
      })
      .subscribe({
        next: () => {
          this.isLoading = false;
          this.success = "Registration successful! Redirecting to login...";
          setTimeout(() => this.router.navigate(["/login"]), 1500);
        },
        error: (err) => {
          this.isLoading = false;
          this.error =
            err.error?.error || err.error?.message || "Registration failed";
        },
      });
  }

  goToLogin(event: Event) {
    event.preventDefault();
    this.router.navigate(["/login"]);
  }
}
