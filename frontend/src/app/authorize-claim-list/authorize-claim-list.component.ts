import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthoriseService, AlertService, AuthenticationService } from '../_services';
import { Router } from '@angular/router';
import { Utils } from '../utils';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-authorize-claim-list',
  templateUrl: './authorize-claim-list.component.html',
  styleUrls: ['./authorize-claim-list.component.css']
})
export class AuthorizeClaimListComponent implements OnInit {
  private subscription: Subscription;
  claims: any = [];
  count: any;
  successStatus: any;
  referClaimData: any;
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
    this.getAuthClaims();
  }
  getAuthClaims() {
    this.spinner.show();
    this.authoriseService.getAuthoriseClaim().subscribe(response => {
      if (Utils.chkResponseSuccess(response)) {
        this.spinner.hide();
        this.claims = response['body'];
        let claimReasonList: any;
        this.claims.forEach((item, Index) => {
          claimReasonList = Utils.convertArrToString(item.claimReason);
          this.claims[Index]['claimReasons'] = claimReasonList;
        });
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
  onApproveClaimSubmit(claim) {
    const updateReferenceData = {
      'action': 'APPROVE',
      'notificationID': claim.notificationID,
      'transType': claim.transType
    };
    this.spinner.show();
    this.authoriseService.approveClaims(updateReferenceData).subscribe(response => {
      if (Utils.chkResponseSuccess(response)) {
        this.spinner.hide();
        if (response['status'] === 200) {
          this.getAuthClaims();
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
  getAuthoriseClaim(notificationID) {
    localStorage.setItem('pageName', null);
    this._router.navigate(['/authorise-claim', notificationID]);
  }
  getReferenceData(data) {
    this.referClaimData = data;
  }
  /**
     Description: Function to reject claim
     @parameters: impairmentId
     @Return: No
   */
  impairmentRejectSuccess($event) {
    if ($event === 'reject_impairment_success') {
      this.getAuthClaims();
    }
  }
}
