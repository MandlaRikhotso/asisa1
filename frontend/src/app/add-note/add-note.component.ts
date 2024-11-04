import { Component, Input, Output, EventEmitter, OnInit } from "@angular/core";
import { FormBuilder, FormGroup, FormArray, Validators } from "@angular/forms";
import {
  SearchInsuredPersonService,
  PersonDetailsService,
  AlertService
} from "../_services";
import { NgxSpinnerService } from "ngx-spinner";
import { Utils } from "../utils";

@Component({
  selector: "app-add-note",
  templateUrl: "./add-note.component.html",
  styleUrls: ["./add-note.component.css"]
})
export class AddNoteComponent implements OnInit {
  @Output() formReady = new EventEmitter<FormGroup>();
  @Input() insuredPersondetails: any;
  addNoteForm: FormGroup;
  policyArray: any = [];
  objTabInfo: any;
  objFormInfoe: any;
  selectedPersonId: any;
  selectedClaimId: any;
  objPerson: any;
  impData: any;
  claimData: any;
  selectedImpId: any;
  subActive: boolean = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  editEnableActive: boolean = false;
  arrTabData: any;
  pageAction: any;
  flag: boolean = false;
  constructor(
    private _fb: FormBuilder,
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _personDetailsService: PersonDetailsService,
    private _alertService: AlertService,
    private _spinner: NgxSpinnerService
  ) { }

  ngOnInit() {
    this.pageAction = JSON.parse(localStorage.getItem("pageAction"));
    // this._spinner.show();
    this.addNoteForm = this._fb.group({
      notes: this._fb.array([this.initNote()])
    });
    this.formReady.emit(this.addNoteForm);
    this.getExistingPersonDetails();
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.arrTabData = [];
        this.arrTabData = response.tabs;
      }
    });

    this._personDetailsService.getClearData.subscribe(response => {
      if (response) {
        if (response.addNoteForm) {
          this.addNoteForm.reset();
          this.policyArray = [];
          localStorage.setItem("objImpairmentData", null);
        }
      }
    });
    this.pageAction = JSON.parse(localStorage.getItem("pageAction"));
    if (this.pageAction.action === "EDIT IMPAIRMENT" || this.pageAction.action === "EDIT CLAIM") {
      this._personDetailsService.getEditPolicyData.subscribe(response => {
        if (response) {
          this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
            if (
              itemArray.policyNumber === "" ||
              itemArray.policyNumber === null
            ) {
              const control = <FormArray>this.addNoteForm.controls["notes"];
              control.removeAt(itemindex);
            }
          });
          const control = <FormArray>this.addNoteForm.controls["notes"];
          control.push(this.initNote());
          this.addNoteForm.controls["notes"]["controls"][0].patchValue({
            policyNumber: response.policyNumber !== "" ? response.policyNumber : "",
            description: response.description !== "" ? response.description : ""
          });
        }
      });
    } else {
      this._personDetailsService.getPolicyData.subscribe(response => {
        this.policyArray = [];
        if (response) {
          this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
            if (
              itemArray.policyNumber === "" ||
              itemArray.policyNumber === null
            ) {
              const control = <FormArray>this.addNoteForm.controls["notes"];
              control.removeAt(itemindex);
            }
          });
          this.removeFormElements();
          this.policyArray = response;
          this.flag = true;
          this.pageAction = JSON.parse(localStorage.getItem("pageAction"));
          this.policyArray.forEach((item, index) => {
            const control = <FormArray>this.addNoteForm.controls["notes"];
            control.push(this.initNote());
            this.addNoteForm.controls["notes"]["controls"][index].patchValue({
              policyNumber: item.policyNumber !== "" ? item.policyNumber : "",
              description: item.description !== "" ? item.description : ""
            });
          });
          this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
            if (
              itemArray.policyNumber === "" ||
              itemArray.policyNumber === null
            ) {
              const control = <FormArray>this.addNoteForm.controls["notes"];
              control.removeAt(itemindex);
            }
          });
          this.validateData();
        }
      });
    }
    // this._spinner.hide();
  }

  /**
    Description: Function to remove form elements from form
    @parameters: No
    @Return: No
  */
  removeFormElements() {
    const id = JSON.parse(localStorage.getItem("removePolicyAt"));
    if (id) {
      const control = <FormArray>this.addNoteForm.controls["notes"];
      control.removeAt(id);
    }
  }

  /**
    Description: Function to initialize form validations
    @parameters: No
    @Return: No
  */
  initNote() {
    return this._fb.group({
      policyNumber: [""],
      description: [""],
      note: ["", Validators.maxLength(800)]
    });
  }

  /**
    Description: Function to fetch existing person details for add/edit notes
    @parameters: No
    @Return: No
  */
  getExistingPersonDetails() {
    const objPersonInfo = JSON.parse(localStorage.getItem("objExistingperson"));
    if (objPersonInfo) {
      this.selectedPersonId = objPersonInfo.personId;
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
    }
  }

  /**
    Description: Function to validate form fileds
    @parameters: No
    @Return: No
  */
  validateData() {
    this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
      if (itemArray.policyNumber === "" || null) {
        const control = <FormArray>this.addNoteForm.controls["notes"];
        control.removeAt(itemindex);
      }
    });
    // const selectedClaimId  = JSON.parse(localStorage.getItem('selectedClaimId'));
    const PageAction = JSON.parse(localStorage.getItem("pageAction"));
    const PageACtione = PageAction.action;
    if (PageACtione === "EDIT IMPAIRMENT") {
      this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
        if (itemArray.policyNumber === "" || null) {
          const control = <FormArray>this.addNoteForm.controls["notes"];
          control.removeAt(itemindex);
        }
      });
      this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
        this.impData = JSON.parse(localStorage.getItem("impDetails"));
        this.selectedImpId = localStorage.getItem("selectedImpId");
        let timeSignal: any = null;
        let readings: any;
        let reads: any;
        let reason: any;
        let note: any;
        let scratchpad: any;
        // tslint:disable-next-line:max-line-length
        readings =
          this.insuredPersondetails.impairments.policies[0].impairments[0]
            .reading +
          "/" +
          this.insuredPersondetails.impairments.policies[0].impairments[0]
            .reading1;
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
          this.insuredPersondetails.impairments.policies[0].impairments[0]
            .timeSignal === "" ||
            this.insuredPersondetails.impairments.policies[0].impairments[0]
              .timeSignal === null
            ? null
            : this.insuredPersondetails.impairments.policies[0].impairments[0]
              .timeSignal;
        reason =
          this.insuredPersondetails.impairments.policies[0].editReason === "" ||
            this.insuredPersondetails.impairments.policies[0].editReason === "" ||
            this.insuredPersondetails.impairments.policies[0].editReason ===
            undefined
            ? null
            : this.insuredPersondetails.impairments.policies[0].editReason;
        note =
          this.addNoteForm.value.notes[0].note === null ||
            this.addNoteForm.value.notes[0].note === ""
            ? null
            : this.addNoteForm.value.notes[0].note;
        scratchpad =
          this.insuredPersondetails.scratch.scratchpads[0].comment === "" ||
            // tslint:disable-next-line:max-line-length
            this.insuredPersondetails.scratch.scratchpads[0].comment === null
            ? null
            : this.insuredPersondetails.scratch.scratchpads[0].comment;
        if (
          (this.impData.timeSignal !== timeSignal && reason !== null) ||
          ((reads !== readingsStr) !== timeSignal && reason !== null) ||
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
        } else {
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
      });
    } else if (PageACtione === "EDIT CLAIM") {
      this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
        if (itemArray.policyNumber === "" || null) {
          const control = <FormArray>this.addNoteForm.controls["notes"];
          control.removeAt(itemindex);
        }
      });
      this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
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
        let note: any;
        let scratchpad: any;
        typee = typeof this.insuredPersondetails.claims.policies[0].dateOfEvent;
        if (typee === "string") {
          dateOfEvent = Utils.convertDate(
            this.insuredPersondetails.claims.policies[0].dateOfEvent
          );
        } else {
          dateOfEvent = Utils.dtFormat(
            this.insuredPersondetails.claims.policies[0].dateOfEvent
          );
        }
        eventDate = Utils.dtFormat(this.claimData.eventDate);
        if (eventDate !== dateOfEvent) {
          dateFlag = true;
        } else {
          dateFlag = false;
        }
        // tslint:disable-next-line:max-line-length
        deathplace =
          this.insuredPersondetails.claims.policies[0].placeOfDeath === "" ||
            this.insuredPersondetails.claims.policies[0].placeOfDeath === null ||
            this.insuredPersondetails.claims.policies[0].placeOfDeath ===
            undefined
            ? null
            : this.insuredPersondetails.claims.policies[0].placeOfDeath;
        // tslint:disable-next-line:max-line-length
        deathcertno =
          this.insuredPersondetails.claims.policies[0].dateCertificateNumber ===
            "" ||
            this.insuredPersondetails.claims.policies[0].dateCertificateNumber ===
            null ||
            this.insuredPersondetails.claims.policies[0].dateCertificateNumber ===
            undefined
            ? null
            : this.insuredPersondetails.claims.policies[0]
              .dateCertificateNumber;
        // tslint:disable-next-line:max-line-length
        dha1663Number =
          this.insuredPersondetails.claims.policies[0].dha1663Number === "" ||
            this.insuredPersondetails.claims.policies[0].dha1663Number === null ||
            this.insuredPersondetails.claims.policies[0].dha1663Number ===
            undefined
            ? null
            : this.insuredPersondetails.claims.policies[0].dha1663Number;

        reason =
          this.insuredPersondetails.claims.policies[0].editReason === "" ||
            this.insuredPersondetails.claims.policies[0].editReason === "" ||
            this.insuredPersondetails.claims.policies[0].editReason === undefined
            ? null
            : this.insuredPersondetails.claims.policies[0].editReason;
        note =
          this.addNoteForm.value.notes[0].note === null ||
            this.addNoteForm.value.notes[0].note === ""
            ? null
            : this.addNoteForm.value.notes[0].note;
        scratchpad =
          this.insuredPersondetails.scratch.scratchpads[0].comment === "" ||
            // tslint:disable-next-line:max-line-length
            this.insuredPersondetails.scratch.scratchpads[0].comment === null
            ? null
            : this.insuredPersondetails.scratch.scratchpads[0].comment;
        if (
          this.claimData.claimType.description === "RISK/DEATH BENEFIT" ||
          this.claimData.claimType.description === "FUNERAL POLICY" ||
          this.claimData.claimType.description === "OTHER"
        ) {
          if (
            (dateFlag === true && reason !== null) ||
            (this.claimData.eventDeathPlace !== deathplace &&
              reason !== null) ||
            (this.claimData.eventDeathCertificateNo !== deathcertno &&
              reason !== null) ||
            (this.claimData.dha1663Number !== dha1663Number &&
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
          } else if (
            dateFlag === false &&
            this.claimData.eventDeathPlace === deathplace &&
            this.claimData.eventDeathCertificateNo === deathcertno &&
            this.claimData.dha1663Number === dha1663Number &&
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
          } else {
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
        } else if (this.claimData.claimType.description === "RETRENCHMENT") {
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
          } else if (
            dateFlag === false &&
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
          } else {
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
          } else if (
            dateFlag === false &&
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
          } else {
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
      });
    } else if (
      PageACtione === "ADD IMPAIRMENT" ||
      PageACtione === "ADD CLAIM"
    ) {
      this.addNoteForm.value.notes.forEach((itemArray, itemindex) => {
        if (itemArray.policyNumber === "" || null) {
          const control = <FormArray>this.addNoteForm.controls["notes"];
          control.removeAt(itemindex);
        }
      });
      this._personDetailsService.getTabInformation.subscribe(response => {
        if (response) {
          this.arrTabData = response.tabs;
          // if (this.arrTabData) {
          //     response.submitActive = true;
          // }
        }
      });
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
    Description: Function to clear form fields
    @parameters: No
    @Return: No
  */
  clearData() {
    this.addNoteForm.value.notes.forEach((item, index) => {
      this.addNoteForm.controls["notes"]["controls"][index].patchValue({
        note: ""
      });
    });
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
