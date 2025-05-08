import { Component } from "@angular/core";
import { RouterModule } from "@angular/router";
import { AppTopbar } from "./components/app.topbar";
import { AppFooter } from "./components/app.footer";

@Component({
  selector: "app-root",
  imports: [RouterModule, AppTopbar, AppFooter],
  templateUrl: "./app.component.html",
  styleUrl: "./app.component.scss",
})
export class AppComponent {}
