import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { PersonDetailsService, AlertService, AuthoriseService } from '../_services';
import { NgxSpinnerService } from 'ngx-spinner';
import { Utils } from '../utils';

@Component({
  selector: 'app-tabs',
  templateUrl: './tabs.component.html',
  styleUrls: ['./tabs.component.css']
})

export class TabsComponent implements OnInit, OnDestroy {
  objTabInfo: any;
  tabInformation: any;
  arrTabData: any = [];
  subActive: boolean = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  editEnableActive: boolean = false;
  @Input() insuredPersondetails: any;
  @Input() formStatus: any ;
  styleClass = 'custom-li-border-bottom';
  insuredPersonDetailsInfo: any = [];
  count: any;
  constructor(
    private _personDetailsService: PersonDetailsService,
    private authoriseService: AuthoriseService,
    private _alertService: AlertService,
    private _router: Router,
    private _spinner: NgxSpinnerService
  ) { }

  ngOnInit() {
    // tabs info start
    this.formStatus = false;
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.tabInformation = response;
      }
    });
  }

  /**
    Description: Function to show/hide tabs
    @parameters: No
    @Return: No
  */
  showTabs(tabIndex: number) {
    switch (tabIndex) {
      case 0:  // Add Person tab
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              if (this.arrTabData[0].formValid) {
                if (Utils.checkUserPermission('ADD_UPDATE_IMPAIRMENT')) {
                  this.arrTabData[0].borderBottomClass = true;
                  this.arrTabData[0].tabContent = true;
                  this.arrTabData[0].formValid = true;
                  this.arrTabData[1].show = true;
                  this.arrTabData[1].active = true;
                  this.arrTabData[1].tabContent = false;
                  this.arrTabData[1].borderBottomClass = false;
                  this.arrTabData[2].show = false;
                  this.arrTabData[2].tabContent = false;
                  this.arrTabData[2].borderBottomClass = false;
                  this.arrTabData[3].tabContent = false;
                  this.arrTabData[3].borderBottomClass = false;
                  this.arrTabData[4].tabContent = false;
                  this.arrTabData[4].borderBottomClass = false;
                } else if (Utils.checkUserPermission('ADD_UPDATE_CLAIM')) {
                  this.arrTabData[0].borderBottomClass = true;
                  this.arrTabData[0].tabContent = true;
                  this.arrTabData[0].formValid = true;
                  this.arrTabData[1].show = false;
                  this.arrTabData[1].active = false;
                  this.arrTabData[1].tabContent = false;
                  this.arrTabData[1].borderBottomClass = false;
                  this.arrTabData[2].show = true;
                  this.arrTabData[2].active = true;
                  this.arrTabData[2].tabContent = false;
                  this.arrTabData[2].borderBottomClass = false;
                  this.arrTabData[3].tabContent = false;
                  this.arrTabData[3].borderBottomClass = false;
                  this.arrTabData[4].tabContent = false;
                  this.arrTabData[4].borderBottomClass = false;
                }
              } else {
                if (Utils.checkUserPermission('ADD_UPDATE_IMPAIRMENT')) {
                  this.arrTabData[0].borderBottomClass = true;
                  this.arrTabData[0].tabContent = true;
                  this.arrTabData[0].formValid = false;
                  this.arrTabData[1].show = true;
                  this.arrTabData[1].active = false;
                  this.arrTabData[1].tabContent = false;
                  this.arrTabData[1].borderBottomClass = false;
                  this.arrTabData[2].show = false;
                  this.arrTabData[2].tabContent = false;
                  this.arrTabData[2].borderBottomClass = false;
                  this.arrTabData[3].tabContent = false;
                  this.arrTabData[3].borderBottomClass = false;
                  this.arrTabData[4].tabContent = false;
                  this.arrTabData[4].borderBottomClass = false;
                } else if (Utils.checkUserPermission('ADD_UPDATE_CLAIM')) {
                  this.arrTabData[0].borderBottomClass = true;
                  this.arrTabData[0].tabContent = true;
                  this.arrTabData[0].formValid = false;
                  this.arrTabData[1].show = false;
                  this.arrTabData[1].active = false;
                  this.arrTabData[1].tabContent = false;
                  this.arrTabData[1].borderBottomClass = false;
                  this.arrTabData[2].show = true;
                  this.arrTabData[2].active = false;
                  this.arrTabData[2].tabContent = false;
                  this.arrTabData[2].borderBottomClass = false;
                  this.arrTabData[3].tabContent = false;
                  this.arrTabData[3].borderBottomClass = false;
                  this.arrTabData[4].tabContent = false;
                  this.arrTabData[4].borderBottomClass = false;
                }
              }
            }
          }
        });
        break;

      case 1: // Add Impairment tab
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              if (this.arrTabData[1].formValid) {
                this.arrTabData[0].tabContent = false;
                this.arrTabData[0].borderBottomClass = false;
                this.arrTabData[1].tabContent = true;
                this.arrTabData[1].borderBottomClass = true;
                this.arrTabData[2].show = false;
                this.arrTabData[2].tabContent = false;
                this.arrTabData[2].borderBottomClass = false;
                this.arrTabData[3].tabContent = false;
                this.arrTabData[3].borderBottomClass = false;
                this.arrTabData[4].tabContent = false;
                this.arrTabData[4].borderBottomClass = false;
                this.subActive = true;
              } else {
                this.arrTabData[0].tabContent = false;
                this.arrTabData[0].borderBottomClass = false;
                this.arrTabData[1].tabContent = true;
                this.arrTabData[1].borderBottomClass = true;
                this.arrTabData[2].show = false;
                this.arrTabData[2].tabContent = false;
                this.arrTabData[2].borderBottomClass = false;
                this.arrTabData[3].tabContent = false;
                this.arrTabData[3].borderBottomClass = false;
                this.arrTabData[4].tabContent = false;
                this.arrTabData[4].borderBottomClass = false;
                this.subActive = false;
              }
            }
          }
        });
        break;

      case 2: // Add Claim tab
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              const pgName = localStorage.getItem('pageName');
              if (pgName === 'History') {
                if (this.arrTabData[2].formValid) {
                  this.arrTabData[0].show = false;
                  this.arrTabData[0].tabContent = false;
                  this.arrTabData[0].borderBottomClass = false;
                  this.arrTabData[1].show = false;
                  this.arrTabData[1].tabContent = false;
                  this.arrTabData[1].borderBottomClass = false;
                  this.arrTabData[2].tabContent = true;
                  this.arrTabData[2].borderBottomClass = true;

                  this.arrTabData[3].active = true;
                  this.arrTabData[3].tabContent = false;
                  this.arrTabData[3].borderBottomClass = false;
                  this.arrTabData[4].active = true;
                  this.arrTabData[4].tabContent = false;
                  this.arrTabData[4].borderBottomClass = false;
                  this.subActive = true;
                } else {
                  this.arrTabData[0].show = false;
                  this.arrTabData[0].tabContent = false;
                  this.arrTabData[0].borderBottomClass = false;
                  this.arrTabData[1].show = false;
                  this.arrTabData[1].tabContent = false;
                  this.arrTabData[1].borderBottomClass = false;
                  this.arrTabData[2].tabContent = true;
                  this.arrTabData[2].borderBottomClass = true;
                  this.arrTabData[3].tabContent = false;
                  this.arrTabData[3].borderBottomClass = false;
                  this.arrTabData[4].tabContent = false;
                  this.arrTabData[4].borderBottomClass = false;
                  this.subActive = false;
                }
              } else {
                if (this.arrTabData[2].formValid) {
                  this.arrTabData[0].show = true;
                  this.arrTabData[0].tabContent = false;
                  this.arrTabData[0].borderBottomClass = false;
                  this.arrTabData[1].show = false;
                  this.arrTabData[1].tabContent = false;
                  this.arrTabData[1].borderBottomClass = false;
                  this.arrTabData[2].tabContent = true;
                  this.arrTabData[2].borderBottomClass = true;

                  this.arrTabData[3].active = true;
                  this.arrTabData[3].tabContent = false;
                  this.arrTabData[3].borderBottomClass = false;
                  this.arrTabData[4].active = true;
                  this.arrTabData[4].tabContent = false;
                  this.arrTabData[4].borderBottomClass = false;
                  this.subActive = true;
                } else {
                  this.arrTabData[0].show = true;
                  this.arrTabData[0].tabContent = false;
                  this.arrTabData[0].borderBottomClass = false;
                  this.arrTabData[1].show = false;
                  this.arrTabData[1].tabContent = false;
                  this.arrTabData[1].borderBottomClass = false;
                  this.arrTabData[2].tabContent = true;
                  this.arrTabData[2].borderBottomClass = true;
                  this.arrTabData[3].tabContent = false;
                  this.arrTabData[3].borderBottomClass = false;
                  this.arrTabData[4].tabContent = false;
                  this.arrTabData[4].borderBottomClass = false;
                  this.subActive = false;
                }
              }
            }
          }
        });
        break;

      case 3: // Add Note tab
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              this.arrTabData[0].tabContent = false;
              this.arrTabData[0].borderBottomClass = false;
              this.arrTabData[1].tabContent = false;
              this.arrTabData[1].borderBottomClass = false;
              this.arrTabData[2].tabContent = false;
              this.arrTabData[2].borderBottomClass = false;
              this.arrTabData[3].tabContent = true;
              this.arrTabData[3].borderBottomClass = true;
              this.arrTabData[4].tabContent = false;
              this.arrTabData[4].borderBottomClass = false;
              this.subActive =false;
              this.noteActive = false;
              this.scratchpadtActive =false;
            }
          }
        });
        break;

      case 4: // Add Scratchpad tab
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              this.arrTabData[0].tabContent = false;
              this.arrTabData[0].borderBottomClass = false;
              this.arrTabData[1].tabContent = false;
              this.arrTabData[1].borderBottomClass = false;
              this.arrTabData[2].tabContent = false;
              this.arrTabData[2].borderBottomClass = false;
              this.arrTabData[3].tabContent = false;
              this.arrTabData[3].borderBottomClass = false;
              this.arrTabData[4].tabContent = true;
              this.arrTabData[4].borderBottomClass = true;
              this.subActive = response.subActive;
              this.noteActive = response.noteActive;
              this.scratchpadtActive = response.scratchpadtActive;
            }
          }
        });
        break;

      default:
        break;
    }

    this.objTabInfo = {
      'tabs': this.arrTabData,
      'submitActive': this.subActive,
      'noteActive': this.noteActive,
      'scratchpadtActive': this.scratchpadtActive,
      'personId': ''
    };
    this._personDetailsService.changeTabInformation(this.objTabInfo);
    this._personDetailsService.getTabInformation.subscribe(response => {
      this.tabInformation = response;
    });

  }

  /**
    Description: Function to add/edit impairment and claim
    @parameters: No
    @Return: No
  */
  submit() {
    this._spinner.show();
    const strAction = JSON.parse(localStorage.getItem('pageAction'));
    switch (strAction.action) {
      case 'ADD IMPAIRMENT':
        this.addImpairment();
        break;
      case 'EDIT IMPAIRMENT':
        this.updateImpairment();
        break;
      case 'ADD CLAIM':
        this.addClaim();
        break;
      case 'EDIT CLAIM':
        this.updateClaim();
        break;
      case 'ADD SCRATCHPAD':
        this.addScratchpad();
        break;
    }
  }

  /**
    Description: Function to add impairments for new and existing person
    @parameters: No
    @Return: No
  */
  addImpairment() {
    this._spinner.show();
    this.insuredPersonDetailsInfo = Utils.setImpairmentsData(this.insuredPersondetails);
    if (this.insuredPersonDetailsInfo) {
      this._personDetailsService.addPersonImpairments(this.insuredPersonDetailsInfo).subscribe(response => {
        this._spinner.hide();
        if (Utils.chkResponseSuccess(response)) {
          this.addUpdateSuccess(response);
        }
      }, error => {
        this.handleServiceError(error);
      });
    }
  }

  /**
    Description: Function to add claims for new and existing person
    @parameters: No
    @Return: No
  */
  addClaim() {
    this._spinner.show();
    this.insuredPersonDetailsInfo = Utils.setClaimsData(this.insuredPersondetails)
    if (this.insuredPersonDetailsInfo) {
      this._personDetailsService.addPersonClaims(this.insuredPersonDetailsInfo).subscribe(response => {
        this._spinner.hide();
        if (Utils.chkResponseSuccess(response)) {
          this.addUpdateSuccess(response);
        }
      }, error => {
        this.handleServiceError(error);
      });
    }
  }

  /**
    Description: Function to update impairments for new and existing person
    @parameters: No
    @Return: No
  */
  updateImpairment() {
    this._spinner.show();
    const tempArray = Utils.setImpForUpdate(this.insuredPersondetails);
    this._personDetailsService.updatePersonImpairment(tempArray).subscribe(response => {
      this._spinner.hide();
      if (Utils.chkResponseSuccess(response)) {
        localStorage.setItem("selectedImpId", null);
        localStorage.removeItem('notificationID');
        this.addUpdateSuccess(response);
        this.authoriseService.getAuthoriseCount().subscribe(data => {
          this.count = (data > 0) ? data : 0;
          this.authoriseService.changeNotificationCount(this.count);
        });
      }
    }, error => {
      this.handleServiceError(error);
    });
  }

  /**
    Description: Function to update claims for new and existing person
    @parameters: No
    @Return: No
  */
  updateClaim() {
    this._spinner.show();
    const tempArray = Utils.setClaimForUpdate(this.insuredPersondetails);
    this._personDetailsService.updatePersonClaim(tempArray).subscribe(response => {
      this._spinner.hide();
      if (Utils.chkResponseSuccess(response)) {
        localStorage.removeItem('selectedClaimId');
        localStorage.removeItem('notificationID');
        this.addUpdateSuccess(response);
        this.authoriseService.getAuthoriseCount().subscribe(data => {
          this.count = (data > 0) ? data : 0;
          this.authoriseService.changeNotificationCount(this.count);
        });
      }
    }, error => {
      this.handleServiceError(error);
    });
  }



   /**
    Description: Function to add impairments for new and existing person
    @parameters: No
    @Return: No
  */
 addScratchpad() {
  this._spinner.show();
  this.insuredPersonDetailsInfo = Utils.setScratchpadData(this.insuredPersondetails);
  if (this.insuredPersonDetailsInfo) {
    if (Utils.checkUserPermission('ADD_UPDATE_IMPAIRMENT')) {
    this._personDetailsService.addPersonImpairments(this.insuredPersonDetailsInfo).subscribe(response => {
      this._spinner.hide();
      if (Utils.chkResponseSuccess(response)) {
        this.addUpdateSuccess(response);
      }
    }, error => {
      this.handleServiceError(error);
    });
  } else if (Utils.checkUserPermission('ADD_UPDATE_CLAIM')) {
    this._personDetailsService.addPersonClaims(this.insuredPersonDetailsInfo).subscribe(response => {
      this._spinner.hide();
      if (Utils.chkResponseSuccess(response)) {
        this.addUpdateSuccess(response);
      }
    }, error => {
      this.handleServiceError(error);
    });
  }
  }
}
  /**
    Description: Function to Add/Update response successfully and redirect to success screen
    @parameters: No
    @Return: No
  */

  addUpdateSuccess(response) {
    const redTo = localStorage.getItem('redirectTo');
    if (redTo === 'Enquiry') {
      this._router.navigate(['/impairments-saved']);
    } else {
      localStorage.setItem('successMsg', response['body']['successMessage']);
      this._router.navigate(['/insured-person-details']);
    }
  }

  /**
    Description: Function to handle service error
    @parameters: No
    @Return: No
  */
  handleServiceError(error) {
    // Handle service error here
    const strMsg: any = Utils.chkResponseSuccess(error);
    this._alertService.error(strMsg);
    this._spinner.hide();
  }

  /**
    Description: Function to destroy services on component destroy
    @parameters: No
    @Return: No
  */
  ngOnDestroy() {
    // Set person information
    this._personDetailsService.changePersonInfo(null);
  }
}
