import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../domains/user';
import { PasswordChange } from '../domains/password-change';
import { ServerConstant } from '../contants/serverConstant';
import { JwtHelperService } from '@auth0/angular-jwt';

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

  constructor(private http: HttpClient) { }

  login(user: User): Observable<HttpErrorResponse | HttpResponse<any>> {
    return this.http.post<HttpErrorResponse | HttpResponse<any>>(`${this.host}/users/login`, user, { observe: `response` });
  }

  register(user: User): Observable<User | HttpErrorResponse> {
    return this.http.post<User>(`${this.host}/users`, user);
  }

  resetPassword(email: String) {
    return this.http.get(`${this.host}/users/reset?email=${email}`);
  }

  logout(): void {
    this.token = null;
    localStorage.removeItem('token');
  }

  saveToken(token: string): void {
    this.token = token;
    localStorage.setItem('token', token);
  }

  loadToken(): void {
    this.token = localStorage.getItem('token');
  }

  getToken(): string {
    return this.token;
  }

  isLoggedIn(): boolean {
    this.loadToken();
    if(this.token != null && this.token != '') {
      var decodedToken = this.jwtHelper.decodeToken(this.token);
      if(decodedToken.sub != null || '') 
        if( !this.jwtHelper.isTokenExpired(this.token)) {
          this.logginUsername = decodedToken.sub;
          return true;
        }
    }
    this.logout();
    return false;
  }

  getUserDetails(username: string): Observable<User> {
    return this.http.get<User>(`${this.host}/users/find?username=${username}`);
  }

  getLocation(latitude: string, longitude: string): Observable<any> {
    return this.http.get<any>(`${this.googleMapsAPIUrl}${latitude},${longitude}&key=${this.googleMapsAPIKey}`);
  }

  update(user: User): Observable<User> {
    return this.http.put<User>(`${this.host}/users`, user);
  }

}
