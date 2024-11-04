import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  Input,
  EventEmitter,
  Output
} from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {
  ConfirmModalService,
  AlertService,
  AuthoriseService
} from "../../_services";
import { NgxSpinnerService } from "ngx-spinner";
import { Router } from "@angular/router";
import { Utils } from "../../utils";

@Component({
  selector: "app-confirm-modal-claim-reject",
  templateUrl: "./confirm-modal-claim-reject.component.html",
  styleUrls: ["./confirm-modal-claim-reject.component.css"]
})
export class ConfirmModalClaimRejectComponent implements OnInit {
  confirmClaimRejectForm: FormGroup;
  submitted = false;
  rejectStatus: any;
  count: any;
  @ViewChild("closeBtn") closeBtn: ElementRef;
  @Input() referenceData: any;
  @Output() impMsgEvent: EventEmitter<string> = new EventEmitter<string>();
  constructor(
    private fb: FormBuilder,
    private confirmModalService: ConfirmModalService,
    private spinner: NgxSpinnerService,
    private alertService: AlertService,
    private authoriseService: AuthoriseService,
    private _router: Router
  ) { }

  ngOnInit() {
    this.confirmClaimRejectForm = this.fb.group({
      reason: ["", Validators.required]
    });
  }

  confirmClaimReject(data) {
    const updateReferenceData = {
      action: "REJECT",
      notificationID:
        this.referenceData.notificationID ||
        this.referenceData.new.notificationID,
      transType: this.referenceData.transType,
      actionRemark: data.reason
    };
    this.submitted = true;
    if (this.confirmClaimRejectForm.invalid) {
      return;
    }
    this.spinner.show();
    const reason = this.confirmClaimRejectForm.controls.reason.value;
    this.authoriseService.approveClaims(updateReferenceData).subscribe(
      response => {
        if (Utils.chkResponseSuccess(response)) {
          // this.spinner.hide();
          this.confirmClaimRejectForm.setValue({ reason: "" });
          if (response["status"] === 200) {
            this.rejectStatus = "reject_impairment_success";
            this.impMsgEvent.emit(this.rejectStatus);
            this.authoriseService.getAuthoriseCount().subscribe(datacount => {
              this.count = datacount > 0 ? datacount : 0;
              this.authoriseService.changeNotificationCount(this.count);
            });
            if (this.referenceData.notificationID === undefined) {
              localStorage.setItem(
                "successMsg",
                response["body"]["successMessage"]
              );
              this._router.navigate(["/authorise"]);
            } else {
              this.alertService.success(
                response["body"]["successMessage"],
                true
              );
            }
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
    this.closeModal();
  }

  public closeModal(): void {
    this.confirmClaimRejectForm.setValue({ reason: "" });
    this.closeBtn.nativeElement.click();
  }
  handleServiceError(error) {
    // Handle service error here
    const strMsg: any = Utils.chkResponseSuccess(error);
    this.alertService.error(strMsg);
  }
}
