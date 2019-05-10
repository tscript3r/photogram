import {Component, OnInit, OnDestroy} from '@angular/core';
import { Subscription } from 'rxjs';
import { Alert } from './domains/alert';
import { LoadingService } from './services/loading.service';
import { AlertService } from './services/alert.service';
import { unsupported } from '@angular/compiler/src/render3/view/util';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'Photogram';

  private subscriptions: Subscription[] = [];
  public alerts: Alert[] = [];
  public loading: boolean = false;

  constructor(private loadingService: LoadingService, private alertService: AlertService) {
  }

  ngOnInit() {

    this.subscriptions.push(
      this.loadingService.isLoading.subscribe(isLoading => {
        this.loading = isLoading;
      })
    );

    this.subscriptions.push(
      this.alertService.alerts.subscribe(alert => {
        this.alerts.push(alert);
        this.closeAlert(3);
      })
    );

  }

  closeAlert(second: number): void {
    setTimeout(() => {
      let element: HTMLElement = document.getElementById('dismissAlert') as HTMLElement;
      element.click();
    }, second * 1000);
  }

  ngOnDestroy() {
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }

}
