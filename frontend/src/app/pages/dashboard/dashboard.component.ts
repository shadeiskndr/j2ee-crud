import { Component } from "@angular/core";
import { StatsWidget } from "../../components/dashboard/statswidget";
import { SalesTrendWidget } from "../../components/dashboard/salestrendwidget";
import { RecentActivityWidget } from "../../components/dashboard/recentactivitywidget";
import { ProductOverviewWidget } from "../../components/dashboard/productoverviewwidget";

@Component({
  selector: "app-dashboard",
  standalone: true,
  imports: [
    StatsWidget,
    SalesTrendWidget,
    RecentActivityWidget,
    ProductOverviewWidget,
  ],
  templateUrl: "./dashboard.component.html",
})
export class DashboardComponent {}
