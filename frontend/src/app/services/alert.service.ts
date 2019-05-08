import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Alert } from '../domains/alert';
import { AlertType } from '../enums/alert-type.enum';

@Injectable({
  providedIn: 'root'
})
export class AlertService {

  public alerts: Subject<Alert>;

  constructor() { }

  showAlert(message: string, alertType: AlertType): void {
    const alert = new Alert(message, alertType);
    this.alerts.next(alert);
  }

}
