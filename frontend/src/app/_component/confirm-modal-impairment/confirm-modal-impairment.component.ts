import { Component, OnInit, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ConfirmModalService, AlertService, AuthoriseService } from '../../_services';
import { NgxSpinnerService } from 'ngx-spinner';
import { Utils } from '../../utils';

@Component({
  selector: 'app-confirm-modal-impairment',
  templateUrl: './confirm-modal-impairment.component.html',
  styleUrls: ['./confirm-modal-impairment.component.css']
})
export class ConfirmModalImpairmentComponent implements OnInit {
  confirmImpForm: FormGroup;
  objData: any;
  submitted = false;
  count: any;
  deleteStatus: string;
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Output() impMsgEvent: EventEmitter<string> = new EventEmitter<string>();

  constructor(
    private fb: FormBuilder,
    private confirmModalService: ConfirmModalService,
    private spinner: NgxSpinnerService,
    private _alertService: AlertService,
    private authoriseService: AuthoriseService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.confirmImpForm = this.fb.group({
      reason: ['', Validators.required]
    });
    this.confirmModalService.getObj.subscribe(response => this.objData = response);
  }

  confirmImpairmentDelete(selectedData) {
    this.submitted = true;
    if (this.confirmImpForm.invalid) {
      return;
    }
    this.spinner.show();

    const objRequest = {
      'delete': true,
      'impairment': {},
      'notes': [],
      'notifiableImpairmentID': selectedData.impairmentId,
      'notificationID': selectedData.notificationID,
      'readings': '',
      'specialInvestigation': [],
      'symbol': [],
      'timeSignal': '',
      'updateReason': this.confirmImpForm.controls.reason.value
    };

    this.confirmModalService.delImpairment(objRequest).subscribe(response => {
      this.spinner.hide();
      this.confirmImpForm.setValue({ reason: '' });
      this.deleteStatus = 'delete_impairment_success';
      this.impMsgEvent.emit(this.deleteStatus);
      this._alertService.success('Impairment deleted successfully', true);
      this.authoriseService.getAuthoriseCount().subscribe(data => {
        this.count = (data > 0) ? data : 0;
        this.authoriseService.changeNotificationCount(this.count);
      });
    }, error => {
      this.handleServiceError(error);
    });
    this.closeModal();
  }

  public closeModal(): void {
    this.confirmImpForm.setValue({ reason: '' });
    this.closeBtn.nativeElement.click();
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
