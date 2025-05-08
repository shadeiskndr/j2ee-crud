import { Routes } from "@angular/router";
import { authGuard } from "./guards/auth.guard";

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
  },
  {
    path: "register",
    loadComponent: () =>
      import("./pages/register/register.component").then(
        (m) => m.RegisterComponent
      ),
  },
  { path: "**", redirectTo: "dashboard" },
];
