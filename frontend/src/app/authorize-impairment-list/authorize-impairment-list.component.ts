import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthoriseService, AlertService, AuthenticationService } from '../_services';
import { Router } from '@angular/router';
import { Utils } from '../utils';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-authorize-impairment-list',
  templateUrl: './authorize-impairment-list.component.html',
  styleUrls: ['./authorize-impairment-list.component.css']
})
export class AuthorizeImpairmentListComponent implements OnInit {
  impairments: any = [];
  private subscription: Subscription;
  count: any;
  successStatus: any;
  referImpairmentData: any;
  objCurrentUser: any;
  message: any;
  boolAuthorizeImp: boolean = false;
  boolAuthorizeClm: boolean = false;
  constructor(
    private authoriseService: AuthoriseService,
    private alertService: AlertService,
    private _authenticationService: AuthenticationService,
    private spinner: NgxSpinnerService,
    private _router: Router,
  ) {
    this.boolAuthorizeImp = Utils.checkUserPermission('AUTH_UPDATES_IMPAIRMENTS');
    this.boolAuthorizeClm = Utils.checkUserPermission('AUTH_UPDATES_CLAIMS');
  }

  ngOnInit() {
    if (localStorage.getItem('successMsg')) {
      this.successStatus = localStorage.getItem('successMsg');
      this.alertService.success(this.successStatus, true);

    }
    this.getAuthImpairments();
  }
  /**
  Description: Function to get Authorise impairments and claims
  @parameters: No
  @Return: No
*/
  getAuthImpairments() {
    this.spinner.show();
    this.authoriseService.getAuthoriseImp().subscribe(response => {
      if (Utils.chkResponseSuccess(response)) {
        this.spinner.hide();
        this.impairments = response['body'];
      }
    }, error => {
      this.spinner.hide();
      this.handleServiceError(error);
    });
  }
  handleServiceError(error) {
    // Handle service error here
    const strMsg: any = Utils.chkResponseSuccess(error);
    this.alertService.error(strMsg);
  }
  /**
     Description: Function to Approve imapirments or claims
     @parameters: authoriseType, id, personId
     @Return: No
   */
  onApproveImpairmentSubmit(impairment) {
    const updateReferenceData = {
      'action': 'APPROVE',
      'notificationID': impairment.notificationID,
      'transType': impairment.transType
    };
    this.spinner.show();
    this.authoriseService.approveImpairments(updateReferenceData).subscribe(response => {
      if (Utils.chkResponseSuccess(response)) {
        this.spinner.hide();
        if (response['status'] === 200) {
          this.getAuthImpairments();
          this.alertService.success(response['body']['successMessage'], true);
          this.authoriseService.getAuthoriseCount().subscribe(responseList => {
            this.count = responseList[0].length + responseList[1].length;
            this.authoriseService.changeNotificationCount(this.count);
          });
        } else {
          this.alertService.error(response['body']['successMessage'], true);
        }
      }
    }, error => {
      this.spinner.hide();
      this.handleServiceError(error);
    });
  }
  getAuthoriseImpairment(notificationID) {
    localStorage.setItem('pageName', null);
    this._router.navigate(['/authorise-impairment', notificationID]);
  }
  getReferenceData(data) {
    this.referImpairmentData = data;
  }

  /**
     Description: Function to reject impairment
     @parameters: impairmentId
     @Return: No
   */
  impairmentRejectSuccess($event) {
    if ($event === 'reject_impairment_success') {
      this.getAuthImpairments();
    }
  }
}
