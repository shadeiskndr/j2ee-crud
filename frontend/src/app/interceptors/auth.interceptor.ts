import { inject } from "@angular/core";
import { HttpInterceptorFn } from "@angular/common/http";
import { AuthService } from "../service/auth.service";
import { Router } from "@angular/router";
import { catchError } from "rxjs/operators";
import { throwError } from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.getToken();
  let cloned = req;
  if (token) {
    cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` },
    });
  }

  return next(cloned).pipe(
    catchError((err) => {
      if (err.status === 401) {
        authService.logout();
        router.navigate(["/login"]);
      }
      return throwError(() => err);
    })
  );
};
