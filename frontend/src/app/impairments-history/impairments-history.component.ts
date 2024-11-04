import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { SearchInsuredPersonService, ConfirmModalService, AlertService, PersonDetailsService } from '../_services';
import { NgxSpinnerService } from 'ngx-spinner';
import { OrderPipe } from 'ngx-order-pipe';
import { Utils } from '../utils';

@Component({
  selector: 'app-impairments-history',
  templateUrl: './impairments-history.component.html',
  styleUrls: ['./impairments-history.component.css']
})

export class ImpairmentsHistoryComponent implements OnInit {
  arrTabData: any = [];
  subActive: boolean = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  editEnableActive:  boolean = false;
  editNoteActive:  boolean = false;
  editSctratchpadActive:  boolean = false;
  objTabInfo: any;
  objFormInfo: any;
  objFormInfoe: any;
  objFormInfoee: any;
  impairments: any = [];
  modalData: any;
  show: boolean = true;
  @Input() insuredPersonId: any;
  @Input() personImpairmentHistory: any ;
  selectedPersonId: any;
  insuredPersonObj: any;
  boolStatus: boolean = true;
  successStatus: any;
  order: string = 'dateCreated';
  reverse: boolean = false;
  loginUser: any;
  boolAddUpdtImp: boolean = false;

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
    this.boolAddUpdtImp = Utils.checkUserPermission('ADD_UPDATE_IMPAIRMENT');
  }

  ngOnInit() {
    this._spinner.show();
    // setTimeout(() => {
    //   this._spinner.hide();
    // }, 2000);
    if (localStorage.getItem('successMsg')) {

      this.successStatus = localStorage.getItem('successMsg');
      this._alertService.success(this.successStatus, true);

    }
    // localStorage.setItem('successMsg', '');
    localStorage.setItem('pageName', 'History');
    localStorage.setItem('redirectTo', 'History');
    localStorage.setItem('objExistingperson', null);
 if (this.personImpairmentHistory) {
  //  this._spinner.hide();
    setTimeout(() => {
      this.getImpairments();
    }, 1000);
}
    if (this.insuredPersonId) {
      // this._spinner.hide();
      this.getImpairmentHistory();
    }
    this._spinner.hide();
  }

  /**
    Description: Function to get impairment details
    @parameters: No
    @Return: No
  */
  getImpairments() {
    // this._spinner.show();
    if (this.personImpairmentHistory) {
      const formData: any = [];
      if (this.personImpairmentHistory.impairmentHistory) {
        this.personImpairmentHistory.impairmentHistory.forEach(item => {
          let arrImpData: any = [];
          const strSplInvest = Utils.convertArrToString(item.specialInvestigation);
          const strSymbol = Utils.convertArrToString(item.symbol);
          arrImpData = {
            'notifiableImpairmentID': item.notifiableImpairmentID,
            'notificationID': item.notificationID,
            'impairment': item.impairment['code'] + '-' + item.impairment['description'],
            'timeSignal': item.timeSignal,
            'readings': (item.readings) ? item.readings : '-',
            'specialInvestigation': (strSplInvest) ? strSplInvest : '-',
            'symbol': (strSymbol) ? strSymbol : '-',
            'updateReason': item.updateReason,
            'policyNumber': item.policyNumber,
            'policyBenefit': item.policyBenefit['description'],
            'dateCreated': item.dateCreated,
            'notificationSource': item.notificationSource,
            'createdBy': item.createdBy,
            'notificationStatus': item.notificationStatus
          };
          formData.push(arrImpData);
        });
        this.impairments = this._orderPipe.transform(formData, 'dateCreated');
      }
      // this._spinner.hide();
    }
  }

  customComparator(itemA, itemB) {
    return itemA < itemB ? 1 : -1;
  }
  /**
    Description: Function to display modal popup to deconste impairment
    @parameters: impairmentId
    @Return: No
  */
  deleteImpairment(impairmentId, notificationID) {
    this.modalData = {
      'modalName': 'Impairment',
      'impairmentId': impairmentId,
      'notificationID': notificationID
    };
    this._confirmModalService.changeObj(this.modalData);
  }

  /**
    Description: Function to delete impairment
    @parameters: impairmentId
    @Return: No
  */
  impairmentDeleteSuccess($event) {
    // this._spinner.show();
    if ($event === 'delete_impairment_success') {
      this.getImpairmentHistory();
    }
  }
  getImpairmentHistory() {
    // this._spinner.show();
    if (this.insuredPersonId) {
    this.boolStatus = false;
    }
    const objIdPerson = JSON.parse(localStorage.getItem('personIdDetails'));
    if (objIdPerson) {
      this._searchInsuredPersonService.getCurrentPersonDetails(objIdPerson).subscribe(response => {
        const formData: any = [];
        if (Utils.chkResponseSuccess(response)) {
          response['body']['impairmentHistory'].forEach(item => {
            let arrImpData: any = [];
            const strSplInvest = Utils.convertArrToString(item.specialInvestigation);
            const strSymbol = Utils.convertArrToString(item.symbol);
            arrImpData = {
              'notifiableImpairmentID': item.notifiableImpairmentID,
              'notificationID': item.notificationID,
              'impairment': item.impairment['code'] + '-' + item.impairment['description'],
              'timeSignal': item.timeSignal,
              'readings': (item.readings) ? item.readings : '-',
              'specialInvestigation': (strSplInvest) ? strSplInvest : '-',
              'symbol': (strSymbol) ? strSymbol : '-',
              'updateReason': item.updateReason,
              'policyNumber': item.policyNumber,
              'policyBenefit': item.policyBenefit['description'],
              'dateCreated': item.dateCreated,
              'notificationSource': item.notificationSource,
              'createdBy': item.createdBy,
              'notificationStatus': item.notificationStatus
            };
            formData.push(arrImpData);
          });
          this.impairments = this._orderPipe.transform(formData, 'dateCreated');
        }
        // this._spinner.hide();
      }, error => {
        this.handleServiceError(error);
        // this._spinner.hide();
      });
    }
  }
  /**
    Description: Function to add/edit impairment
    @parameters: impairmentId
    @Return: No
  */
  addImpairment(impairmentId: string, notificationID: string) {
    localStorage.setItem('impairmentItems', 'null');
    // localStorage.removeItem('impDetails');
    const objPersonIdDetails = JSON.parse(localStorage.getItem('personIdDetails'));
    const selectedPersonId = objPersonIdDetails.identityNumber;
    localStorage.setItem('insuredPersonId', selectedPersonId);
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.arrTabData = response.tabs;
        if (this.arrTabData) {
          // Add Person
          this.arrTabData[0].active = false;
          this.arrTabData[0].show = false;
          this.arrTabData[0].borderBottomClass = false;
          this.arrTabData[0].tabContent = false;
          this.arrTabData[0].formValid = false;

          // Add/Edit Impairment
          // tslint:disable-next-line:max-line-length
          (impairmentId === undefined || impairmentId === null) ? this.arrTabData[1].tabName = 'ADD IMPAIRMENT' : this.arrTabData[1].tabName = 'EDIT IMPAIRMENT';
          this.arrTabData[1].active = true;
          this.arrTabData[1].show = true;
          this.arrTabData[1].borderBottomClass = true;
          this.arrTabData[1].tabContent = true;
          this.arrTabData[1].formValid = false;

          // Add/Edit Claim
          this.arrTabData[2].active = false;
          this.arrTabData[2].show = false;
          this.arrTabData[2].borderBottomClass = false;
          this.arrTabData[2].tabContent = false;
          this.arrTabData[2].formValid = false;
          // Add/Edit Note
          (impairmentId === undefined || impairmentId === null) ? this.arrTabData[3].active = false : this.arrTabData[3].active = true;
          this.arrTabData[3].show = true;
          this.arrTabData[3].borderBottomClass = false;
          this.arrTabData[3].tabContent = false;
          this.arrTabData[3].formValid = false;

          // Add/Edit Scratchpad
          (impairmentId === undefined || impairmentId === null) ? this.arrTabData[4].active = false : this.arrTabData[4].active = true;
          this.arrTabData[4].show = true;
          this.arrTabData[4].borderBottomClass = false;
          this.arrTabData[4].tabContent = false;
          this.arrTabData[4].formValid = false;

          response.submitActive = false;
          response.noteActive = false;
          response.scratchpadtActive = false;
          response.editEnableActive = false;
          response.editNoteActive = false;
          response.editNoteActive = false;

          (impairmentId === undefined || impairmentId === null) ? this.insuredPersonObj = { 'personId': selectedPersonId } :
            this.insuredPersonObj = { 'personId': selectedPersonId, 'impairmentId': impairmentId, 'notificationID': notificationID };
          localStorage.setItem('objExistingperson', JSON.stringify(this.insuredPersonObj));
        }
        this.objTabInfo = {
          'tabs': this.arrTabData,
          'submitActive': this.subActive,
          'noteActive': this.noteActive,
          'scratchpadtActive': this.scratchpadtActive,
          'editEnableActive': this.editEnableActive,
          'editNoteActive': this.editNoteActive,
          'editSctratchpadActive': this.editSctratchpadActive,
          'personId': selectedPersonId,
          'impairmentId': impairmentId ? impairmentId : ''
        };
      }
    });
    this._personDetailsService.changeTabInformation(this.objTabInfo);
    // Set person id in storage to reuse further
    // localStorage.setItem('objExistingperson', JSON.stringify(insuredPersonObj));

    let objAction;
    if (impairmentId === undefined) {
      objAction = {
        'action': 'ADD IMPAIRMENT'
      };
    } else {
      objAction = {
        'action': 'EDIT IMPAIRMENT'
      };
    }
    localStorage.setItem('pageAction', JSON.stringify(objAction));
    localStorage.setItem('pageName', 'History');
    this._router.navigate(['/person-details']);
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
