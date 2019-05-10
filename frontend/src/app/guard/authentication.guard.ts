import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AccountService } from '../services/account.service';
import { AlertService } from '../services/alert.service';
import { AlertType } from '../enums/alert-type.enum';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationGuard implements CanActivate {

  constructor(private accountService: AccountService, private alertService: AlertService, private router: Router) {
  } 

  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
    return this.isLoggedIn(state.url);
  }

  private isLoggedIn(url: string): boolean {
    if (this.accountService.isLoggedIn) 
      return true;
  
    this.accountService.redirectUrl = url;
    this.router.navigate(['/login']);
    this.alertService.showAlert('Login in order to access this page', AlertType.DANGER);
    return false;
  }
  
}
