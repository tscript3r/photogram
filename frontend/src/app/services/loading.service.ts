import { Injectable, OnInit, OnDestroy } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoadingService {

  public isLoading: Subject<boolean> = new Subject();

  constructor() { }


}
