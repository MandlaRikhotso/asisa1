import { Component, OnInit, Input, Output, EventEmitter } from "@angular/core";
import { FormBuilder, FormGroup, FormArray, Validators } from "@angular/forms";
import {
  SearchInsuredPersonService,
  TabserviceService,
  PersonDetailsService,
  AlertService,
  ConfirmModalService
} from "../_services";
import { NgxSpinnerService } from "ngx-spinner";
import { first } from "rxjs/operators";
import {
  Impairmentbenefit,
  ClaimCategory,
  Impairment,
  ClaimStatus,
  PaymentMethod,
  CauseOfEvents
} from "../_models";
import { Utils } from "../utils";

@Component({
  selector: "app-add-person-claim",
  templateUrl: "./add-person-claim.component.html",
  styleUrls: ["./add-person-claim.component.css"]
})
export class AddPersonClaimComponent implements OnInit {
  claimForm: FormGroup;
  claimTypes: Impairmentbenefit[] = [];
  categoryIds: ClaimCategory[] = [];
  categoryIdArray: any = [];
  claimReasons: Impairment[] = [];
  claimStatus: ClaimStatus[] = [];
  payMethods: PaymentMethod[] = [];
  causeOfEvents: CauseOfEvents[] = [];
  claimTypeSetting = {};
  claimCategorySetting = {};
  claimReasonSetting = {};
  claimStatusSetting = {};
  payMethodSetting = {};
  causeOfEventSetting = {};
  @Input() insuredPersondetails: any;
  @Output() formReady = new EventEmitter<FormGroup>();
  arrTabData: any = [];
  subActive: boolean = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  editEnableActive: boolean = false;
  objTabInfo: any;
  objPerson: any;
  objPersonInfo: any;
  cliamInfo: any;
  claimData: any;
  selectedPersonId: any;
  selectedClaimId: any;
  seltabName: boolean = true;
  policyArray: any = [];
  flag: boolean = false;
  flagR: boolean = true;
  flagShow: boolean = false;
  flagFirst: boolean = false;
  flagSecond: boolean = false;
  addPolicyFlag = true;
  count: number = 0;
  datePickerConfig: any;
  constructor(
    private _fb: FormBuilder,
    private _personDetailsService: PersonDetailsService,
    private _alertService: AlertService,
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _spinner: NgxSpinnerService
  ) {
    this.datePickerConfig = Object.assign(
      {},
      {
        maxDate: new Date(),
        dateInputFormat: "DD/MM/YYYY"
      }
    );
  }

  ngOnInit() {
    // this._spinner.show();
    this.objPersonInfo = JSON.parse(localStorage.getItem("objExistingperson"));
    if (this.objPersonInfo != null || "") {
      this.selectedPersonId = this.objPersonInfo.personId
        ? this.objPersonInfo.personId
        : "";
      this.selectedClaimId = this.objPersonInfo.claimId
        ? this.objPersonInfo.claimId
        : "";
    } else {
      this.selectedPersonId = "";
      this.selectedClaimId = "";
    }
    // this.claimForm['value']['policies'][i][claimType][0][description]
    this.claimForm = this._fb.group({
      policies: this._fb.array([this.initPolicy()])
    });
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.arrTabData = [];
        this.arrTabData = response.tabs;
      }
    });
    this.getClaimTypes();
    this.getCategoryIds();
    this.getClaimReasons();
    this.getClaimStatus();
    this.getPayMethods();
    this.getCauseOfEvents();
    this.claimTypeSetting = {
      // badgeShowLimit: 1,
      singleSelection: true,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      showCheckbox: false,
      classes: "myclass custom-class",
      disabled: this.isSelectReadOnly(),
      enableSearchFilter: true
    };

    this.claimCategorySetting = {
      singleSelection: true,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      showCheckbox: false,
      // badgeShowLimit: 1,
      classes: "myclass custom-class",
      disabled: this.isSelectReadOnly(),
      enableSearchFilter: true
    };

    this.causeOfEventSetting = {
      singleSelection: true,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      showCheckbox: false,
      classes: "myclass custom-class",
      enableSearchFilter: true
    };

    this.claimReasonSetting = {
      badgeShowLimit: 1,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      enableSearchFilter: true,
      classes: "myclass custom-class"
    };

    this.claimStatusSetting = {
      singleSelection: true,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      showCheckbox: false,
      classes: "myclass custom-class",
      enableSearchFilter: true
    };

    this.payMethodSetting = {
      singleSelection: true,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      showCheckbox: false,
      classes: "myclass custom-class",
      enableSearchFilter: true
    };
    this.formReady.emit(this.claimForm);
    this.getExistingPersonDetails();
    // this._spinner.hide();
  }
  /**
    Description: Function to fetch existing person details for add/edit claim
    @parameters: No
    @Return: No
  */
  getExistingPersonDetails() {
    const pgName = localStorage.getItem("pageName");
    // let objPersonInfo = JSON.parse(localStorage.getItem('objExistingperson'));

    if (pgName === "Enquiry") {
      this.objPersonInfo = null;
      localStorage.setItem("objExistingperson", null);
    }

    if (this.objPersonInfo) {
      // this.selectedPersonId = objPersonInfo.personId;
      // this.selectedClaimId = objPersonInfo.claimId ? objPersonInfo.claimId : '';
      this.seltabName = this.objPersonInfo.seltabName;
      if (this.selectedPersonId) {
        const objSelPerson = JSON.parse(
          localStorage.getItem("personIdDetails")
        );
        this._searchInsuredPersonService
          .getCurrentPersonDetails(objSelPerson)
          .subscribe(
            response => {
              if (Utils.chkResponseSuccess(response)) {
                this.objPerson = Utils.setInsuredPersonInfo(
                  response["body"]["insuredPerson"],
                  true
                );
                localStorage.setItem(
                  "exitingPersonInfo",
                  JSON.stringify(this.objPerson)
                );
                // Set person information
                this._personDetailsService.changePersonInfo(this.objPerson);
              }
            },
            error => {
              this.handleServiceError(error);
            }
          );
      }
      localStorage.setItem("selectedClaimId", "");
      if (this.selectedClaimId) {
        localStorage.setItem("selectedClaimId", this.selectedClaimId);
        localStorage.setItem(
          "notificationID",
          this.objPersonInfo.notificationID
        );

        const arrClaimType: any = [];
        const arrEventCause: any = [];
        const arrClaimStatus: any = [];
        const arrPaymentMethod: any = [];

        const objSelPerson = JSON.parse(
          localStorage.getItem("personIdDetails")
        );
        this._personDetailsService
          .getCurrentPersonCliamDetails(objSelPerson)
          .subscribe(
            response => {
              if (Utils.chkResponseSuccess(response)) {
                const responseData = response["body"]["claimHistory"];
                responseData.forEach(item => {
                  if (this.selectedClaimId === item.notifiableClaimID) {
                    this.cliamInfo = item;
                    localStorage.setItem(
                      "claimDetails",
                      JSON.stringify(this.cliamInfo)
                    );
                  }
                });

                if (this.cliamInfo) {
                  this.claimForm.controls["policies"]["controls"][0].patchValue(
                    {
                      policyNumber: this.cliamInfo.policyNumber,
                      // tslint:disable-next-line:max-line-length
                      dateOfEvent:
                        Utils.dtFormat(this.cliamInfo.eventDate) ===
                          "Invalid date"
                          ? ""
                          : Utils.dtFormat(this.cliamInfo.eventDate),
                      placeOfDeath: this.cliamInfo.eventDeathPlace,
                      dateCertificateNumber: this.cliamInfo
                        .eventDeathCertificateNo,
                      dha1663Number: this.cliamInfo.dha1663Number
                    }
                  );

                  arrClaimType.push(this.cliamInfo.claimType);
                  this.claimForm.controls["policies"]["controls"][0].patchValue(
                    {
                      claimType: arrClaimType
                    }
                  );

                  this.claimForm.controls["policies"]["controls"][0].patchValue(
                    {
                      categoryId: this.cliamInfo.claimCategory
                    }
                  );

                  if (this.cliamInfo.eventCause !== null) {
                    arrEventCause.push(this.cliamInfo.eventCause);
                  }
                  this.claimForm.controls["policies"]["controls"][0].patchValue(
                    {
                      causeOfEvent: arrEventCause
                    }
                  );

                  this.claimForm.controls["policies"]["controls"][0].patchValue(
                    {
                      claimReason: this.cliamInfo.claimReason
                    }
                  );
                  if (this.cliamInfo.claimStatus !== null) {
                    arrClaimStatus.push(this.cliamInfo.claimStatus);
                  }
                  // arrClaimStatus.push(this.cliamInfo.claimStatus);
                  this.claimForm.controls["policies"]["controls"][0].patchValue(
                    {
                      claimStatus: arrClaimStatus
                    }
                  );
                  if (this.cliamInfo.paymentMethod !== null) {
                    arrPaymentMethod.push(this.cliamInfo.paymentMethod);
                  }
                  // arrPaymentMethod.push(this.cliamInfo.paymentMethod);
                  this.claimForm.controls["policies"]["controls"][0].patchValue(
                    {
                      payMethod: arrPaymentMethod
                    }
                  );
                }
                let policyObject;
                policyObject = {
                  policyNumber: this.claimForm.value.policies[0].policyNumber,
                  description: this.claimForm.value.policies[0].claimType[0].description
                }
                this._personDetailsService.changeEditPolicyData(policyObject);
                this.flagShow = false;
                this.validateData();
              }
            },
            error => {
              this.handleServiceError(error);
            }
          );
      }
    }
    const arrPayMethod = [
      {
        code: "10",
        description: "EFT"
      }
    ];
    this.claimForm.controls["policies"]["controls"][0].patchValue({
      payMethod: arrPayMethod
    });
    this.clearNoteScratchForm();
  }

  /**
    Description: Function to initialize form validations
    @parameters: No
    @Return: No
  */
  initPolicy() {
    if (this.selectedClaimId) {
      return this._fb.group({
        policyNumber: ["", Validators.required],
        claimType: ["", Validators.required],
        categoryId: ["", Validators.required],
        dateOfEvent: ["", Validators.required],
        causeOfEvent: [""],
        placeOfDeath: [""],
        dateCertificateNumber: [""],
        dha1663Number: [""],
        claimReason: [""],
        claimStatus: [""],
        payMethod: [""],
        editReason: [""]
      });
    }
    if (!this.selectedClaimId) {
      return this._fb.group({
        policyNumber: ["", Validators.required],
        claimType: ["", Validators.required],
        categoryId: ["", Validators.required],
        dateOfEvent: ["", Validators.required],
        causeOfEvent: [""],
        placeOfDeath: [""],
        dateCertificateNumber: [""],
        dha1663Number: [""],
        claimReason: [""],
        claimStatus: [""],
        payMethod: [""],
        editReason: [""]
      });
    }
  }

  /**
    Description: Function to validate form data
    @parameters: No
    @Return: No
  */
  validateData() {
    const PageAction = JSON.parse(localStorage.getItem("pageAction"));
    const PageACtione = PageAction.action;
    if (this.claimForm.valid) {
      this.addPolicyFlag = false;
      if (PageACtione === "EDIT CLAIM") {
        this.claimData = JSON.parse(localStorage.getItem("claimDetails"));
        this.selectedClaimId = localStorage.getItem("selectedClaimId");
        let dateOfEvent: any;
        let dateFlag: any;
        let typee: any;
        let deathplace: any;
        let eventDate: any;
        let deathcertno: any;
        let dha1663Number: any;
        let reason: any;
        let note: any = null;
        let scratchpad: any = null;
        typee = typeof this.claimForm.value.policies[0].dateOfEvent;
        if (typee === "string") {
          dateOfEvent = Utils.convertDate(
            this.claimForm.value.policies[0].dateOfEvent
          );
        } else {
          dateOfEvent = Utils.dtFormat(
            this.claimForm.value.policies[0].dateOfEvent
          );
        }
        eventDate = Utils.dtFormat(this.cliamInfo.eventDate);
        if (eventDate !== dateOfEvent) {
          dateFlag = true;
        } else {
          dateFlag = false;
        }
        // tslint:disable-next-line:max-line-length
        deathplace =
          this.claimForm.value.policies[0].placeOfDeath === "" ||
            this.claimForm.value.policies[0].placeOfDeath === null ||
            this.claimForm.value.policies[0].placeOfDeath === undefined
            ? null
            : this.claimForm.value.policies[0].placeOfDeath;
        // tslint:disable-next-line:max-line-length
        deathcertno =
          this.claimForm.value.policies[0].dateCertificateNumber === "" ||
            this.claimForm.value.policies[0].dateCertificateNumber === null ||
            this.claimForm.value.policies[0].dateCertificateNumber === undefined
            ? null
            : this.claimForm.value.policies[0].dateCertificateNumber;
        // tslint:disable-next-line:max-line-length
        dha1663Number =
          this.claimForm.value.policies[0].dha1663Number === "" ||
            this.claimForm.value.policies[0].dha1663Number === null ||
            this.claimForm.value.policies[0].dha1663Number === undefined
            ? null
            : this.claimForm.value.policies[0].dha1663Number;
        reason =
          this.claimForm.value.policies[0].editReason === "" ||
            this.claimForm.value.policies[0].editReason === "" ||
            this.claimForm.value.policies[0].editReason === undefined
            ? null
            : this.claimForm.value.policies[0].editReason;
        note =
          this.insuredPersondetails.note.notes[0].note === null ||
            this.insuredPersondetails.note.notes[0].note === ""
            ? null
            : this.insuredPersondetails.note.notes[0].note;
        scratchpad =
          this.insuredPersondetails.scratch.scratchpads[0].comment === "" ||
            // tslint:disable-next-line:max-line-length
            this.insuredPersondetails.scratch.scratchpads[0].comment === null
            ? null
            : this.insuredPersondetails.scratch.scratchpads[0].comment;
        if (
          this.cliamInfo.claimType.description === "RISK/DEATH BENEFIT" ||
          this.cliamInfo.claimType.description === "FUNERAL POLICY" ||
          this.cliamInfo.claimType.description === "OTHER"
        ) {
          if (
            (dateFlag === true && reason !== null) ||
            (this.cliamInfo.eventDeathPlace !== deathplace &&
              reason !== null) ||
            (this.cliamInfo.eventDeathCertificateNo !== deathcertno &&
              reason !== null) ||
            (this.cliamInfo.dha1663Number !== dha1663Number &&
              reason !== null) ||
            note !== null ||
            scratchpad !== null
          ) {
            this._personDetailsService.getTabInformation.subscribe(response => {
              if (response) {
                this.arrTabData = response.tabs;
                if (this.arrTabData) {
                  this.arrTabData[3].active = true;
                  this.arrTabData[4].active = true;
                  response.submitActive = true;
                  response.noteActive = true;
                  response.scratchpadtActive = true;
                  response.editEnableActive = false;
                }
              }
            });
          }
          if (
            dateFlag === false &&
            this.cliamInfo.eventDeathPlace === deathplace &&
            this.cliamInfo.eventDeathCertificateNo === deathcertno &&
            this.cliamInfo.dha1663Number === dha1663Number &&
            note === null &&
            scratchpad === null
          ) {
            this._personDetailsService.getTabInformation.subscribe(response => {
              if (response) {
                this.arrTabData = response.tabs;
                if (this.arrTabData) {
                  this.arrTabData[3].active = true;
                  this.arrTabData[4].active = true;
                  response.submitActive = false;
                  response.noteActive = false;
                  response.scratchpadtActive = false;
                  response.editEnableActive = false;
                }
              }
            });
          }
        } else if (this.cliamInfo.claimType.description === "RETRENCHMENT") {
          if (
            (dateFlag === true && reason !== null) ||
            note !== null ||
            scratchpad !== null
          ) {
            this._personDetailsService.getTabInformation.subscribe(response => {
              if (response) {
                this.arrTabData = response.tabs;
                if (this.arrTabData) {
                  this.arrTabData[3].active = true;
                  this.arrTabData[4].active = true;
                  response.submitActive = true;
                  response.noteActive = true;
                  response.scratchpadtActive = true;
                  response.editEnableActive = false;
                }
              }
            });
          }
          if (dateFlag === false && note === null && scratchpad === null) {
            this._personDetailsService.getTabInformation.subscribe(response => {
              if (response) {
                this.arrTabData = response.tabs;
                if (this.arrTabData) {
                  this.arrTabData[3].active = true;
                  this.arrTabData[4].active = true;
                  response.submitActive = false;
                  response.noteActive = false;
                  response.scratchpadtActive = false;
                  response.editEnableActive = false;
                }
              }
            });
          }
        } else {
          if (
            (dateFlag === true && reason !== null) ||
            note !== null ||
            scratchpad !== null
          ) {
            this._personDetailsService.getTabInformation.subscribe(response => {
              if (response) {
                this.arrTabData = response.tabs;
                if (this.arrTabData) {
                  this.arrTabData[3].active = true;
                  this.arrTabData[4].active = true;
                  response.submitActive = true;
                  response.noteActive = true;
                  response.scratchpadtActive = true;
                  response.editEnableActive = false;
                }
              }
            });
          }
          if (dateFlag === false && note === null && scratchpad === null) {
            this._personDetailsService.getTabInformation.subscribe(response => {
              if (response) {
                this.arrTabData = response.tabs;
                if (this.arrTabData) {
                  this.arrTabData[3].active = true;
                  this.arrTabData[4].active = true;
                  response.submitActive = false;
                  response.noteActive = false;
                  response.scratchpadtActive = false;
                  response.editEnableActive = false;
                }
              }
            });
          }
        }
      } else if (PageACtione === "ADD CLAIM") {
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              this.arrTabData[2].formValid = true;
              this.arrTabData[3].active = true;
              this.arrTabData[4].active = true;
              response.submitActive = true;
              response.noteActive = true;
              response.scratchpadtActive = true;
              response.editEnableActive = false;
            }
          }
        });
        if (!this.selectedClaimId) {
          this.setClaimPolicyData(this.claimForm.value.policies);
        }
      }
    } else {
      this.addPolicyFlag = true;
      if (PageACtione === "EDIT CLAIM") {
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              this.arrTabData[3].active = true;
              this.arrTabData[4].active = true;
              response.submitActive = false;
              response.noteActive = false;
              response.scratchpadtActive = false;
              response.editEnableActive = false;
            }
          }
        });
      } else if (PageACtione === "ADD CLAIM") {
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            response.submitActive = false;
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              this.arrTabData[2].formValid = false;
              if (
                this.arrTabData[0].active === true &&
                this.arrTabData[0].show === true &&
                this.arrTabData[0].formValid === true
              ) {
                this.arrTabData[3].active = false;
                this.arrTabData[4].active = true;
                response.submitActive = true;
                response.noteActive = true;
                response.scratchpadtActive = true;
                response.editEnableActive = false;
              } else {
                this.arrTabData[3].active = false;
                this.arrTabData[4].active = false;
                response.submitActive = false;
                response.noteActive = false;
                response.scratchpadtActive = false;
                response.editEnableActive = false;
              }
            }
          }
        });
      }
    }
    this.objTabInfo = {
      tabs: this.arrTabData,
      submitActive: this.subActive,
      noteActive: this.noteActive,
      scratchpadtActive: this.scratchpadtActive,
      editEnableActive: this.editEnableActive,
      personId: ""
    };
    this._personDetailsService.changeTabInformation(this.objTabInfo);
  }

  /**
    Description: Function to set claim policy data
    @parameters: No
    @Return: No
  */
  setClaimPolicyData(formData) {
    // Set Policy Data
    let obj;
    this.policyArray = [];
    formData.forEach((item, index) => {
      if (item.policyNumber !== "" || item.policyNumber !== null) {
        obj = {
          policyNumber: item.policyNumber,
          description: item.claimType[0].description
        };
        this.policyArray.push(obj);
        this._personDetailsService.changePolicyData(this.policyArray);
      }
    });
  }

  /**
    Description: Function to add multiple policies
    @parameters: No
    @Return: No
  */
  addClaimPolicy() {
    this.count = 0;
    const control = <FormArray>this.claimForm.controls["policies"];
    control.push(this.initPolicy());
    this.validateData();
  }
  /**
    Description: Function to add multiple policies
    @parameters: No
    @Return: No
  */
  addClaimType() {
    this.count = 0;
    let item = "";
    let count = 0;
    this.claimForm.value.policies.forEach(function (element) {
      item = element.policyNumber;
      count++;
    });
    const control = <FormArray>this.claimForm.controls["policies"];
    control.push(this.initPolicy());
    const formArray = this.claimForm.get("policies") as FormArray;
    formArray.at(count)["controls"]["policyNumber"].patchValue(item);
    this.validateData();
  }

  /**
    Description: Function to remove policy
    @parameters: No
    @Return: No
  */
  removeClaimPolicy(i) {
    const control = <FormArray>this.claimForm.controls["policies"];
    control.removeAt(i);
    localStorage.setItem("removePolicyAt", i);
    this.setClaimPolicyData(this.claimForm.value.policies);
    this.validateData();
  }

  /**
    Description: Function to fetch claim types
    @parameters: No
    @Return: No
  */
  getClaimTypes(): void {
    this._personDetailsService
      .getAllImpairmentBenefits()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.claimTypes = response["body"]["policyTypes"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
    Description: Function to fetch category id
    @parameters: No
    @Return: No
  */
  getCategoryIds(): void {
    this._personDetailsService
      .getCategoryIds()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.categoryIds = response["body"]["claimCategories"];
            this.categoryIdArray = response["body"]["claimCategories"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
    Description: Function to fetch claim reasons
    @parameters: No
    @Return: No
  */
  getClaimReasons(): void {
    this._personDetailsService
      .getAllImpairments()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.claimReasons = response["body"]["impairmentCodes"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
    Description: Function to fetch claim status
    @parameters: No
    @Return: No
  */
  getClaimStatus(): void {
    this._personDetailsService
      .getClaimStatus()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.claimStatus = response["body"]["claimStatuses"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
    Description: Function to fetch claim payment methods
    @parameters: No
    @Return: No
  */
  getPayMethods(): void {
    this._personDetailsService
      .getPayMethods()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.payMethods = response["body"]["paymentMethods"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
   Description: Function to fetch claim cause of events
   @parameters: No
   @Return: No
 */
  getCauseOfEvents(): void {
    this._personDetailsService
      .getCauseOfEvents()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.causeOfEvents = response["body"]["claimCauses"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
    Description: Function to clear form fields
    @parameters: No
    @Return: No
  */
  clearData() {
    this.claimForm.reset();
    this.validateData();
    this.claimForm = this._fb.group({
      policies: this._fb.array([this.initPolicy()])
    });
    localStorage.setItem("objClaimData", null);
    this.clearNoteScratchForm();
  }

  /**
    Description: Function to clear notes and scratchpad
    @parameters: No
    @Return: No
  */
  clearNoteScratchForm() {
    // Set clear information
    const objClearData = {
      addPeronForm: false,
      addImpairmentForm: false,
      addClaimForm: false,
      addNoteForm: true,
      addScratchpadForm: true
    };
    // Set person information
    this._personDetailsService.changeClearData(objClearData);
  }

  /**
    Description: Function to disabled form dropdown
    @parameters: No
    @Return: No
  */
  isSelectReadOnly(): boolean {
    const objData = JSON.parse(localStorage.getItem("objExistingperson"));
    if (objData) {
      const id = objData.claimId ? objData.claimId : "";
      if (id) {
        return true;
      } else {
        return false;
      }
    }
  }

  /**
    Description: Function to disabled form inputs
    @parameters: No
    @Return: No
  */
  isReadOnly(): boolean {
    if (this.selectedClaimId) {
      return true;
    } else {
      return false;
    }
  }

  /**
    Description: Function to handle validations on form fields
    @parameters: No
    @Return: No
  */
  formControlValueChanged(policy, i) {
    this.categoryIds = [];
    for (const items of this.categoryIdArray) {
      if (policy.value.claimType[0].claimType === items.claimType) {
        this.categoryIds.push(items);
      }
    }
    this.flag = true;
    this.flagR = true;
    this.claimForm.value.policies.forEach((item, index) => {
      if (item.policyNumber === policy.value.policyNumber && index === i) {
        item.claimType.forEach((itemClaimType, indexClaimType) => {
          if (
            itemClaimType.description === "RISK/DEATH BENEFIT" ||
            itemClaimType.description === "FUNERAL POLICY" ||
            itemClaimType.description === "OTHER"
          ) {
            this.flag = true;
            const formArray = this.claimForm.get("policies") as FormArray;
            formArray
              .at(index)
            ["controls"]["placeOfDeath"].setValidators(Validators.required);
            formArray
              .at(index)
            ["controls"]["causeOfEvent"].setValidators(Validators.required);
            formArray
              .at(index)
            ["controls"]["placeOfDeath"].updateValueAndValidity();
            formArray
              .at(index)
            ["controls"]["causeOfEvent"].updateValueAndValidity();
            formArray.at(index)["controls"]["claimReason"].clearValidators();
            formArray
              .at(index)
            ["controls"]["claimReason"].updateValueAndValidity();
          } else if (itemClaimType.description === "RETRENCHMENT") {
            this.flagR = false;
            this.flag = false;
            const formArray = this.claimForm.get("policies") as FormArray;
            formArray.at(index)["controls"]["placeOfDeath"].clearValidators();
            formArray.at(index)["controls"]["causeOfEvent"].clearValidators();
            formArray
              .at(index)
            ["controls"]["placeOfDeath"].updateValueAndValidity();
            formArray
              .at(index)
            ["controls"]["causeOfEvent"].updateValueAndValidity();
            formArray.at(index)["controls"]["claimReason"].clearValidators();
            formArray
              .at(index)
            ["controls"]["claimReason"].updateValueAndValidity();
          } else {
            this.flag = false;
            const formArray = this.claimForm.get("policies") as FormArray;
            formArray.at(index)["controls"]["placeOfDeath"].clearValidators();
            formArray.at(index)["controls"]["causeOfEvent"].clearValidators();
            formArray
              .at(index)
            ["controls"]["placeOfDeath"].updateValueAndValidity();
            formArray
              .at(index)
            ["controls"]["causeOfEvent"].updateValueAndValidity();
            formArray
              .at(index)
            ["controls"]["claimReason"].setValidators(Validators.required);
            formArray
              .at(index)
            ["controls"]["claimReason"].updateValueAndValidity();
          }
        });
      }
    });
  }
  formControlValidChanged() {
    if (this.selectedClaimId) {
      if (this.flagShow === true) {
        this.validateData();
      }
    }
  }
  formControlValidAllowed() {
    if (this.selectedClaimId) {
      const formArray = this.claimForm.get("policies") as FormArray;
      formArray.at(0)["controls"]["editReason"].setValidators(null);
      formArray.at(0)["controls"]["editReason"].updateValueAndValidity();
      this.validateData();
    }
  }
  formControlFieldChanged() {
    if (this.selectedClaimId) {
      this.claimData = JSON.parse(localStorage.getItem("claimDetails"));
      this.selectedClaimId = localStorage.getItem("selectedClaimId");
      let dateOfEvent: any;
      let dateFlag: any;
      let typee: any;
      let deathplace: any;
      let eventDate: any;
      let deathcertno: any;
      let dha1663Number: any;
      typee = typeof this.claimForm.value.policies[0].dateOfEvent;
      if (typee === "string") {
        dateOfEvent = Utils.convertDate(
          this.claimForm.value.policies[0].dateOfEvent
        );
      } else {
        dateOfEvent = Utils.dtFormat(
          this.claimForm.value.policies[0].dateOfEvent
        );
      }
      eventDate = Utils.dtFormat(this.cliamInfo.eventDate);
      if (eventDate !== dateOfEvent) {
        dateFlag = true;
      } else {
        dateFlag = false;
      }
      // tslint:disable-next-line:max-line-length
      deathplace =
        this.claimForm.value.policies[0].placeOfDeath === "" ||
          this.claimForm.value.policies[0].placeOfDeath === null ||
          this.claimForm.value.policies[0].placeOfDeath === undefined
          ? null
          : this.claimForm.value.policies[0].placeOfDeath;
      // tslint:disable-next-line:max-line-length
      deathcertno =
        this.claimForm.value.policies[0].dateCertificateNumber === "" ||
          this.claimForm.value.policies[0].dateCertificateNumber === null ||
          this.claimForm.value.policies[0].dateCertificateNumber === undefined
          ? null
          : this.claimForm.value.policies[0].dateCertificateNumber;
      // tslint:disable-next-line:max-line-length
      dha1663Number =
        this.claimForm.value.policies[0].dha1663Number === "" ||
          this.claimForm.value.policies[0].dha1663Number === null ||
          this.claimForm.value.policies[0].dha1663Number === undefined
          ? null
          : this.claimForm.value.policies[0].dha1663Number;
      if (
        this.cliamInfo.claimType.description === "RISK/DEATH BENEFIT" ||
        this.cliamInfo.claimType.description === "FUNERAL POLICY" ||
        this.cliamInfo.claimType.description === "OTHER"
      ) {
        if (
          dateFlag === true ||
          this.cliamInfo.eventDeathPlace !== deathplace ||
          this.cliamInfo.eventDeathCertificateNo !== deathcertno ||
          this.cliamInfo.dha1663Number !== dha1663Number
        ) {
          this.flagShow = true;
          const formArray = this.claimForm.get("policies") as FormArray;
          formArray
            .at(0)
          ["controls"]["editReason"].setValidators(Validators.required);
          formArray.at(0)["controls"]["editReason"].updateValueAndValidity();
          this.validateData();
        }
        if (
          dateFlag === false &&
          this.cliamInfo.eventDeathPlace === deathplace &&
          this.cliamInfo.eventDeathCertificateNo === deathcertno &&
          this.cliamInfo.dha1663Number === dha1663Number
        ) {
          this.flagShow = false;
          const formArray = this.claimForm.get("policies") as FormArray;
          formArray.at(0)["controls"]["editReason"].setValidators(null);
          formArray.at(0)["controls"]["editReason"].updateValueAndValidity();
          this.validateData();
        }
      } else if (this.cliamInfo.claimType.description === "RETRENCHMENT") {
        if (dateFlag === true) {
          this.flagShow = true;
          const formArray = this.claimForm.get("policies") as FormArray;
          formArray
            .at(0)
          ["controls"]["editReason"].setValidators(Validators.required);
          formArray.at(0)["controls"]["editReason"].updateValueAndValidity();
          this.validateData();
        }
        if (dateFlag === false) {
          this.flagShow = false;
          const formArray = this.claimForm.get("policies") as FormArray;
          formArray.at(0)["controls"]["editReason"].setValidators(null);
          formArray.at(0)["controls"]["editReason"].updateValueAndValidity();
          this.validateData();
        }
      } else {
        if (dateFlag === true) {
          this.flagShow = true;
          const formArray = this.claimForm.get("policies") as FormArray;
          formArray
            .at(0)
          ["controls"]["editReason"].setValidators(Validators.required);
          formArray.at(0)["controls"]["editReason"].updateValueAndValidity();
          this.validateData();
        }
        if (dateFlag === false) {
          this.flagShow = false;
          const formArray = this.claimForm.get("policies") as FormArray;
          formArray.at(0)["controls"]["editReason"].setValidators(null);
          formArray.at(0)["controls"]["editReason"].updateValueAndValidity();
          this.validateData();
        }
      }
    } else {
      this.validateData();
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
  }
}
