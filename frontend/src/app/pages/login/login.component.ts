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
  selector: "app-login",
  standalone: true,
  templateUrl: "./login.component.html",
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
export class LoginComponent {
  username = "";
  password = "";
  error = "";

  passwordVisible = false;

  constructor(private auth: AuthService, public router: Router) {}

  login() {
    this.error = "";
    this.auth.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(["/dashboard"]),
      error: (err) =>
        (this.error = err.error?.error || err.error?.message || "Login failed"),
    });
  }

  goToRegister(event: Event) {
    event.preventDefault();
    this.router.navigate(["/register"]);
  }
}
