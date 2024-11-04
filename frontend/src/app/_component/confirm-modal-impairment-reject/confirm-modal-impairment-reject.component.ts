import {
  Component,
  OnInit,
  ViewChild,
  ElementRef,
  Input,
  EventEmitter,
  Output,
  OnDestroy
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
  selector: "app-confirm-modal-impairment-reject",
  templateUrl: "./confirm-modal-impairment-reject.component.html",
  styleUrls: ["./confirm-modal-impairment-reject.component.css"]
})
export class ConfirmModalImpairmentRejectComponent implements OnInit , OnDestroy {
  confirmImpRejectForm: FormGroup;
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
  ) {}

  ngOnInit() {
    this.confirmImpRejectForm = this.fb.group({
      reason: ["", Validators.required]
    });
  }

  confirmImpairmentReject(data) {
    let updateReferenceData = {
      action: "REJECT",
      // tslint:disable-next-line:max-line-length
      notificationID:
        this.referenceData.notificationID ||
        this.referenceData.new.notificationID,
      transType: this.referenceData.transType,
      actionRemark: data.reason
    };
    this.submitted = true;
    if (this.confirmImpRejectForm.invalid) {
      return;
    }
    // this.spinner.show();
    const reason = this.confirmImpRejectForm.controls.reason.value;
    this.authoriseService.approveImpairments(updateReferenceData).subscribe(
      response => {
        if (Utils.chkResponseSuccess(response)) {
          // this.spinner.hide();
          this.confirmImpRejectForm.setValue({ reason: "" });
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
    this.confirmImpRejectForm.setValue({ reason: "" });
    this.closeBtn.nativeElement.click();
  }
  handleServiceError(error) {
    // Handle service error here
    const strMsg: any = Utils.chkResponseSuccess(error);
    this.alertService.error(strMsg);
  }
  ngOnDestroy() {
    console.log('Add Person destroy...');
    this.confirmImpRejectForm.setValue({ reason: "" });
  }
}
