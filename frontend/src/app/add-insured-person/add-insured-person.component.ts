import {
  Component,
  Output,
  EventEmitter,
  OnInit,
  OnDestroy
} from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {
  SearchInsuredPersonService,
  PersonDetailsService,
  AlertService
} from "../_services";
import { NgxSpinnerService } from "ngx-spinner";
import { first } from "rxjs/operators";
import { Utils } from "../utils";

@Component({
  selector: "app-add-insured-person",
  templateUrl: "./add-insured-person.component.html",
  styleUrls: ["./add-insured-person.component.css"]
})
export class AddInsuredPersonComponent implements OnInit, OnDestroy {
  addPersonForm: FormGroup;
  serachInsuredPersonForm: FormGroup;
  objTabInfo: any;
  arrTabData: any = [];
  objPerson: any;
  userAction: any;
  subActive: boolean = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  editEnableActive: boolean = false;
  arrPersonData: any = [];
  personData: any = [];
  isValidNumber: any = true;
  isExist: any = false;
  strCustomerNotFound: any = false;
  idtypedropdownsettings: {};
  genderdropdownsettings: {};
  titlesdropdownsettings: {};
  idTypes: any;
  pretitles: any;
  genders: any;
  @Output() formReady = new EventEmitter<FormGroup>();
  objCurrentUser: any;
  strUserRole: string;
  datePickerConfig: any;
  clearDataflag: boolean = false;
  constructor(
    private _fb: FormBuilder,
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _personDetailsService: PersonDetailsService,
    private _alertService: AlertService,
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
    localStorage.setItem("personIdDetails", null);
    localStorage.setItem("objExistingperson", null);
    this.getGenders();
    this.getPretitles();
    this.getIdTypes();

    this.addPersonForm = this._fb.group({
      idType: ["", Validators.required],
      idNumber: ["", Validators.required],
      dateOfBirth: ["", Validators.required],
      gender: ["", Validators.required],
      pretitle: ["", Validators.required],
      surname: ["", Validators.required],
      givenName1: ["", Validators.required],
      givenName2: [""],
      givenName3: [""],
      addressLine1: [""],
      addressLine2: [""],
      addressLine3: [""],
      postalCode: [""]
    });

    this.serachInsuredPersonForm = this._fb.group({
      gsIdNumber: ["", Validators.required]
    });

    this.idtypedropdownsettings = {
      singleSelection: true,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      enableSearchFilter: true,
      showCheckbox: false,
      classes: "myclass custom-class"
    };

    this.genderdropdownsettings = {
      singleSelection: true,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      enableSearchFilter: true,
      showCheckbox: false,
      classes: "myclass custom-class"
    };

    this.titlesdropdownsettings = {
      singleSelection: true,
      text: "Select",
      labelKey: "description",
      primaryKey: "code",
      enableSearchFilter: true,
      showCheckbox: false,
      classes: "myclass custom-class"
    };

    this.formReady.emit(this.addPersonForm);
    // Set person information
    const objPerson = null;
    this._personDetailsService.changePersonInfo(objPerson);
    // Get search person information
    setTimeout(() => {
      this._searchInsuredPersonService.getPersonSearchInfo.subscribe(
        response => {
          if (response) {
            if (
              response.identityType.code === "4" &&
              response.identityNumber !== ""
            ) {
              const idTypeNameArray = [
                {
                  code: response ? response.identityType.code : "",
                  description: response ? response.identityType.description : ""
                }
              ];
              // get first 6 digits as a valid date
              // tslint:disable-next-line:max-line-length
              const tempDate = new Date(
                response.identityNumber.substring(0, 2),
                response.identityNumber.substring(2, 4) - 1,
                response.identityNumber.substring(4, 6)
              );

              const id_date = tempDate.getDate();
              const id_month = tempDate.getMonth() + 1;
              const id_year = tempDate.getFullYear();

              const DatefullDate = id_date + "/" + id_month + "/" + id_year;

              // get the gender
              const genderCode = response.identityNumber.substring(6, 10);
              const genderArray = [
                {
                  code: "2",
                  description: "FEMALE"
                }
              ];
              const genderArrayOther = [
                {
                  code: "1",
                  description: "MALE"
                }
              ];
              // tslint:disable-next-line:radix
              const genderDeriveArray =
                // tslint:disable-next-line:radix
                parseInt(genderCode) < 5000 ? genderArray : genderArrayOther;

              this.addPersonForm.patchValue({
                idNumber: response ? response.identityNumber : ""
              });
              this.addPersonForm.patchValue({
                idType: idTypeNameArray ? idTypeNameArray : ""
              });
              this.addPersonForm.patchValue({
                gender: genderDeriveArray
              });
            } else {
              this.addPersonForm.patchValue({
                dateOfBirth: response.dateOfBirth ? response.dateOfBirth : ""
              });
              this.addPersonForm.patchValue({
                surname: response.surname ? response.surname : ""
              });
            }
          }
        }
      );
    }, 1000);
    // this._spinner.hide();
    this.formControlValueChanged();
  }
  formControlValueChanged() {
    const idNumber = this.addPersonForm.get("idNumber");
    this.addPersonForm
      .get("idType")
      .valueChanges.subscribe((idtype: string) => {
        if (idtype[0]["code"] === "4") {
          idNumber.setValidators([Validators.minLength(13)]);
        } else {
          idNumber.clearValidators();
        }
        idNumber.updateValueAndValidity();
        this.validateData();
      });
  }
  /**
   Description: Function to validate ID Number on Search button
   @parameters: ID
   @Return: No
 */
  isValidSAID(id) {
    let i,
      c,
      result,
      even: any = "",
      sum: any = 0;
    const check = +id.slice(-1);

    if (id.length !== 13) {
      return false;
    }
    id = id.substr(0, id.length - 1);
    for (i = 0; (c = id.charAt(i)); i += 2) {
      sum += +c;
      even += id.charAt(i + 1);
    }
    even = "" + even * 2;
    for (i = 0; (c = even.charAt(i)); i++) {
      sum += +c;
    }
    result = 10 - Number(String(sum).substr(1, 1));
    return result === check;
  }
  /**
    Description: Function to validate form fields and set person info and tabs accordingly
    @parameters: No
    @Return: No
  */
  validateData() {
    if (this.addPersonForm.value.idType[0]["code"] !== "4") {
      this.isValidNumber = true;
      this.isExist = false;
    }
    if (
      this.addPersonForm.value.idType[0]["code"] === "4" &&
      this.addPersonForm.value.idNumber !== "" && this.addPersonForm.value.idNumber !== null
    ) {
      this.isValidNumber = this.isValidSAID(
        this.addPersonForm.value.idNumber
      );
    }
    if (this.addPersonForm.value.idType[0]["code"] === "4" && (this.isValidNumber === true)) {
      const objPersonEnquiry = {
        dateOfBirth: "",
        identityNumber: this.addPersonForm.value.idNumber,
        identityType: {
          code: this.addPersonForm.value.idType[0]["code"],
          description: this.addPersonForm.value.idType[0][
            "description"
          ]
        },
        surname: ""
      };
      if (this.clearDataflag) {
        objPersonEnquiry.identityNumber = "";
      }

      if (objPersonEnquiry.identityNumber === "" || objPersonEnquiry.identityNumber == null) {
        this.isExist = false;
      } else {
        // Call service to exist the insured persons
        this._searchInsuredPersonService
          .getExistingPerson(objPersonEnquiry)
          .subscribe(
            response => {
              if (Utils.chkResponseSuccess(response)) {
                this.isExist = response["body"];

              }
            },
            error => {
              this.handleServiceError(error);
            }
          );
      }
    }
    this.strCustomerNotFound = false;
    if (
      this.addPersonForm.valid &&
      (this.isValidNumber === true) &&
      (this.isExist === false)
    ) {
      this._personDetailsService.getTabInformation.subscribe(response => {
        if (response) {
          this.userAction = JSON.parse(localStorage.getItem("pageAction"));
          this.arrTabData = response.tabs;
          if (this.arrTabData) {
            this.arrTabData[0].formValid = true;
            if (this.userAction.action === "ADD IMPAIRMENT") {
              this.arrTabData[1].active = true;
            }
            if (this.userAction.action === "ADD CLAIM") {
              this.arrTabData[2].active = true;
            }
            this.arrTabData[4].active = true;
            // tslint:disable-next-line:max-line-length
            if (
              this.arrTabData[1].formValid === true ||
              this.arrTabData[2].formValid === true ||
              this.arrTabData[3].formValid === true ||
              this.arrTabData[4].formValid === true
            ) {
              this.arrTabData[3].active = true;
            }
            response.submitActive = true;
            response.noteActive = true;
            response.scratchpadtActive = true;
            response.editEnableActive = false;
          }
        }
      });
      // Set person information
      localStorage.setItem("pageName", "");
      this.objPerson = Utils.setInsuredPersonInfo(this.addPersonForm.controls);
      this._personDetailsService.changePersonInfo(this.objPerson);
    } else {
      this._personDetailsService.getTabInformation.subscribe(response => {
        if (response) {
          this.arrTabData = response.tabs;
          if (this.arrTabData) {
            this.arrTabData[0].active = true;
            this.arrTabData[0].formValid = false;
            this.arrTabData[1].active = false;
            this.arrTabData[2].active = false;
            this.arrTabData[4].active = false;
            this.arrTabData[3].active = false;
            response.submitActive = false;
            response.noteActive = false;
            response.scratchpadtActive = false;
            response.editEnableActive = false;
          }
        }
      });
      // Set person information
      localStorage.setItem("pageName", "Enquiry");
      const objPerson = null;
      this._personDetailsService.changePersonInfo(objPerson);
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
    Description: Function to retrive person details from GSC client
    @parameters: No
    @Return: No
  */
  retrivePersonDetails(data) {
    // this._spinner.show();
    const objData = {
      PartySearchCriteria: {
        partyID: data.gsIdNumber
      }
    };
    this._personDetailsService
      .getPersonDetailsBYCustomerId(objData)
      .pipe(first())
      .subscribe(response => {
        if (response) {
          // this.strCustomerNotFound = false;
          this.personData = response ? response["Person"] : "";
          if (this.personData.length > 0) {
            this.strCustomerNotFound = false;
            const genderArray = [];
            const preTitleArray = [];
            for (const items of this.genders) {
              if (items.description === this.personData[0].gender) {
                genderArray.push(items);
              }
            }
            for (const items of this.pretitles) {
              if (
                items.description ===
                this.personData[0].PersonName[0].prefixTitles
              ) {
                preTitleArray.push(items);
              }
            }
            const idTypeId = [
              {
                code: "4",
                description: "ID NUMBER"
              }
            ];
            const idTypeOther = [
              {
                code: "21477483647",
                description: "OTHER"
              }
            ];
            const idTypeNameArray =
              this.personData[0].PartyRegistration[3].typeName ===
                "IDENTITY CARD"
                ? idTypeId
                : idTypeOther;
            this.addPersonForm.patchValue({
              idType: idTypeNameArray,
              // tslint:disable-next-line:max-line-length
              idNumber: this.personData[0].PartyRegistration[3]
                .externalReference
                ? this.personData[0].PartyRegistration[3].externalReference
                : "",
              dateOfBirth: Utils.reformatDate(this.personData[0].birthDate),
              gender: genderArray,
              pretitle: preTitleArray,
              surname: this.personData[0].PersonName[0].lastName,
              givenName1: this.personData[0].PersonName[0].firstName
                ? this.personData[0].PersonName[0].firstName
                : "",
              givenName2: this.personData[0].PersonName[0].givenName2
                ? this.personData[0].PersonName[0].givenName2
                : "",
              givenName3: this.personData[0].PersonName[0].givenName3
                ? this.personData[0].PersonName[0].givenName3
                : "",
              // tslint:disable-next-line:max-line-length
              addressLine1: this.personData[0].ContactPreference[1]
                .ContactPointInPreference.LinePostalAddress.addressLines
                ? this.personData[0].ContactPreference[1]
                  .ContactPointInPreference.LinePostalAddress.addressLines
                : "",
              addressLine2: this.personData[0].PersonName[0].addressLine2
                ? this.personData[0].PersonName[0].addressLine2
                : "",
              addressLine3: this.personData[0].PersonName[0].addressLine3
                ? this.personData[0].PersonName[0].addressLine3
                : "",
              // tslint:disable-next-line:max-line-length
              postalCode: this.personData[0].ContactPreference[1]
                .ContactPointInPreference.LinePostalAddress.postalCode
                ? this.personData[0].ContactPreference[1]
                  .ContactPointInPreference.LinePostalAddress.postalCode
                : ""
            });
          }
          if (this.personData.length === 0) {
            this.strCustomerNotFound = true;
          }
        } else {
          this.strCustomerNotFound = true;
          // this.clearData();
        }
        localStorage.setItem("pageName", "History");
        this.validateData();
      });
  }

  /**
    Description: Function to fetch genders to prepopulate in dropdown
    @parameters: No
    @Return: No
  */
  getGenders(): void {
    this._personDetailsService.getAllGenders().subscribe(
      response => {
        if (Utils.chkResponseSuccess(response)) {
          this.genders = response["body"]["genders"];
        }
      },
      error => {
        this.handleServiceError(error);
      }
    );
  }

  /**
    Description: Function to fetch titles to prepopulate in dropdown
    @parameters: No
    @Return: No
  */
  getPretitles(): void {
    this._personDetailsService.getAllPretitles().subscribe(
      response => {
        if (Utils.chkResponseSuccess(response)) {
          this.pretitles = response["body"]["titles"];
        }
      },
      error => {
        this.handleServiceError(error);
      }
    );
  }

  /**
    Description: Function to fetch types to prepopulate in dropdown
    @parameters: No
    @Return: No
  */
  getIdTypes(): void {
    this._searchInsuredPersonService.getIdTypes().subscribe(
      response => {
        if (Utils.chkResponseSuccess(response)) {
          this.idTypes = response["body"]["identityTypes"];
        }
      },
      error => {
        this.handleServiceError(error);
      }
    );
  }

  /**
    Description: Function to clear all form fields
    @parameters: No
    @Return: No
  */
  clearData() {
    this.clearDataflag = true;
    this.addPersonForm.reset();
    let defaultVal: any;
    defaultVal = [{ "code": "4", "description": "ID NUMBER" }];
    this.addPersonForm.controls['idType'].setValue(defaultVal);
    this.validateData();
    this.clearDataflag = false;
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
    console.log("Add Person destroy...");
    this._searchInsuredPersonService.changePersonSearchInfo(null);
  }
}
