import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../domains/user';
import { PasswordChange } from '../domains/password-change';
import { ServerConstant } from '../contants/serverConstant';
import {JwtHelperService} from '@auth0/angular-jwt';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  constant: ServerConstant = new ServerConstant();
  public host: string = this.constant.host;
  public token: string;
  public logginUsername: string | null;
  public redirectUrl: string;
  private googleMapsAPIKey = 'key';
  private googleMapsAPIUrl = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=';
  private jwtHelper = new JwtHelperService();

  constructor() { }
}
