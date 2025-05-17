import { Routes } from "@angular/router";
import { authGuard } from "./guards/auth.guard";
import { loggedInGuard } from "./guards/logged-in.guard";

export const routes: Routes = [
  { path: "", redirectTo: "dashboard", pathMatch: "full" },
  {
    path: "dashboard",
    loadComponent: () =>
      import("./pages/dashboard/dashboard.component").then(
        (m) => m.DashboardComponent
      ),
    canActivate: [authGuard],
  },
  {
    path: "members",
    loadComponent: () =>
      import("./pages/members/members-list.component").then(
        (m) => m.MembersListComponent
      ),
    canActivate: [authGuard],
  },
  {
    path: "members/add",
    loadComponent: () =>
      import("./pages/members/member-form.component").then(
        (m) => m.MemberFormComponent
      ),
    canActivate: [authGuard],
  },
  {
    path: "members/edit/:id",
    loadComponent: () =>
      import("./pages/members/member-form.component").then(
        (m) => m.MemberFormComponent
      ),
    canActivate: [authGuard],
  },
  {
    path: "login",
    loadComponent: () =>
      import("./pages/login/login.component").then((m) => m.LoginComponent),
    canActivate: [loggedInGuard],
  },
  {
    path: "register",
    loadComponent: () =>
      import("./pages/register/register.component").then(
        (m) => m.RegisterComponent
      ),
    canActivate: [loggedInGuard],
  },
  {
    path: "forgot-password",
    loadComponent: () =>
      import("./pages/forgot-password/forgot-password.component").then(
        (m) => m.ForgotPasswordComponent
      ),
    canActivate: [loggedInGuard],
  },
  {
    path: "verify-code",
    loadComponent: () =>
      import("./pages/verify-code/verify-code.component").then(
        (m) => m.VerifyCodeComponent
      ),
    canActivate: [loggedInGuard],
  },
  {
    path: "reset-password",
    loadComponent: () =>
      import("./pages/reset-password/reset-password.component").then(
        (m) => m.ResetPasswordComponent
      ),
    canActivate: [loggedInGuard],
  },
  { path: "**", redirectTo: "dashboard" },
];
