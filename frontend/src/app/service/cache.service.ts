import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class CacheService {

  constructor() {}

  private request: any = {};

  cacheRequest(requestUrl: string, response: HttpResponse<any>): void {
    this.request[requestUrl] = response;
  }

  getCache(requestUrl: string): HttpResponse<any> | null {
    return this.request[requestUrl];
  }

  invalidateCache(requestUrl: string): void {
    this.request[requestUrl] = null;
  }

  clearCache(): void {
    this.request = {};
  }
}
