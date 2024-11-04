import { Component, OnInit, OnDestroy } from "@angular/core";
import { FormBuilder, FormGroup } from "@angular/forms";
import {
  SearchInsuredPersonService,
  AlertService,
  PersonDetailsService,
  AuthenticationService
} from "../_services";
import { IdTypes } from "../_models";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { Utils } from "../utils";
import * as moment from "moment";
import { OrderPipe } from "ngx-order-pipe";

@Component({
  selector: "app-insured-person-enquiry",
  templateUrl: "./insured-person-enquiry.component.html",
  styleUrls: ["./insured-person-enquiry.component.css"]
})
export class InsuredPersonEnquiryComponent implements OnInit, OnDestroy {
  searchForm: FormGroup;
  searchResultFound: boolean = false;
  searchResultNotFound: boolean = false;
  validNumber: boolean = false;
  isValidNumber: any;
  searchFlag: boolean = false;
  insuredPersons: any = [];
  arrIdTypes: IdTypes[];
  idType: string;
  idNumber: number;
  surname: string;
  dateOfBirth: string;
  arrIds: any = [];
  currentDateTime: any;
  arrIdNumbers: any = [];
  arrIdNumbersLen: any;
  arrSurnames: any = [];
  strIdFields: boolean = false;
  order: string = "dateOfBirth";
  reverse: boolean = false;
  subActive: boolean = false;
  objCurrentUser: any;
  objData: any;
  objTabInfo: any;
  arrTabData: any;
  userAction: any;
  strCurrentUserName: string;
  strCurrentGivenName: string;
  strCurrentSurName: string;
  strUserRole: string;
  boolAddPerson: boolean = false;
  count: any;
  datePickerConfig: any;
  constructor(
    private _fb: FormBuilder,
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _router: Router,
    private _spinner: NgxSpinnerService,
    private _alertService: AlertService,
    private _personDetailsService: PersonDetailsService,
    private _orderPipe: OrderPipe,
    private _authenticationService: AuthenticationService
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
    this.objCurrentUser = JSON.parse(localStorage.getItem("currentUser"));
    this._authenticationService.changeUser(this.objCurrentUser);
    this.strUserRole = this.objCurrentUser['authorities'][0].authority;
    this.strCurrentUserName = this.objCurrentUser["username"];
    this.strCurrentGivenName = this.objCurrentUser["givenName"];
    this.strCurrentSurName = this.objCurrentUser["surname"];
    // Set person information
    this._personDetailsService.changePersonInfo(null);
    localStorage.setItem("objExistingperson", null);
    localStorage.setItem("personIdDetails", null);
    localStorage.setItem("pageName", "Enquiry");
    localStorage.setItem("redirectTo", "Enquiry");
    this._spinner.show();
    this.buildForm();
    // Fetch id types from service to prepopulate
    this.getIdTypes();

    if (Utils.checkUserPermission("ADD_PERSON")) {
      this.boolAddPerson = true;
    }
  }

  /*
    Description: Function to validate form fields
    @parameters: No
    @Return: No
  */
  buildForm() {
    this.searchForm = this._fb.group({
      idType: [""],
      idNumber: [""],
      surname: [""],
      dateOfBirth: [""]
    });
  }

  /**
    Description: Function to get id types to prepopulate
    @parameters: No
    @Return: No
  */
  getIdTypes(): void {
    this._searchInsuredPersonService.getIdTypes().subscribe(
      response => {
        this._spinner.hide();
        if (Utils.chkResponseSuccess(response)) {
          this.arrIdTypes = response["body"]["identityTypes"];
          this.searchForm.setValue({
            idType: this.arrIdTypes[0]["code"],
            idNumber: "",
            surname: "",
            dateOfBirth: ""
          });
          this.arrIdTypes.forEach(item => {
            this.arrIds[item.code] = item.description;
          });
        }
      },
      error => {
        this.handleServiceError(error);
      }
    );
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
    Description: Function to search insured person on Search button
    @parameters: No
    @Return: No
  */
  searchInsuredPersons() {
    // tslint:disable-next-line:max-line-length
    if (
      !this.searchForm.controls.idNumber.value &&
      (!this.searchForm.controls.surname.value ||
        !this.searchForm.controls.dateOfBirth.value)
    ) {
      this.validNumber = false;
      this.searchResultFound = false;
      this.searchResultNotFound = false;
      this._alertService.error(
        "Please capture the required fields for performing Insured Person search!"
      );
    } else {
      this.searchFlag = false;
      if (
        this.searchForm.controls.idType.value === "4" &&
        this.searchForm.controls.idNumber.value !== ""
      ) {
        this.isValidNumber = this.isValidSAID(
          this.searchForm.controls.idNumber.value
        );
        if (this.isValidNumber) {
          this.searchFlag = true;
        } else {
          this.validNumber = true;
          this.searchFlag = false;
          this.searchResultFound = false;
          this.searchResultNotFound = false;
        }
      } else {
        this.searchFlag = true;
      }
      if (
        Utils.checkUserPermission("ENQUIRY_FULL") &&
        this.searchFlag === true
      ) {
        this._spinner.show();
        this._searchInsuredPersonService.changePersonSearchInfo(null);
        this.validNumber = false;
        this.arrIdNumbers = [];
        this.arrSurnames = [];
        const date = this.searchForm.controls.dateOfBirth.value;
        const objEnquiry = {
          dateOfBirth: date ? Utils.dtFormat(date) : "",
          identityNumber: this.searchForm.controls.idNumber.value,
          identityType: {
            code: this.searchForm.controls.idType.value,
            description: this.arrIds[this.searchForm.controls.idType.value]
          },
          surname: this.searchForm.controls.surname.value
        };
        this._searchInsuredPersonService.changePersonSearchInfo(objEnquiry);
        // Call service to search the insured persons
        this._searchInsuredPersonService.getAllPersons(objEnquiry).subscribe(
          response => {
            this._spinner.hide();
            const formData: any = [];
            if (Utils.chkResponseSuccess(response)) {
              const arrData: any = response["body"];
              // if (arrData) {
              if (arrData.length > 0) {
                this.searchResultFound = true;
                this.searchResultNotFound = false;
                // this.insuredPersons = this._orderPipe.transform(arrData, 'dateOfBirth');
                arrData.forEach(item => {
                  let arrSearchData: any = [];
                  arrSearchData = {
                    personID: item.personID,
                    identityNumber: item.identityNumber,
                    identityType: item.identityType,
                    givenName1: item.givenName1,
                    givenName2: item.givenName2,
                    givenName3: item.givenName3,
                    surname: item.surname,
                    dateOfBirth: Utils.dtFormat(item.dateOfBirth),
                    gender: item.gender,
                    perfect: item.perfectMatch
                  };
                  formData.push(arrSearchData);
                });
                this.insuredPersons = this._orderPipe.transform(
                  formData,
                  "dateOfBirth"
                );
              } else {
                this.currentDateTime = moment().format("DD/MM/YYYY HH:mm A");
                this.validNumber = false;
                this.searchResultNotFound = true;
                this.searchResultFound = false;
                this.idType = this.arrIds[
                  this.searchForm.controls.idType.value
                ];
                this.idNumber = this.searchForm.controls.idNumber.value;
                this.surname = this.searchForm.controls.surname.value;
                this.dateOfBirth = date ? Utils.dtFormat(date) : "";
                this.strIdFields = this.idNumber ? true : false;
              }
            }
          },
          error => {
            this.validNumber = false;
            this.searchResultFound = false;
            this.searchResultNotFound = false;
            this.handleServiceError(error);
          }
        );
      }
    }

    // this.searchFlag = false;
    // } else {
    //   this._alertService.error('You are not Authorized to search Person Enquiry.');
    // }
  }
  validateID(value: any): any {
    throw new Error("Method not implemented.");
  }

  /**
    Description: Function to redirect on person detail/history screen
    @parameters: personId
    @Return: No
  */
  displayPersonDetails(personID, typeCode, typeNumber, perfectMatch) {
    const objIdDeatils: any = {
      personID: personID,
      identityTypeCode: typeCode,
      identityNumber: typeNumber,
      perfectMatch: perfectMatch
    };
    localStorage.setItem("personIdDetails", JSON.stringify(objIdDeatils))
    localStorage.setItem("selectedImpId", null);;
    setTimeout(() => {
      this._router.navigate(["/insured-person-details"]);
    }, 1000);
  }

  /**
    Description: Function to download snapshot of the screen
    @parameters: No
    @Return: No
  */
  capPersonNotFndScreen() {
    Utils.downloadSnapshot("personNotFoundSection", "person-not-found-screen");
  }

  /**
    Description: Function to Add new insured person
    @parameters: No
    @Return: No
  */
  addNewPerson() {
    this._spinner.show();
    let objAction = {};
    if (Utils.checkUserPermission("ADD_UPDATE_IMPAIRMENT")) {
      objAction = {
        action: "ADD IMPAIRMENT"
      };
    } else {
      objAction = {
        action: "ADD CLAIM"
      };
    }
    // Set to check action while add/edit impairment/claim
    localStorage.setItem("pageAction", JSON.stringify(objAction));
    // Set to check screen flow
    localStorage.setItem("pageName", "Enquiry");
    localStorage.setItem("redirectTo", "Enquiry");
    localStorage.removeItem('selectedImpId');
    localStorage.removeItem('selectedClaimId');
    localStorage.removeItem('impDetails');
    // Set Tab Information
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.arrTabData = response.tabs;
        if (this.arrTabData) {
          this.arrTabData[0].tabName = "ADD PERSON";
          this.arrTabData[0].active = true;
          this.arrTabData[0].show = true;
          this.arrTabData[0].borderBottomClass = true;
          this.arrTabData[0].tabContent = true;
          this.arrTabData[0].formValid = false;
          if (objAction["action"] === "ADD IMPAIRMENT") {
            this.arrTabData[1].tabName = "ADD IMPAIRMENT";
            this.arrTabData[1].active = false;
            this.arrTabData[1].show = true;
            this.arrTabData[1].tabContent = false;
            this.arrTabData[1].borderBottomClass = false;
            this.arrTabData[1].formValid = false;
            this.arrTabData[2].active = false;
            this.arrTabData[2].show = false;
            this.arrTabData[2].tabContent = false;
            this.arrTabData[2].borderBottomClass = false;
            this.arrTabData[2].formValid = false;
          } else if (objAction["action"] === "ADD CLAIM") {
            this.arrTabData[1].active = false;
            this.arrTabData[1].show = false;
            this.arrTabData[1].tabContent = false;
            this.arrTabData[1].borderBottomClass = false;
            this.arrTabData[1].formValid = false;
            this.arrTabData[2].tabName = "ADD CLAIM";
            this.arrTabData[2].active = false;
            this.arrTabData[2].show = true;
            this.arrTabData[2].tabContent = false;
            this.arrTabData[2].borderBottomClass = false;
            this.arrTabData[2].formValid = false;
          }

          this.arrTabData[3].tabName = "ADD NOTE";
          this.arrTabData[3].active = false;
          this.arrTabData[3].show = true;
          this.arrTabData[3].tabContent = false;
          this.arrTabData[3].borderBottomClass = false;
          this.arrTabData[3].formValid = false;

          this.arrTabData[4].tabName = "ADD SCRATCHPAD";
          this.arrTabData[4].active = false;
          this.arrTabData[4].show = true;
          this.arrTabData[4].tabContent = false;
          this.arrTabData[4].borderBottomClass = false;
          this.arrTabData[4].formValid = false;
        }
      }
    });
    this.objTabInfo = {
      tabs: this.arrTabData,
      submitActive: this.subActive,
      noteActive: false,
      scratchpadtActive: false,
      personId: ""
    };
    this._personDetailsService.changeTabInformation(this.objTabInfo);
    localStorage.setItem('impairmentItems', 'null');
    setTimeout(() => {
      this._router.navigate(["/person-details"]);
      this._spinner.hide();
    }, 1000);
  }
  /**
    Description: Function to select the id types
    @parameters: No
    @Return: No
  */
  changeIdType() {
    this.searchResultNotFound = false;
    this.searchResultFound = false;
  }

  /**
    Description: Function to search id number 
    @parameters: No
    @Return: No
  */
  autocompleteIdNumber() {
    this.arrIdNumbers = [];
    let arrData: any = [];
    let idNumber = this.searchForm.controls.idNumber.value;
    let arrNumber = String(idNumber).split("");
    if (arrNumber.length >= 10) {
      this._searchInsuredPersonService.getIdNumbers(idNumber).subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            arrData = response["body"];
            if (arrData.length > 0) {
              this.arrIdNumbers = arrData;
            }
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
    }
  }
  selectedIdNumber(idNumber) {
    this.searchForm.patchValue({ idNumber: idNumber });
    this.arrIdNumbers = [];
  }

  /**
    Description: Function to search person surname
    @parameters: No
    @Return: No
  */
  autocompleteSurname() {
    this.arrSurnames = [];
    let arrData: any = [];
    let surname = this.searchForm.controls.surname.value;
    let arrSurname = String(surname).split("");
    if (arrSurname.length >= 3) {
      this._searchInsuredPersonService.getSurnames(surname).subscribe(
        response => {
          if (Utils.chkResponseSuccess(response)) {
            arrData = response["body"];
            if (arrData.length > 0) {
              this.arrSurnames = arrData;
            }
          }
        },
        error => {
          this.handleServiceError(error);
        }
      );
    }
  }
  hideautocomplete() {
    this.arrIdNumbers = [];
    this.arrSurnames = [];
  }
  selectedSurname(surname) {
    this.searchForm.patchValue({ surname: surname });
    this.arrSurnames = [];
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
    this._spinner.hide();
  }

  /**
    Description: Function to destroy services on component destroy
    @parameters: No
    @Return: No
  */
  ngOnDestroy() {
    console.log("Enquiry destroy...");
  }
}
