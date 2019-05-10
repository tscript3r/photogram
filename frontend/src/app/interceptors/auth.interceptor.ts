import { Injectable, Inject } from '@angular/core';
import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { AccountService } from '../services/account.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

    constructor(private accountService: AccountService) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        var url = request.url;
        if(url.includes('https://maps.googleapis.com/')) 
            return next.handle(request);
        if(url.includes(`${this.accountService.host}/users/login`) || url.includes(`${this.accountService.host}/users`))
            return next.handle(request);
        this.accountService.loadToken();
        const token = this.accountService.getToken();
        const requestWithToken = request.clone({ setHeaders: { Authorization: token }});
        return next.handle(requestWithToken);
    }

}