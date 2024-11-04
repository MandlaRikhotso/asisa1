import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';
import { AuthoriseService, AlertService, PersonDetailsService } from '../_services';
import { Utils } from '../utils';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
@Component({
  selector: 'app-authorise-claims',
  templateUrl: './authorise-claims.component.html',
  styleUrls: ['./authorise-claims.component.css']
})
export class AuthoriseClaimsComponent implements OnInit {
  authoriseDetails: any;
  objPerson: any;
  claims: any = {};
  referClaimData: any;
  strNote: any = [];
    boolAuthorizeImp: boolean = false;
  boolAuthorizeClm: boolean = false;
  count: any;
  constructor(
    private authoriseService: AuthoriseService,
    private _personDetailsService: PersonDetailsService,
    private alertService: AlertService,
    private spinner: NgxSpinnerService,
    private _activatedRoute: ActivatedRoute,
    private _router: Router,
  ) { }

  ngOnInit() {
    const parameter: any = this._activatedRoute.snapshot.params['id'];
    this.getClaimDetails(parameter);
    this.boolAuthorizeImp = Utils.checkUserPermission('AUTH_UPDATES_IMPAIRMENTS');
    this.boolAuthorizeClm = Utils.checkUserPermission('AUTH_UPDATES_CLAIMS');
  }

  /**
  Description: Function to Approve claims
  @parameters: id, personId
  @Return: No
*/
  onApproveClaimSubmit(claim) {
    this.spinner.show();
    const updateReferenceData = {
      'action': 'APPROVE',
      'notificationID': claim.new.notificationID,
      'transType': claim.transType
    };
    this.authoriseService.approveClaims(updateReferenceData).subscribe(
      response => {
        if (Utils.chkResponseSuccess(response)) {
          this.spinner.hide();
          if (response["status"] === 200) {
            localStorage.setItem('successMsg', response['body']['successMessage']);
            this.authoriseService.getAuthoriseCount().subscribe(data => {
              this.count = (data > 0) ? data : 0;
              this.authoriseService.changeNotificationCount(this.count);
            });
            this._router.navigate(['/authorise']);
          } else {
            this.alertService.error(response["body"]["successMessage"], true);
          }
        }
      },
      error => {
        this.spinner.hide();
        this.handleServiceError(error);
      }
    );
  }
  /**
  Description: Function to get reference data for reject claim
  @parameters:claimdata
  @Return: No
*/
  getReferenceData(data) {
    this.referClaimData = data;
  }
  getClaimDetails(parameter) {
    this.spinner.show();
    this.authoriseService.getAuthoriseClaimDetails(parameter).subscribe(response => {
      if (Utils.chkResponseSuccess(response)) {
        // this.spinner.hide();
        this.claims = response['body'];
        // Set person information
        this.claims.insuredPerson['lifeOffice'] = this.claims.new.lifeOffice;
        localStorage.setItem('pageName', 'History');
        this._personDetailsService.changePersonInfo(this.claims.insuredPerson);
        let strNotes: any = [];
        let strScratchpad: any = [];
        let strNotesStr: any;
        let strScratchpadStr: any;
        if (this.claims.note) {
          this.claims.note.forEach((element, index) => {
            if (element.scratchpad === 'N') {
              strNotes.push(element['noteText']);
            } else {
              strScratchpad.push(element['noteText']);
            }

          });
          strNotesStr = strNotes.join(",");
          strScratchpadStr = strScratchpad.join(",");
          this.strNote.push(strNotesStr);
          this.strNote.push(strScratchpadStr);
        }
        this.spinner.hide();
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
    Description: Function to download snapshot of the screen
    @parameters: No
    @Return: No
  */
  capAuthoriseClaim() {
    Utils.downloadSnapshot('authoriseClaim', 'authorise-claim-screen');
  }
}
