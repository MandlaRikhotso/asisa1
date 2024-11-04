import { Component, OnInit } from '@angular/core';
import { AlertService } from '../_services';

@Component({
  selector: 'app-not-found',
  templateUrl: './not-found.component.html',
  styleUrls: ['./not-found.component.css']
})
export class NotFoundComponent implements OnInit {

  constructor( private alertService: AlertService
    ) { }
  successStatus:any;
  ngOnInit() {
    if (localStorage.getItem('successMsg')) {
      this.successStatus = localStorage.getItem('successMsg');
      this.alertService.error(this.successStatus, true);

    }
  }

}
