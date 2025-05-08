import {
  Component,
  inject,
  effect,
  computed,
  ViewChild,
  AfterViewInit,
  OnDestroy,
} from "@angular/core";
import { MenubarModule } from "primeng/menubar";
import { MenuItem } from "primeng/api";
import { Router, RouterModule } from "@angular/router";
import { CommonModule } from "@angular/common";
import { AuthService } from "../service/auth.service";
import { ButtonModule } from "primeng/button";
import { StyleClassModule } from "primeng/styleclass";
import { AppConfig } from "./app.config";
import { LayoutService } from "../service/layout.service";
import { DrawerModule, Drawer } from "primeng/drawer";
import { AvatarModule } from "primeng/avatar";
import { RippleModule } from "primeng/ripple";

@Component({
  selector: "app-topbar",
  standalone: true,
  imports: [
    CommonModule,
    MenubarModule,
    ButtonModule,
    StyleClassModule,
    AppConfig,
    RouterModule,
    DrawerModule,
    AvatarModule,
    RippleModule,
  ],
  template: `
    <div
      class="bg-surface-0 dark:bg-surface-900 p-6 rounded-2xl max-w-7xl mx-auto border border-surface-200 dark:border-surface-700 w-full flex items-center justify-between"
    >
      <!-- Left: Logo & Title -->
      <div class="flex gap-3 items-center flex-shrink-0">
        <img
          src="assets/logo.png"
          width="31"
          height="36"
          alt="Logo"
          class="block mx-auto w-8 h-9"
        />
        <span class="hidden sm:flex flex-col">
          <span
            class="text-xl font-light text-surface-700 dark:text-surface-100 leading-none mb-0.5 ml-1"
          >
            Lonpac Insurance
          </span>
          <span class="text-sm font-medium text-primary leading-tight ml-1">
            Membership Management
          </span>
        </span>
      </div>
      <!-- Center: Navigation -->
      <div class="flex-1 flex justify-center">
        <!-- Hamburger for small screens -->
        <button
          *ngIf="isSmallScreen && isLoggedIn()"
          type="button"
          class="cursor-pointer w-10 h-10 flex items-center justify-center rounded-full hover:bg-surface-100 dark:hover:bg-surface-800 transition-all text-surface-900 dark:text-surface-0"
          (click)="visible = true"
          aria-label="Open menu"
        >
          <i class="pi pi-bars text-base"></i>
        </button>
        <!-- Menubar for large screens -->
        <p-menubar
          *ngIf="!isSmallScreen && isLoggedIn()"
          [model]="items"
          class="border-none shadow-none bg-transparent w-full max-w-lg px-8 mr-14"
        ></p-menubar>
      </div>
      <!-- Right: Controls -->
      <div class="flex items-center gap-2 flex-shrink-0">
        <button
          type="button"
          class="cursor-pointer w-10 h-10 flex items-center justify-center rounded-full hover:bg-surface-100 dark:hover:bg-surface-800 transition-all text-surface-900 dark:text-surface-0 focus-visible:outline-hidden focus-visible:ring-1 focus-visible:ring-primary focus-visible:ring-offset-2 focus-visible:ring-offset-surface-0 dark:focus-visible:ring-offset-surface-950"
          (click)="toggleDarkMode()"
        >
          <i
            class="pi text-base"
            [ngClass]="{
              'pi-moon': isDarkMode(),
              'pi-sun': !isDarkMode()
            }"
          ></i>
        </button>
        <div class="relative">
          <p-button
            pStyleClass="@next"
            enterFromClass="hidden"
            enterActiveClass="animate-scalein"
            leaveToClass="hidden"
            leaveActiveClass="animate-fadeout"
            [hideOnOutsideClick]="true"
            icon="pi pi-cog"
            text
            rounded
            aria-label="Settings"
          />
          <app-config class="hidden" />
        </div>
        <button
          *ngIf="isLoggedIn()"
          type="button"
          class="cursor-pointer w-10 h-10 flex items-center justify-center rounded-full hover:bg-red-100 dark:hover:bg-red-800 transition-all text-red-600 dark:text-red-300 focus-visible:outline-hidden focus-visible:ring-1 focus-visible:ring-red-500 focus-visible:ring-offset-2 focus-visible:ring-offset-surface-0 dark:focus-visible:ring-offset-surface-950"
          (click)="logout()"
          aria-label="Logout"
          title="Logout"
        >
          <i class="pi pi-sign-out text-base"></i>
        </button>
      </div>
    </div>

    <!-- Drawer for small screens -->
    <p-drawer
      #drawerRef
      [(visible)]="visible"
      [modal]="true"
      position="left"
      [style]="{ width: '300px' }"
    >
      <ng-template #headless>
        <div class="flex flex-col h-full">
          <div class="flex items-center justify-between px-6 pt-4 shrink-0">
            <span class="inline-flex items-center gap-2">
              <img
                src="assets/logo.png"
                width="31"
                height="36"
                alt="Logo"
                class="block mx-auto w-8 h-9"
              />
              <span class="font-semibold text-2xl text-primary"
                >Lonpac Insurance</span
              >
            </span>
            <span>
              <p-button
                type="button"
                (click)="closeDrawer($event)"
                icon="pi pi-times"
                rounded="true"
                outlined="true"
                styleClass="h-8 w-8"
              ></p-button>
            </span>
          </div>
          <div class="overflow-y-auto flex-1">
            <ul class="list-none p-4 m-0">
              <li *ngFor="let item of items">
                <!-- If item has subitems, render collapsible section -->
                <ng-container
                  *ngIf="item.items && item.items.length > 0; else singleLink"
                >
                  <div
                    pRipple
                    pStyleClass="@next"
                    enterFromClass="hidden"
                    enterActiveClass="animate-slidedown"
                    leaveToClass="hidden"
                    leaveActiveClass="animate-slideup"
                    class="p-4 flex items-center justify-between text-surface-600 dark:text-surface-200 cursor-pointer p-ripple"
                    (click)="toggleSection(item)"
                  >
                    <span class="font-medium flex items-center gap-2">
                      <i *ngIf="item.icon" class="pi {{ item.icon }}"></i>
                      {{ item.label }}
                    </span>
                    <i
                      class="pi"
                      [ngClass]="{
                        'pi-chevron-down': !isSectionOpen(item),
                        'pi-chevron-up': isSectionOpen(item)
                      }"
                    ></i>
                  </div>
                  <ul
                    class="list-none p-0 m-0 overflow-hidden transition-all duration-[400ms] ease-in-out"
                    [ngClass]="{ hidden: !isSectionOpen(item) }"
                  >
                    <li *ngFor="let sub of item.items">
                      <a
                        pRipple
                        class="flex items-center cursor-pointer p-4 rounded-border text-surface-700 dark:text-surface-100 hover:bg-surface-100 dark:hover:bg-surface-700 duration-150 transition-colors p-ripple"
                        (click)="navigate(sub.routerLink)"
                      >
                        <i *ngIf="sub.icon" class="pi {{ sub.icon }} mr-2"></i>
                        <span class="font-medium">{{ sub.label }}</span>
                      </a>
                    </li>
                  </ul>
                </ng-container>
                <ng-template #singleLink>
                  <a
                    pRipple
                    class="flex items-center cursor-pointer p-4 rounded-border text-surface-700 dark:text-surface-100 hover:bg-surface-100 dark:hover:bg-surface-700 duration-150 transition-colors p-ripple"
                    (click)="navigate(item.routerLink)"
                  >
                    <i *ngIf="item.icon" class="pi {{ item.icon }} mr-2"></i>
                    <span class="font-medium">{{ item.label }}</span>
                  </a>
                </ng-template>
              </li>
            </ul>
          </div>
        </div>
      </ng-template>
    </p-drawer>
  `,
})
export class AppTopbar implements AfterViewInit, OnDestroy {
  layoutService: LayoutService = inject(LayoutService);
  router: Router = inject(Router);
  authService = inject(AuthService);

  isLoggedIn = this.authService.loggedIn;
  isDarkMode = computed(() => this.layoutService.appState().darkMode);

  items: MenuItem[] = [];
  visible: boolean = false;
  isSmallScreen: boolean = false;

  @ViewChild("drawerRef") drawerRef!: Drawer;

  private mediaQueryList: MediaQueryList;
  private mediaQueryListener: () => void;

  // Track open/closed state for collapsible sections
  openSections = new Set<string>();

  constructor() {
    effect(() => {
      this.items = this.isLoggedIn()
        ? [
            {
              label: "Dashboard",
              icon: "pi pi-home",
              routerLink: "/dashboard",
            },
            {
              label: "Members",
              icon: "pi pi-users",
              items: [
                {
                  label: "Add Member",
                  icon: "pi pi-user-plus",
                  routerLink: "/members/add",
                },
                {
                  label: "List Members",
                  icon: "pi pi-list",
                  routerLink: "/members",
                },
              ],
            },
          ]
        : [];
    });

    this.mediaQueryList = window.matchMedia("(max-width: 900px)");
    this.mediaQueryListener = () => {
      this.isSmallScreen = this.mediaQueryList.matches;
    };
    this.mediaQueryList.addEventListener("change", this.mediaQueryListener);
    this.isSmallScreen = this.mediaQueryList.matches;
  }

  ngAfterViewInit() {}

  ngOnDestroy() {
    this.mediaQueryList.removeEventListener("change", this.mediaQueryListener);
  }

  toggleDarkMode() {
    this.layoutService.appState.update((state) => ({
      ...state,
      darkMode: !state.darkMode,
    }));
  }

  logout() {
    this.authService.logout();
    this.router.navigate(["/login"]);
  }

  closeDrawer(e: Event) {
    if (this.drawerRef) {
      this.drawerRef.close(e);
    }
  }

  navigate(route: string | undefined) {
    if (route) {
      this.visible = false;
      this.router.navigate([route]);
    }
  }

  // Collapsible section helpers
  toggleSection(item: MenuItem) {
    if (this.openSections.has(item.label!)) {
      this.openSections.delete(item.label!);
    } else {
      this.openSections.add(item.label!);
    }
  }
  isSectionOpen(item: MenuItem): boolean {
    return this.openSections.has(item.label!);
  }
}
