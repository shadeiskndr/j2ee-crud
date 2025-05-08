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

  constructor(private auth: AuthService, public router: Router) {}

  register() {
    this.error = "";
    this.success = "";
    if (this.password !== this.confirmPassword) {
      this.error = "Passwords do not match";
      return;
    }
    this.auth
      .register({
        username: this.username,
        email: this.email,
        password: this.password,
      })
      .subscribe({
        next: () => {
          this.success = "Registration successful! Redirecting to login...";
          setTimeout(() => this.router.navigate(["/login"]), 1500);
        },
        error: (err) =>
          (this.error = err.error?.message || "Registration failed"),
      });
  }

  goToLogin(event: Event) {
    event.preventDefault();
    this.router.navigate(["/login"]);
  }
}
