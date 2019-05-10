import { Injectable, Inject } from '@angular/core';
import {
    HttpEvent,
    HttpInterceptor,
    HttpHandler,
    HttpRequest,
    HttpResponse
} from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { tap } from 'rxjs/operators';
import { ServerConstant } from '../contants/serverConstant';
import { CacheService } from '../services/cache.service';
import { AccountService } from '../services/account.service';

@Injectable()
export class CacheInterceptor implements HttpInterceptor {

    private host = new ServerConstant().host;

    constructor(private accountService: AccountService, private cacheService: CacheService) {}

    private isGetMethod(request: HttpRequest<any>): boolean | false {
        if(request.method == 'GET')
            return true;
    }

    private isIgnoredUrl(url: string): boolean | false {
        if(url.includes(`${this.accountService.host}/users/login`) || url.includes(`${this.accountService.host}/users/find`))
            return true;
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if( !this.isGetMethod(request)) {
            this.cacheService.clear();
            return next.handle(request);
        }

        if( this.isIgnoredUrl(request.url))
            return next.handle(request);

        const cachedResponse = this.cacheService.getCache(request.url);

        if(cachedResponse) 
            return of (cachedResponse)
        
        return next.handle(request).pipe(tap(event => {
                if(event instanceof HttpResponse)
                    this.cacheService.cacheRequest(request.url, event);
               }));
    }

}