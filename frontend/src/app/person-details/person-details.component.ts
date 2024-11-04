import { Component, OnInit, OnDestroy } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { Utils } from "../utils";
import {
  PersonDetailsService,
  TabserviceService,
  AuthenticationService
} from "../_services";
declare var jQuery: any;

@Component({
  selector: "app-person-details",
  templateUrl: "./person-details.component.html",
  styleUrls: ["./person-details.component.css"]
})
export class PersonDetailsComponent implements OnInit, OnDestroy {
  checkoutForm: FormGroup;
  confirmationCancelForm: FormGroup;
  tabInformation: any;
  subscription: any;
  arrTabData: any = [];
  objTabInfo: any;
  subActive: boolean = false;
  objCurrentUser: any;
  strUserRole: string;
  loginUser: any;
  impData: any;
  selectedImpId: any;
  formActive: boolean = false;
  noteActive: boolean = false;
  scratchpadtActive: boolean = false;
  claimData: any;
  selectedClaimId: any;
  constructor(
    private formBuilder: FormBuilder,
    private _tabservice: TabserviceService,
    private _router: Router,
    private _personDetailsService: PersonDetailsService,
    private _authenticationService: AuthenticationService,
    private _spinner: NgxSpinnerService
  ) {
    this.loginUser = JSON.parse(localStorage.getItem("currentUser"));
    this._authenticationService.changeUser(this.loginUser);
  }

  ngOnInit() {
    this._spinner.show();
    this.checkoutForm = this.formBuilder.group({});
    this.confirmationCancelForm = this.formBuilder.group({});
    const obj = Utils.defaultTabs();
    this._personDetailsService.changeTabInformation(obj);

    // tabs info start
    // this.getInfo();
    setTimeout(() => {
      this.getInfo();
    }, 1000);
    this._spinner.hide();
  }
  getInfo() {
    this.subscription = this._personDetailsService.getTabInformation.subscribe(
      response => {
        this.tabInformation = response;
      }
    );
  }

  /**
   * After a form is initialized, we link it to our main form
   */
  formInitialized(name: string, form: FormGroup) {
    this._spinner.show();
    this.checkoutForm.setControl(name, form);
    this._spinner.hide();
  }
  confirmCancel() {
    this.checkoutForm.reset();
    jQuery("#myModal1").modal("hide");
    this.setDefaultTabs();
    const redTo = localStorage.getItem("redirectTo");
    const personId = localStorage.getItem("insuredPersonId");
    if (redTo === "Enquiry") {
      this._router.navigate(["/insured-person-enquiry"]);
    } else {
      this._router.navigate(["/insured-person-details"]);
    }
  }
  setDefaultTabs() {
    // tabs info end
    // this.objCurrentUser = JSON.parse(localStorage.getItem('currentUser'));
    localStorage.setItem("objExistingperson", null);
    localStorage.setItem("objImpairmentData", null);
    localStorage.setItem("objClaimData", null);

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

          if (Utils.checkUserPermission("ADD_UPDATE_IMPAIRMENT")) {
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
          } else if (Utils.checkUserPermission("ADD_UPDATE_CLAIM")) {
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

        this.objTabInfo = {
          tabs: this.arrTabData,
          submitActive: this.subActive,
          noteActive: this.noteActive,
          scratchpadtActive: this.scratchpadtActive,
          personId: ""
        };
      }
    });
  }
  setDefaultTabsIn() {
    // tabs info end
    this.strUserRole = JSON.parse(localStorage.getItem("pageAction"));
    localStorage.setItem("objExistingperson", null);
    localStorage.setItem("objImpairmentData", null);
    localStorage.setItem("objClaimData", null);

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
          if (this.strUserRole["action"] === "ADD IMPAIRMENT") {
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
          } else if (this.strUserRole["action"] === "ADD CLAIM") {
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
      noteActive: this.noteActive,
      scratchpadtActive: this.scratchpadtActive,
      personId: ""
    };
    this._personDetailsService.changeTabInformation(this.objTabInfo);
  }

  ngOnDestroy() {
    // Set person information
    this._personDetailsService.changePersonInfo(null);
    this.setDefaultTabsIn();
  }
}
