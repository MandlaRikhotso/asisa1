import { Component, OnInit, ViewChild, ElementRef, Output, EventEmitter } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ConfirmModalService, AlertService, AuthoriseService } from '../../_services';
import { NgxSpinnerService } from 'ngx-spinner';
import { Utils } from '../../utils';

@Component({
  selector: 'app-confirm-modal-claim',
  templateUrl: './confirm-modal-claim.component.html',
  styleUrls: ['./confirm-modal-claim.component.css']
})
export class ConfirmModalClaimComponent implements OnInit {
  confirmClaimForm: FormGroup;
  objData: any;
  submitted = false;
  count: any;
  deleteStatus: string;
  @ViewChild('closeBtn') closeBtn: ElementRef;
  @Output() claimMsgEvent: EventEmitter<string> = new EventEmitter<string>();


  constructor(
    private fb: FormBuilder,
    private confirmModalService: ConfirmModalService,
    private spinner: NgxSpinnerService,
    private _alertService: AlertService,
    private authoriseService: AuthoriseService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.confirmClaimForm = this.fb.group({
      reason: ['', Validators.required]
    });
    this.confirmModalService.getObj.subscribe(response => this.objData = response);
  }

  confirmClaimDelete(selectedData) {
    this.submitted = true;
    if (this.confirmClaimForm.invalid) {
      return;
    }
    this.spinner.show();
    const objRequest = {
       'dha1663Number': '',
      'claimCategory': {},
      'claimReason': [],
      'claimStatus': {},
      'delete': true,
      'eventCause': {},
      'eventDate': '',
      'eventDeathCertificateNo': '',
      'eventDeathPlace': '',
      'notes': [],
      'notifiableClaimRefID': selectedData.claimId,
      'notificationID': selectedData.notificationID,
      'paymentMethod': {},
      'updateReason': this.confirmClaimForm.controls.reason.value
    };

    this.confirmModalService.delClaim(objRequest).subscribe(response => {
      this.spinner.hide();
      this.confirmClaimForm.setValue({ reason: '' });
      this.deleteStatus = 'delete_claim_success';
      this.claimMsgEvent.emit(this.deleteStatus);
      this._alertService.success('Claim deleted successfully', true);
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
    this.confirmClaimForm.setValue({ reason: '' });
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
