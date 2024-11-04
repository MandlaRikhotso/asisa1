import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { SearchInsuredPersonService, ConfirmModalService, AlertService, PersonDetailsService } from '../_services';
import { NgxSpinnerService } from 'ngx-spinner';
import { OrderPipe } from 'ngx-order-pipe';
import { Utils } from '../utils';

@Component({
  selector: 'app-claims-history',
  templateUrl: './claims-history.component.html',
  styleUrls: ['./claims-history.component.css']
})

export class ClaimsHistoryComponent implements OnInit {
  arrTabData: any = [];
  subActive: boolean = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  editEnableActive:  boolean = false;
  objTabInfo: any;
  claims: any = [];
  modalData: any;
  show: boolean = true;
  @Input() insuredPersonId: any;
  @Input() personCliamHistory: any = [];
  selectedPersonId: any;
  insuredPersonObj: any;
  boolStatus: boolean = true;
  order: string = 'dateCreated';
  reverse: boolean = false;
  loginUser: any;
  boolAddUpdtClm: boolean = false;
  sortedCollection: any[];
  constructor(
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _confirmModalService: ConfirmModalService,
    private _alertService: AlertService,
    private _router: Router,
    private _personDetailsService: PersonDetailsService,
    private _orderPipe: OrderPipe,
    private _spinner: NgxSpinnerService
  ) {
    this.loginUser = JSON.parse(localStorage.getItem('currentUser'));
    this.boolAddUpdtClm = Utils.checkUserPermission('ADD_UPDATE_CLAIM');
  }

  ngOnInit() {
   if (this.personCliamHistory) {
    setTimeout(() => {
      this.getClaims();
    }, 1000);
    }
    if (this.insuredPersonId) {
      this.getClaimHistory();
    }
  }
  /**
    Description: Function to get claim details
    @parameters: No
    @Return: No
  */
  getClaims() {
    if (this.personCliamHistory.claimHistory) {
      const formData: any = [];
      this.personCliamHistory['claimHistory'].forEach(item => {
        let arrClaimData: any = [];
        arrClaimData = {
          'notifiableClaimID': item.notifiableClaimID,
          'notificationID': item.notificationID,
          'claimCategory': item.claimCategory,
          'eventDate': Utils.dtFormat(item.eventDate),
          'eventCause': (item.eventCause === null ) ? '-' : item.eventCause['description'],
          'eventDeathPlace': item.eventDeathPlace,
          'eventDeathCertificateNo': (item.eventDeathCertificateNo) ? item.eventDeathCertificateNo : '-',
          'dha1663Number ': (item.dha1663Number ) ? item.dha1663Number  : '-',
          'claimReason': item.claimReason,
          'claimStatus': (item.claimStatus === null) ? '-' : item.claimStatus['description'],
          'paymentMethod':  (item.paymentMethod === null) ? '-' : item.paymentMethod['description'],
          'updateReason': item.updateReason,
          'policyNumber': item.policyNumber,
          'claimType': item.claimType['description'],
          'dateCreated': item.dateCreated,
          'notificationSource': item.notificationSource,
          'createdBy': item.createdBy,
          'notificationStatus': item.notificationStatus
        };
        formData.push(arrClaimData);
      });
      this.claims = this._orderPipe.transform(formData, 'dateCreated');
    }
  }
  customComparator(itemA, itemB) {
    return itemA < itemB ? 1 : -1;
  }
  /**
    Description: Function to display modal popup to deconste claim
    @parameters: impairmentId
    @Return: No
  */
  deleteClaim(claimId, notificationID) {
    this.modalData = {
      'modalName': 'Claim',
      'claimId': claimId,
      'notificationID': notificationID
    };
    this._confirmModalService.changeObj(this.modalData);
  }

  /**
    Description: Function to delete claim
    @parameters: impairmentId
    @Return: No
  */
  claimDeleteSuccess($event) {
    if ($event === 'delete_claim_success') {
      this.getClaimHistory();
    }
  }
  getClaimHistory() {
    if (this.insuredPersonId) {
    this.boolStatus = false;
    }
    const objIdPerson = JSON.parse(localStorage.getItem('personIdDetails'));
    if (objIdPerson) {
      this._searchInsuredPersonService.getCurrentPersonDetails(objIdPerson).subscribe(response => {
        const formData: any = [];
        // let newArray: any = [];
        if (Utils.chkResponseSuccess(response)) {
          response['body']['claimHistory'].forEach(item => {
            let arrClaimData: any = [];
            arrClaimData = {
              'notifiableClaimID': item.notifiableClaimID,
              'notificationID': item.notificationID,
              'claimCategory': item.claimCategory,
              'eventDate': Utils.dtFormat(item.eventDate),
              'eventCause': (item.eventCause === null ) ? '-' : item.eventCause['description'],
              'eventDeathPlace': item.eventDeathPlace,
              'eventDeathCertificateNo': (item.eventDeathCertificateNo) ? item.eventDeathCertificateNo : '-',
              'dha1663Number': (item.dha1663Number) ? item.dha1663Number : '-',
              'claimReason': item.claimReason,
              'claimStatus': (item.claimStatus === null) ? '-' : item.claimStatus['description'],
              'paymentMethod':  (item.paymentMethod === null) ? '-' : item.paymentMethod['description'],
              'updateReason': item.updateReason,
              'policyNumber': item.policyNumber,
              'claimType': item.claimType['description'],
              'dateCreated': item.dateCreated,
              'notificationSource': item.notificationSource,
              'createdBy': item.createdBy,
              'notificationStatus': item.notificationStatus
            };
            formData.push(arrClaimData);
          });
          this.claims = this._orderPipe.transform(formData, 'dateCreated');
        }
      }, error => {
        this.handleServiceError(error);
      });
    }
  }
  /**
    Description: Function to add/edit claim
    @parameters: claimId
    @Return: No
  */
  addClaim(claimId, notificationID) {
    const objPersonIdDetails = JSON.parse(localStorage.getItem('personIdDetails'));
    const selectedPersonId = objPersonIdDetails.identityNumber;
    localStorage.setItem('insuredPersonId', selectedPersonId);
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.arrTabData = response.tabs;
        if (this.arrTabData) {
          if (claimId === undefined) {
            // Add Person
            this.arrTabData[0].active = false;
            this.arrTabData[0].show = false;
            this.arrTabData[0].borderBottomClass = false;
            this.arrTabData[0].tabContent = false;
            this.arrTabData[0].formValid = false;

            // Add Impairment
            this.arrTabData[1].active = false;
            this.arrTabData[1].show = false;
            this.arrTabData[1].borderBottomClass = false;
            this.arrTabData[1].tabContent = false;
            this.arrTabData[1].formValid = false;

            // Add Claim
            this.arrTabData[2].tabName = 'ADD CLAIM';
            this.arrTabData[2].active = true;
            this.arrTabData[2].show = true;
            this.arrTabData[2].borderBottomClass = true;
            this.arrTabData[2].tabContent = true;
            this.arrTabData[2].formValid = false;

            // Add Note
            this.arrTabData[3].active = false;
            this.arrTabData[3].show = true;
            this.arrTabData[3].borderBottomClass = false;
            this.arrTabData[3].tabContent = false;
            this.arrTabData[3].formValid = false;

            // Add Scratchpad
            this.arrTabData[4].active = false;
            this.arrTabData[4].show = true;
            this.arrTabData[4].borderBottomClass = false;
            this.arrTabData[4].tabContent = false;
            this.arrTabData[4].formValid = false;
            // this.subActive = false;
            response.submitActive = false;
            this.insuredPersonObj = { 'personId': selectedPersonId, 'seltabName': true };
            localStorage.setItem('objExistingperson', JSON.stringify(this.insuredPersonObj));
          }
          // tslint:disable-next-line:one-line
          else {
            // Add Person
            this.arrTabData[0].active = false;
            this.arrTabData[0].show = false;
            this.arrTabData[0].borderBottomClass = false;
            this.arrTabData[0].tabContent = false;
            this.arrTabData[0].formValid = false;

            // Add Impairment
            this.arrTabData[1].active = false;
            this.arrTabData[1].show = false;
            this.arrTabData[1].borderBottomClass = false;
            this.arrTabData[1].tabContent = false;
            this.arrTabData[1].formValid = false;

            // Add Claim
            this.arrTabData[2].tabName = 'EDIT CLAIM';
            this.arrTabData[2].active = true;
            this.arrTabData[2].show = true;
            this.arrTabData[2].borderBottomClass = true;
            this.arrTabData[2].tabContent = true;
            this.arrTabData[2].formValid = false;

            // Add Note
            this.arrTabData[3].active = true;
            this.arrTabData[3].show = true;
            this.arrTabData[3].borderBottomClass = false;
            this.arrTabData[3].tabContent = false;
            this.arrTabData[3].formValid = false;

            // Add Scratchpad
            this.arrTabData[4].active = true;
            this.arrTabData[4].show = true;
            this.arrTabData[4].borderBottomClass = false;
            this.arrTabData[4].tabContent = false;
            this.arrTabData[4].formValid = false;
            // this.subActive = false;
            response.submitActive = false;
            response.noteActive = false;
            response.scratchpadtActive = false;
            response.editEnableActive = false;
            // tslint:disable-next-line:max-line-length
            this.insuredPersonObj = { 'personId': selectedPersonId, 'claimId': claimId, 'notificationID': notificationID, 'seltabName': false };
            // Set person id in storage to reuse further
            localStorage.setItem('objExistingperson', JSON.stringify(this.insuredPersonObj));
          }
        }
        this.objTabInfo = {
          'tabs': this.arrTabData,
          'submitActive': this.subActive,
          'noteActive': this.noteActive,
          'scratchpadtActive': this.scratchpadtActive,
          'editEnableActive': this.editEnableActive,
          'personId': selectedPersonId
        };
      }
    });
    let objAction;
    if (claimId === undefined) {
      objAction = {
        'action': 'ADD CLAIM'
      };
    } else {
      objAction = {
        'action': 'EDIT CLAIM'
      };
    }
    localStorage.setItem('pageAction', JSON.stringify(objAction));
    localStorage.setItem('pageName', 'History');
    this._router.navigate(['/person-details']);
    // this._spinner.hide();
  }

  /**
    Description: Function to sort ASC and DESC order
    @parameters: Column Name
    @Return: No
  */
  setOrder(value: string) {
    if (this.order === value) {
      this.reverse = !this.reverse;
    }
    this.order = value;
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
  }
}
