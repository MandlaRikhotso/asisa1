import {
  Component,
  Output,
  Input,
  EventEmitter,
  OnInit,
  OnDestroy
} from "@angular/core";
import { FormBuilder, FormGroup, FormArray, Validators } from "@angular/forms";
import {
  SearchInsuredPersonService,
  PersonDetailsService,
  AlertService
} from "../_services";
import { NgxSpinnerService } from "ngx-spinner";
import { first } from "rxjs/operators";
import {
  Impairment,
  Impairmentbenefit,
  Symbol,
  SpecialInvestigate
} from "../_models";
import { Utils } from "../utils";

@Component({
  selector: "app-add-person-impairmaint",
  templateUrl: "./add-person-impairmaint.component.html",
  styleUrls: ["./add-person-impairmaint.component.css"]
})
export class AddPersonImpairmaintComponent implements OnInit, OnDestroy {
  addImpairmentForm: FormGroup;
  @Input() insuredPersondetails: any;
  objTabInfo: any;
  arrTabData: any = [];
  impData: any;
  selectedImpId: any;
  subActive = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  editEnableActive: boolean = false;
  editNoteActive: boolean = false;
  editSctratchpadActive: boolean = false;
  objFormInfo: any;
  objFormInfoe: any;
  objFormInfoee: any;
  impairments: Impairment[] = [];
  impairmentData: any = [];
  impairmentItems: any = [];
  impairmentBenefits: Impairmentbenefit[] = [];
  symbols: Symbol[] = [];
  @Output() formReady = new EventEmitter<FormGroup>();
  specialInvestigates: SpecialInvestigate[] = [];
  impairmentSettings = {};
  impairmentBenefitsettings = {};
  symbolDropdownSettings = {};
  splInvestigationsettings = {};
  timeSignalSettings = {};
  objPerson: any;
  objPersonInfo: any;
  selectedPersonId: any;
  selectedimpairmentId: any;
  impairmentInfo: any;
  boolStatus = true;
  policyArray: any = [];
  flagShow: boolean = false;
  addPolicyFlag = true;
  constructor(
    private _fb: FormBuilder,
    private _personDetailsService: PersonDetailsService,
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _alertService: AlertService,
    private _spinner: NgxSpinnerService
  ) {}

  ngOnInit() {
    this._spinner.show();
    //  this._spinner.show();
    setTimeout(() => {
      this._spinner.hide();
    }, 3000);
    localStorage.setItem("objClearData", null);
    this.objPersonInfo = JSON.parse(localStorage.getItem("objExistingperson"));
    if (this.objPersonInfo != null || "") {
      this.selectedPersonId = this.objPersonInfo.personId
        ? this.objPersonInfo.personId
        : "";
      this.selectedimpairmentId = this.objPersonInfo.impairmentId
        ? this.objPersonInfo.impairmentId
        : "";
    } else {
      this.selectedPersonId = "";
      this.selectedimpairmentId = "";
    }
    this.addImpairmentForm = this._fb.group({
      policies: this._fb.array([this.initPolicy()])
    });
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.arrTabData = [];
        this.arrTabData = response.tabs;
      }
    });
    this.getImpairments();
    this.getImpairmentBenefits();
    this.getSymbols();
    this.getSpecialInvestigates();

    this.impairmentSettings = {
      singleSelection: true,
      text: "Select Impairment",
      labelKey: "codes",
      primaryKey: "code",
      enableSearchFilter: true,
      classes: "myclass custom-class",
      disabled: this.isSelectReadOnly(),
      showCheckbox: false
    };
    this.impairmentBenefitsettings = {
      singleSelection: true,
      text: "Select Benefits",
      labelKey: "description",
      primaryKey: "code",
      enableSearchFilter: true,
      // badgeShowLimit: 2,
      classes: "myclass custom-class",
      disabled: this.isSelectReadOnly(),
      showCheckbox: false
    };

    this.symbolDropdownSettings = {
      text: "Select Symbol",
      labelKey: "description",
      primaryKey: "code",
      badgeShowLimit: 1,
      classes: "myclass custom-class",
      disabled: this.isSelectReadOnly(),
      enableSearchFilter: true
    };

    this.splInvestigationsettings = {
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      badgeShowLimit: 1,
      classes: "myclass custom-class",
      disabled: this.isSelectReadOnly(),
      enableSearchFilter: true
    };
    this.formReady.emit(this.addImpairmentForm);
    this.getExistingPersonDetails();
    // this._spinner.hide();
  }

  /**
    Description: Function to fetch existing person details for add/edit impairment
    @parameters: No
    @Return: No
  */
  getExistingPersonDetails() {
    // this._spinner.show();
    const pgName = localStorage.getItem("pageName");
    // let objPersonInfo = JSON.parse(localStorage.getItem('objExistingperson'));

    if (pgName === "Enquiry") {
      this.objPersonInfo = null;
      localStorage.setItem("objExistingperson", null);
    }
    if (this.objPersonInfo) {
      // this.selectedPersonId = objPersonInfo.personId ? objPersonInfo.personId : '';
      // this.selectedimpairmentId = objPersonInfo.impairmentId ? objPersonInfo.impairmentId : '';
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
      localStorage.setItem("selectedImpId", "");
      if (this.selectedimpairmentId) {
        localStorage.setItem("selectedImpId", this.selectedimpairmentId);
        localStorage.setItem(
          "notificationID",
          this.objPersonInfo.notificationID
        );
        this.boolStatus = false;
        this.impairmentInformation(this.selectedimpairmentId);
      }
    }

    const arrPolicyBenefit = [
      {
        code: "1",
        description: "RISK/DEATH BENEFIT"
      }
    ];
    this.addImpairmentForm.controls["policies"]["controls"][0].patchValue({
      policyBenefit: arrPolicyBenefit
    });
    // this._spinner.hide();
    this.clearNoteScratchForm();
  }

  /**
    Description: Function to initialize form validation
    @parameters: No
    @Return: No
  */
  updateImpairmentList(i) {
    this.impairmentData = JSON.parse(localStorage.getItem("impairmentItems"));
    const formArray = this.addImpairmentForm.get("policies") as FormArray;

    for (let l = 0; l < formArray.value[i].impairments.length; l++) {
      for (let k = 0; k < this.impairmentData.length; k++) {
        if (
          this.impairmentData[k].code ==
          formArray.value[i].impairments[l].impairment[0].code
        ) {
          this.impairmentData.splice(k, 1);
          break;
        }
      }
    }
  }

  initPolicy() {
    if (this.selectedimpairmentId) {
      return this._fb.group({
        policyNumber: ["", Validators.required],
        policyBenefit: ["", Validators.required],
        editReason: [""],
        impairments: this._fb.array([this.initImpairment()])
      });
    }
    if (!this.selectedimpairmentId) {
      return this._fb.group({
        policyNumber: ["", Validators.required],
        policyBenefit: ["", Validators.required],
        editReason: [""],
        impairments: this._fb.array([this.initImpairment()])
      });
    }
  }

  /**
    Description: Function to initialize form validation for impairment
    @parameters: No
    @Return: No
  */
  initImpairment() {
    return this._fb.group({
      impairment: [[], Validators.required],
      timeSignal: ["", Validators.required],
      reading: [""],
      reading1: [""],
      specialInvestigate: [[]],
      symbol: [[]]
    });
  }

  /**
    Description: Function to add multiple policies
    @parameters: No
    @Return: No
  */
  addPolicy() {
    this.impairmentData = [];
    this.impairmentData = JSON.parse(localStorage.getItem("impairmentItems"));
    const control = <FormArray>this.addImpairmentForm.controls["policies"];
    control.push(this.initPolicy());
    this.validateData();
  }

  /**
    Description: Function to add policy benefit
    @parameters: No
    @Return: No
  */
  addPolicyBenefit() {
    let item = "";
    let count = 0;
    this.addImpairmentForm.value.policies.forEach(function(element) {
      item = element.policyNumber;
      count++;
    });
    const control = <FormArray>this.addImpairmentForm.controls["policies"];
    control.push(this.initPolicy());
    const formArray = this.addImpairmentForm.get("policies") as FormArray;
    formArray.at(count)["controls"]["policyNumber"].patchValue(item);
    this.validateData();
  }

  /**
    Description: Function to add multiple imapairments
    @parameters: No
    @Return: No
  */
  addimpairments(policy): void {
    if (policy.controls.impairments.controls.length <= 8) {
      setTimeout(() => {
        const control = <FormArray>policy.controls.impairments;
        control.push(this.initImpairment());
        this.validateData();
      }, 400);
    }
  }

  /**
    Description: Function to remove policies
    @parameters: No
    @Return: No
  */
  removePolicy(i) {
    this.impairmentData = [];
    this.impairmentData = JSON.parse(localStorage.getItem("impairmentItems"));
    const control = <FormArray>this.addImpairmentForm.controls["policies"];
    control.removeAt(i);
    localStorage.setItem("removePolicyAt", i);
    // this.validateData();
    this.setImpPolicyData(this.addImpairmentForm.value.policies);
    this.validateData();
  }

  /**
    Description: Function to remove imapirments
    @parameters: No
    @Return: No
  */
  removeImpairment(policy, j, i) {
    setTimeout(() => {
      const control = <FormArray>policy.controls.impairments;
      control.removeAt(j);
      this.validateData();
      this.updateImpairmentList(i);
    }, 400);
  }

  /**
    Description: Function to validate form data
    @parameters: No
    @Return: No
  */
  validateData() {
    const PageAction = JSON.parse(localStorage.getItem("pageAction"));
    const PageACtione = PageAction.action;
    if (this.addImpairmentForm.valid) {
      this.addPolicyFlag = false;
      if (PageACtione === "EDIT IMPAIRMENT") {
        this.subActive = true;
        let reason: any = null;
        this.impData = JSON.parse(localStorage.getItem("impDetails"));
        this.selectedImpId = localStorage.getItem("selectedImpId");
        let timeSignal: any = null;
        let readings: any = "";
        let reads: any;
        let note: any = null;
        let scratchpad: any = null;
        // tslint:disable-next-line:max-line-length
        // tslint:disable-next-line:max-line-length
        readings =
          this.addImpairmentForm.value.policies[0].impairments[0].reading +
          "/" +
          this.addImpairmentForm.value.policies[0].impairments[0].reading1;
        let str = readings;
        let readingsStr = str.replace("undefined", "");
        // tslint:disable-next-line:max-line-length
        reads =
          this.impData.readings === "/" ||
          this.impData.readings === "/undefined" ||
          this.impData.readings === "undefined/" ||
          this.impData.readings === "undefined/undefined"
            ? "/"
            : this.impData.readings;
        timeSignal =
          this.addImpairmentForm.value.policies[0].impairments[0].timeSignal ===
            "" || this.addImpairmentForm.value.policies[0].timeSignal === null
            ? null
            : this.addImpairmentForm.value.policies[0].impairments[0]
                .timeSignal;
        reason =
          this.addImpairmentForm.value.policies[0].editReason === "" ||
          this.addImpairmentForm.value.policies[0].editReason === "" ||
          this.addImpairmentForm.value.policies[0].editReason === undefined
            ? null
            : this.addImpairmentForm.value.policies[0].editReason;
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
        // tslint:disable-next-line:max-line-length
        if (
          (this.impData.timeSignal !== timeSignal && reason !== null) ||
          (reads !== readingsStr && reason !== null) ||
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
              }
            }
          });
        } else if (
          this.impData.timeSignal === timeSignal &&
          reads === readingsStr &&
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
              }
            }
          });
        }
      } else if (PageACtione === "ADD IMPAIRMENT") {
        this.subActive = true;
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              this.arrTabData[1].formValid = true;
              this.arrTabData[3].active = true;
              this.arrTabData[4].active = true;
              response.submitActive = true;
              response.noteActive = true;
              response.scratchpadtActive = true;
              response.editEnableActive = false;
            }
          }
        });
        this.setImpPolicyData(this.addImpairmentForm.value.policies);
      }
    } else {
      this.addPolicyFlag = true;
      if (PageACtione === "EDIT IMPAIRMENT") {
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
      } else if (PageACtione === "ADD IMPAIRMENT") {
        this._personDetailsService.getTabInformation.subscribe(response => {
          if (response) {
            response.submitActive = false;
            this.arrTabData = response.tabs;
            if (this.arrTabData) {
              this.arrTabData[1].formValid = false;
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
      editNoteActive: this.editNoteActive,
      editSctratchpadActive: this.editSctratchpadActive,
      personId: ""
    };
    this._personDetailsService.changeTabInformation(this.objTabInfo);
  }

  /**
    Description: Function to set impairment policy
    @parameters: No
    @Return: No
  */
  setImpPolicyData(formData) {
    // Set Policy Data
    let obj;
    this.policyArray = [];
    formData.forEach((item, index) => {
      if (
        (item.policyNumber !== "" || null) &&
        (item.policyBenefit[0].description !== "" || null)
      ) {
        obj = {
          policyNumber: item.policyNumber,
          description: item.policyBenefit[0].description
        };
        this.policyArray.push(obj);
        this._personDetailsService.changePolicyData(this.policyArray);
      }
    });
  }

  /**
    Description: Function to fetch imapirments
    @parameters: No
    @Return: No
  */
  getImpairments(): void {
    this._personDetailsService
      .getAllImpairments()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.impairments = response["body"]["impairmentCodes"];
            this.impairments.forEach((item, index) => {
              this.impairments[index]["codes"] =
                item.code + " - " + item.description;
            });
            this.impairmentData = this.impairments;
            localStorage.setItem(
              "impairmentItems",
              JSON.stringify(this.impairmentData)
            );
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
    Description: Function to fetch policy benefits
    @parameters: No
    @Return: No
  */
  getImpairmentBenefits(): void {
    this._personDetailsService
      .getAllImpairmentBenefits()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.impairmentBenefits = response["body"]["policyTypes"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
    Description: Function to fetch symbols
    @parameters: No
    @Return: No
  */
  getSymbols(): void {
    this._personDetailsService
      .getAllSymbols()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.symbols = response["body"]["symbols"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }
  /**
    Description: Function to fetch special investigation
    @parameters: No
    @Return: No
  */
  getSpecialInvestigates(): void {
    this._personDetailsService
      .getAllSpecialInvestigates()
      .pipe(first())
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            this.specialInvestigates = response["body"]["lifeSpecs"];
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
  }

  /**
    Description: Function to prepopulate impairment details to edit
    @parameters: No
    @Return: No
  */
  impairmentInformation(impairmentId) {
    // this._spinner.show();
    const arrImpairment: any = [];
    const arrPolicyBenefit: any = [];
    const objSelPerson = JSON.parse(localStorage.getItem("personIdDetails"));
    localStorage.setItem("impDetails", "null");
    this._personDetailsService
      .getCurrentPersonImpairmentDetails(objSelPerson)
      .subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            const responseData = response["body"]["impairmentHistory"];
            responseData.forEach(item => {
              if (impairmentId === item.notifiableImpairmentID) {
                this.impairmentInfo = item;
                localStorage.setItem(
                  "impDetails",
                  JSON.stringify(this.impairmentInfo)
                );
              }
            });

            if (this.impairmentInfo) {
              this.addImpairmentForm.controls["policies"][
                "controls"
              ][0].patchValue({
                policyNumber: this.impairmentInfo.policyNumber
              });
              arrPolicyBenefit.push(this.impairmentInfo.policyBenefit);
              this.addImpairmentForm.controls["policies"][
                "controls"
              ][0].patchValue({
                policyBenefit: [this.impairmentInfo.policyBenefit]
              });
              if (this.impairmentInfo.readings) {
                const arrReadings = this.impairmentInfo.readings.split("/");
                this.addImpairmentForm.controls["policies"][
                  "controls"
                ][0].controls["impairments"]["controls"][0].patchValue({
                  reading: arrReadings[0],
                  reading1: arrReadings[1]
                });
              }
              this.impairmentInfo.impairment["codes"] =
                this.impairmentInfo.impairment.code +
                " - " +
                this.impairmentInfo.impairment.description;
              arrImpairment.push(this.impairmentInfo.impairment);
              this.addImpairmentForm.controls["policies"][
                "controls"
              ][0].controls["impairments"]["controls"][0].patchValue({
                impairment: [this.impairmentInfo.impairment]
              });
              this.addImpairmentForm.controls["policies"][
                "controls"
              ][0].controls["impairments"]["controls"][0].patchValue({
                timeSignal: this.impairmentInfo.timeSignal
              });

              this.addImpairmentForm.controls["policies"][
                "controls"
              ][0].controls["impairments"]["controls"][0].patchValue({
                specialInvestigate: this.impairmentInfo.specialInvestigation
              });

              this.addImpairmentForm.controls["policies"][
                "controls"
              ][0].controls["impairments"]["controls"][0].patchValue({
                symbol: this.impairmentInfo.symbol
              });
            }
            let policyObject;
            policyObject={
              policyNumber: this.addImpairmentForm.value.policies[0].policyNumber,
              description: this.addImpairmentForm.value.policies[0].policyBenefit[0].description
            }
            this._personDetailsService.changeEditPolicyData(policyObject);
            // this.setImpPolicyData(this.addImpairmentForm.value.policies);
            this.flagShow = false;
            this.validateData();
          }
          // this._spinner.hide();
        },
        error => {
          // this._spinner.hide();
          this.handleServiceError(error);
        }
      );
    // this._spinner.hide();
  }

  formControlValidChanged() {
    if (this.selectedimpairmentId) {
      if (this.flagShow === true) {
        this.validateData();
      }
    }
  }
  formControlFieldChanged() {
    if (this.selectedimpairmentId) {
      this.impData = JSON.parse(localStorage.getItem("impDetails"));
      this.selectedImpId = localStorage.getItem("selectedImpId");
      let timeSignal: any = null;
      let readings: any = null;
      let reads: any;
      readings =
        this.addImpairmentForm.value.policies[0].impairments[0].reading +
        "/" +
        this.addImpairmentForm.value.policies[0].impairments[0].reading1;
      let str = readings;
      let readingsStr = str.replace("undefined", "");
      // tslint:disable-next-line:max-line-length
      reads =
        this.impData.readings === "/" ||
        this.impData.readings === "/undefined" ||
        this.impData.readings === "undefined/" ||
        this.impData.readings === "undefined/undefined"
          ? "/"
          : this.impData.readings;
      timeSignal =
        this.addImpairmentForm.value.policies[0].impairments[0].timeSignal ===
          "" || this.addImpairmentForm.value.policies[0].timeSignal === null
          ? null
          : this.addImpairmentForm.value.policies[0].impairments[0].timeSignal;
      if (this.impData.timeSignal !== timeSignal || reads !== readingsStr) {
        this.flagShow = true;
        const formArray = this.addImpairmentForm.get("policies") as FormArray;
        formArray
          .at(0)
          ["controls"]["editReason"].setValidators(Validators.required);
        formArray.at(0)["controls"]["editReason"].updateValueAndValidity();
        this.validateData();
      } else if (
        this.impData.timeSignal === timeSignal &&
        reads === readingsStr
      ) {
        this.flagShow = false;
        const formArraye = this.addImpairmentForm.get("policies") as FormArray;
        formArraye.at(0)["controls"]["editReason"].setValidators(null);
        formArraye.at(0)["controls"]["editReason"].updateValueAndValidity();
        this.validateData();
      }
    } else {
      this.validateData();
    }
  }

  /**
    Description: Function to clear form fields
    @parameters: No
    @Return: No
  */
  clearData() {
    this.impairmentData = [];
    this.impairmentData = JSON.parse(localStorage.getItem("impairmentItems"));
    this.addImpairmentForm.reset();
    this.addImpairmentForm = this._fb.group({
      policies: this._fb.array([this.initPolicy()])
    });
    // this.validateData();
    localStorage.setItem("objImpairmentData", null);
    this.clearNoteScratchForm();
    this.validateData();
  }

  /**
    Description: Function to clear notes and scratchpad when clear impairment form
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
    Description: Function to disabled dropdown for existing person
    @parameters: No
    @Return: No
  */
  isSelectReadOnly(): boolean {
    // this._spinner.show();
    const objData = JSON.parse(localStorage.getItem("objExistingperson"));
    if (objData) {
      const id = objData.impairmentId ? objData.impairmentId : "";
      if (id) {
        return true;
      } else {
        return false;
      }
    }
    // this._spinner.hide();
  }

  /**
    Description: Function to readonly text fields for existing person
    @parameters: No
    @Return: No
  */
  isReadOnly(): boolean {
    if (this.selectedimpairmentId) {
      return true;
    } else {
      return false;
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

  /**
    Description: Function to destroy services on component destroy
    @parameters: No
    @Return: No
  */
  ngOnDestroy() {
    console.log("Add impairment Person destroy...");
  }
}
