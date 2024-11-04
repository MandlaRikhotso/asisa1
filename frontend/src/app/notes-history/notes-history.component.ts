import { Component, OnInit, Input } from '@angular/core';
import { SearchInsuredPersonService, AlertService } from '../_services';
import { OrderPipe } from 'ngx-order-pipe';
import { Utils } from '../utils';

@Component({
  selector: 'app-notes-history',
  templateUrl: './notes-history.component.html',
  styleUrls: ['./notes-history.component.css']
})

export class NotesHistoryComponent implements OnInit {
  notes: any = [];
  show: boolean = true;
  @Input() insuredPersonId: any;
  @Input() personNotetHistory: any;
  selectedPersonId: any;
  boolStatus: boolean = true;
  order: string = 'dateCreated';
  reverse: boolean = false;

  constructor(
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _alertService: AlertService,
    private _orderPipe: OrderPipe
  ) { }

  ngOnInit() {
    // this.getNotes();
    setTimeout(() => {
      this.getNotes();
    }, 1000);
    if (this.insuredPersonId) {
      this.getNotesHistory();
    }
  }

  /**
    Description: Function to get notes details
    @parameters: No
    @Return: No
  */
  getNotes() {
    if (this.insuredPersonId) {
      this.boolStatus = false;
    }
    if (this.personNotetHistory) {
    const formData: any = [];
          this.personNotetHistory['noteHistory'].forEach(item => {
            let arrNotesData: any = [];
            arrNotesData = {
              'noteText': item.noteText,
              'dateCreated': item.dateCreated,
              'createdBy': item.createdBy,
              'notificationSource': item.notificationSource,
              'policyNumber': item.policyNumber
            };
            formData.push(arrNotesData);
          });
          this.notes = this._orderPipe.transform(formData, 'dateCreated');
        }
        }
  customComparator(itemA, itemB) {
    return itemA < itemB ? 1 : -1;
}
getNotesHistory() {
  if (this.insuredPersonId) {
    this.boolStatus = false;
  }
  const objIdPerson = JSON.parse(localStorage.getItem('personIdDetails'));
  if (objIdPerson) {
    this._searchInsuredPersonService.getCurrentPersonDetails(objIdPerson).subscribe(response => {
      const formData: any = [];
      if (Utils.chkResponseSuccess(response)) {
        response['body']['noteHistory'].forEach(item => {
          let arrNotesData: any = [];
          arrNotesData = {
            'noteText': item.noteText,
            'dateCreated': Utils.dtFormat(item.dateCreated),
            'createdBy': item.createdBy,
            'notificationSource': item.notificationSource,
            'policyNumber': item.policyNumber
          };
          formData.push(arrNotesData);
        });
        this.notes = this._orderPipe.transform(formData, 'dateCreated');
      }
    }, error => {
      this.handleServiceError(error);
    });
  }
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
