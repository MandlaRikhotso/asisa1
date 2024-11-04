import { Component, OnInit } from '@angular/core';
import { NgxSpinnerService } from 'ngx-spinner';

import {
  AuthoriseService,
  AlertService,
  PersonDetailsService
} from '../_services';
import { Utils } from '../utils';
import { ActivatedRoute } from '@angular/router';
import { Router } from "@angular/router";
@Component({
  selector: "app-authorise-impairments",
  templateUrl: "./authorise-impairments.component.html",
  styleUrls: ["./authorise-impairments.component.css"]
})
export class AuthoriseImpairmentsComponent implements OnInit {
  authoriseDetails: any;
  impairments: any = {};
  impairmentData: any = {};
  strSplInvestSymbol: any = [];
  strNote: any = [];
  flagArray: any;
  referImpairmentData: any;
    boolAuthorizeImp: boolean = false;
  boolAuthorizeClm: boolean = false;
  count: any;
  constructor(
    private authoriseService: AuthoriseService,
    private _personDetailsService: PersonDetailsService,
    private alertService: AlertService,
    private spinner: NgxSpinnerService,
    private _activatedRoute: ActivatedRoute,
    private _router: Router
  ) { }

  ngOnInit() {
    const parameter: any = this._activatedRoute.snapshot.params["id"];
    this.getImpairmentDetails(parameter);
    this.boolAuthorizeImp = Utils.checkUserPermission('AUTH_UPDATES_IMPAIRMENTS');
    this.boolAuthorizeClm = Utils.checkUserPermission('AUTH_UPDATES_CLAIMS');
  }

  /**
  Description: Function to Approve impairment
  @parameters: id, personId
  @Return: No
*/
  onApproveImpairmentSubmit(impairment) {
    // tslint:disable-next-line:prefer-const
    let updateReferenceData = {
      action: "APPROVE",
      notificationID: impairment.new.notificationID,
      transType: impairment.transType
    };
    // this.spinner.show();
    this.authoriseService.approveImpairments(updateReferenceData).subscribe(
      response => {
        if (Utils.chkResponseSuccess(response)) {
          // this.spinner.hide();
          if (response['status'] === 200) {
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
  Description: Function to get reference data for reject imapirment
  @parameters:impairmentdata
  @Return: No
*/
  getReferenceData(data) {
    this.referImpairmentData = data;
  }
  /**
  Description: Function to get impairment details
  @parameters: No
  @Return: No
  */
  getImpairmentDetails(parameter) {
    this.spinner.show();
    this.authoriseService.getAuthoriseImpairmentDetails(parameter).subscribe(
      response => {
        if (Utils.chkResponseSuccess(response)) {
          // this.spinner.hide();
          this.impairments = response["body"];

          this.impairmentData = {
            'newtimeSignal': this.impairments.new.timeSignal,
            'oldtimeSignal': this.impairments.old.timeSignal,
            'newReadings': this.impairments.new.readings,
            'oldReadings': this.impairments.old.readings,
          };
          this.impairments.insuredPerson[
            "lifeOffice"
          ] = this.impairments.new.lifeOffice;
          // localStorage.setItem("pageName", "History");
          this._personDetailsService.changePersonInfo(
            this.impairments.insuredPerson
          );
          let newstrSplInvest = Utils.convertArrToString(
            this.impairments.new.specialInvestigation
          );
          this.strSplInvestSymbol.push(newstrSplInvest);
          let oldstrSplInvest = Utils.convertArrToString(
            this.impairments.old.specialInvestigation
          );
          this.strSplInvestSymbol.push(oldstrSplInvest);
          let newstrSymbol = Utils.convertArrToString(
            this.impairments.new.symbol
          );
          this.strSplInvestSymbol.push(newstrSymbol);
          let oldstrSymbol = Utils.convertArrToString(
            this.impairments.old.symbol
          );
          this.strSplInvestSymbol.push(oldstrSymbol);

          let strNotes: any = [];
          let strScratchpad: any = [];
          let strNotesStr: any;
          let strScratchpadStr: any;
          if (this.impairments.note) {
            this.impairments.note.forEach((element, index) => {
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
      },
      error => {
        this.spinner.hide();
        this.handleServiceError(error);
      }
    );
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
  capAuthoriseImpairment() {
    Utils.downloadSnapshot(
      "authoriseImpairment",
      "authorise-impairment-screen"
    );
  }
}
