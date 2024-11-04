import { Component, OnInit, Input } from '@angular/core';
import { Router } from '@angular/router';
import { SearchInsuredPersonService, AlertService, PersonDetailsService } from '../_services';
import { OrderPipe } from 'ngx-order-pipe';
import { Utils } from '../utils';

@Component({
  selector: 'app-scratchpad-history',
  templateUrl: './scratchpad-history.component.html',
  styleUrls: ['./scratchpad-history.component.css']
})

export class ScratchpadHistoryComponent implements OnInit {
  arrTabData: any = [];
  subActive: boolean = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  editEnableActive:  boolean = false;
  objTabInfo: any;
  scratchpad: any = [];
  show: boolean = true;
  @Input() insuredPersonId: any;
  @Input() personScratchpadHistory:any;
  selectedPersonId: any;
  boolStatus: boolean = true;
  order: string = 'dateCreated';
  reverse: boolean = false;
  loginUser: any;
  boolAddNote: boolean = false;

  constructor(
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _alertService: AlertService,
    private _router: Router,
    private _personDetailsService: PersonDetailsService,
    private _orderPipe: OrderPipe
  ) {
    this.loginUser = JSON.parse(localStorage.getItem('currentUser'));
    this.boolAddNote = Utils.checkUserPermission('ADD_NOTES');
  }

  ngOnInit() {
   if (this.personScratchpadHistory) {
    setTimeout(() => {
      this.getScratchpad();
    }, 1000);
    }
    if (this.insuredPersonId) {
      this.getScratchpadHistory();
    }
  }

  /**
    Description: Function to get scratchpad details
    @parameters: No
    @Return: No
  */
  getScratchpad() {
    if (this.insuredPersonId) {
      this.boolStatus = false;
    }
    if (this.personScratchpadHistory) {
    const formData: any = [];
          this.personScratchpadHistory['scratchpadHistory'].forEach(item => {
            let arrScratchData: any = [];
            arrScratchData = {
              'noteText': item.noteText,
              'dateCreated': item.dateCreated,
              'createdBy': item.createdBy,
              'businessUnit': item.businessUnit,
              'policyNumber': item.policyNumber
            };
            formData.push(arrScratchData);
          });
          this.scratchpad = this._orderPipe.transform(formData, 'dateCreated');
        }
  }

  customComparator(itemA, itemB) {
    return itemA < itemB ? 1 : -1;
}
getScratchpadHistory() {
  if (this.insuredPersonId) {
    this.boolStatus = false;
  }
  const objIdPerson = JSON.parse(localStorage.getItem('personIdDetails'));
  if (objIdPerson) {
    this._searchInsuredPersonService.getCurrentPersonDetails(objIdPerson).subscribe(response => {
      const formData: any = [];
      if (Utils.chkResponseSuccess(response)) {
        response['body']['scratchpadHistory'].forEach(item => {
          let arrScratchData: any = [];
          arrScratchData = {
            'noteText': item.noteText,
            'dateCreated': item.dateCreated,
            'createdBy': item.createdBy,
            'businessUnit': item.businessUnit,
            'policyNumber': item.policyNumber
          };
          formData.push(arrScratchData);
        });
        this.scratchpad = this._orderPipe.transform(formData, 'dateCreated');
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
  addScratchpad() {
    const objPersonIdDetails = JSON.parse(localStorage.getItem('personIdDetails'));
    const selectedPersonId = objPersonIdDetails.identityNumber;
    this._personDetailsService.changePolicyData(null);
    localStorage.setItem('insuredPersonId', selectedPersonId);
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.arrTabData = response.tabs;
        if (this.arrTabData) {
          this.arrTabData[0].active = false;
          this.arrTabData[0].show = false;
          this.arrTabData[0].borderBottomClass = false;
          this.arrTabData[0].tabContent = false;
          this.arrTabData[0].formValid = false;

          this.arrTabData[1].active = false;
          this.arrTabData[1].show = false;
          this.arrTabData[1].borderBottomClass = false;
          this.arrTabData[1].tabContent = false;
          this.arrTabData[1].formValid = false;

          this.arrTabData[2].active = false;
          this.arrTabData[2].show = false;
          this.arrTabData[2].borderBottomClass = false;
          this.arrTabData[2].tabContent = false;
          this.arrTabData[2].formValid = false;

          this.arrTabData[3].active = false;
          this.arrTabData[3].show = false;
          this.arrTabData[3].borderBottomClass = false;
          this.arrTabData[3].tabContent = false;
          this.arrTabData[3].formValid = false;

          this.arrTabData[4].active = true;
          this.arrTabData[4].show = true;
          this.arrTabData[4].borderBottomClass = true;
          this.arrTabData[4].tabContent = true;
          this.arrTabData[4].formValid = false;
          this.subActive = false;
          this.noteActive = false;
          this.scratchpadtActive = false;
          this.editEnableActive = false;
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
    this._personDetailsService.changeTabInformation(this.objTabInfo);
    // Set person id in storage to reuse further
    const objPerson = { 'personId': selectedPersonId };
    localStorage.setItem('objExistingperson', JSON.stringify(objPerson));
    let objAction;
      objAction = {
        'action': 'ADD SCRATCHPAD'
      };
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
