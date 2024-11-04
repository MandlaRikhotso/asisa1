import { Component, OnInit, OnDestroy, AfterContentChecked } from '@angular/core';
import { Subscription } from 'rxjs';

import { AlertService } from '../../_services';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit, OnDestroy, AfterContentChecked {
  private subscription: Subscription;
  message: any;
  alerts: any = [];
  constructor(private alertService: AlertService) { }

  ngOnInit() {
    this.subscription = this.alertService.getMessage().subscribe(message => {
      this.message = message;
      setTimeout(() => {
        this.message = '';
        localStorage.removeItem('successMsg');
      }, 5000);
    });
  }

  removeAlert() {
    this.message = '';
      localStorage.removeItem('successMsg');
  }
  ngOnDestroy() {
    // this.subscription.unsubscribe();
  }

  ngAfterContentChecked() {
    let top = document.getElementById('alertTop');
    if (top !== null) {
      top.scrollIntoView();
      top = null;
    }
  }
}
