import { async, ComponentFixture, TestBed } from "@angular/core/testing";
import { RouterTestingModule } from "@angular/router/testing";
import { ReactiveFormsModule, FormGroup } from "@angular/forms";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { BsDatepickerModule } from "ngx-bootstrap";
import { HttpClientModule } from "@angular/common/http";
import {
  SearchInsuredPersonService,
  AlertService,
  PersonDetailsService
} from "../_services";
import { AddPersonClaimComponent } from "./add-person-claim.component";

import { NgxSpinnerService } from "ngx-spinner";
import { NO_ERRORS_SCHEMA } from "@angular/core";
import { of } from "rxjs";
import { Utils } from "../utils";

describe("AddPersonClaimComponent", () => {
  let component: AddPersonClaimComponent;
  let fixture: ComponentFixture<AddPersonClaimComponent>;
  let persondetailsservice;
  let utilservice;
  let searchinsuredpersonservice;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddPersonClaimComponent],
      imports: [
        ReactiveFormsModule,
        AngularMultiSelectModule,
        BsDatepickerModule.forRoot(),
        HttpClientModule,
        RouterTestingModule
      ],
      providers: [
        SearchInsuredPersonService,
        AlertService,
        NgxSpinnerService,
        PersonDetailsService,
        Utils
      ],
      schemas: [NO_ERRORS_SCHEMA]
    });
    fixture = TestBed.createComponent(AddPersonClaimComponent);
    component = fixture.componentInstance;
    persondetailsservice = fixture.debugElement.injector.get(
      PersonDetailsService
    );
    searchinsuredpersonservice = fixture.debugElement.injector.get(
      SearchInsuredPersonService
    );
    utilservice = fixture.debugElement.injector.get(Utils);

    component.objPersonInfo = {
      insuredPerson: {
        identityTypeCode: [
          {
            code: "1",
            description: "SA ID"
          }
        ],
        identityNumber: "123456789",
        surname: "Moolamalla",
        givenName1: "Tirumal",
        givenName2: null,
        givenName3: null,
        dateOfBirth: "01/07/2018",
        gender: [
          {
            code: "1",
            description: "MALE"
          }
        ],
        addressLine1: null,
        addressLine2: null,
        addressLine3: null,
        postalCode: 0,
        title: [
          {
            code: "Mr",
            description: null
          }
        ],
        seltabName: "ADD PERSON"
      }
    };

    component.arrTabData = [
      {
        tabName: "ADD PERSON",
        active: true,
        show: true,
        tabContent: true,
        borderBottomClass: true,
        formValid: false
      },
      {
        tabName: "EDIT IMPAIRMENT",
        active: false,
        show: true,
        tabContent: false,
        borderBottomClass: false,
        formValid: false
      },
      {
        tabName: "ADD CLAIM",
        active: false,
        show: false,
        tabContent: false,
        borderBottomClass: false,
        formValid: false
      },
      {
        tabName: "ADD NOTE",
        active: false,
        show: true,
        tabContent: false,
        borderBottomClass: false,
        formValid: false
      },
      {
        tabName: "ADD SCRATCHPAD",
        active: false,
        show: true,
        tabContent: false,
        borderBottomClass: false,
        formValid: false
      }
    ];
  });

  it("should call getClaimTypes", () => {
    const response = {
      body: { policyTypes: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(persondetailsservice, "getAllImpairmentBenefits").and.returnValue(
      of(response)
    );
    component.getClaimTypes();

    fixture.detectChanges();
    expect(component.claimTypes).toEqual(response["body"]["policyTypes"]);
  });

  it("should call getCategoryIds", () => {
    const response = {
      body: { claimCategories: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(persondetailsservice, "getCategoryIds").and.returnValue(of(response));
    component.getCategoryIds();

    fixture.detectChanges();
    expect(component.categoryIds).toEqual(response["body"]["claimCategories"]);
  });

  it("should call getClaimReasons", () => {
    const response = {
      body: { impairmentCodes: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(persondetailsservice, "getAllImpairments").and.returnValue(
      of(response)
    );
    component.getClaimReasons();

    fixture.detectChanges();
    expect(component.claimReasons).toEqual(response["body"]["impairmentCodes"]);
  });
  it("should call getClaimStatus", () => {
    const response = {
      body: { claimStatuses: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(persondetailsservice, "getClaimStatus").and.returnValue(of(response));
    component.getClaimStatus();

    fixture.detectChanges();
    expect(component.claimStatus).toEqual(response["body"]["claimStatuses"]);
  });
  it("should call getPayMethods", () => {
    const response = {
      body: { paymentMethods: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(persondetailsservice, "getPayMethods").and.returnValue(of(response));
    component.getPayMethods();

    fixture.detectChanges();
    expect(component.payMethods).toEqual(response["body"]["paymentMethods"]);
  });

  it("should call getCauseOfEvents", () => {
    const response = {
      body: { claimCauses: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(persondetailsservice, "getCauseOfEvents").and.returnValue(
      of(response)
    );
    component.getCauseOfEvents();

    fixture.detectChanges();
    expect(component.causeOfEvents).toEqual(response["body"]["claimCauses"]);
  });

  it("claimForm form be invalid", () => {
    fixture.detectChanges();

    component.claimForm.controls["policies"]["controls"][0].patchValue({
      policyNumber: ""
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      claimType: ""
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      categoryId: ""
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      causeOfEvent: ""
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      claimReason: ""
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      claimStatus: ""
    });
    expect(component.claimForm.valid).toBeFalsy();
  });

  it("claimForm form be valid", () => {
    fixture.detectChanges();

    component.claimForm.controls["policies"]["controls"][0].patchValue({
      policyNumber: "12345"
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      claimType: "1"
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      categoryId: "1"
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      causeOfEvent: "ACCIDENT IN THE HOME"
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      claimReason: "ACCIDENT"
    });
    component.claimForm.controls["policies"]["controls"][0].patchValue({
      claimStatus: "APPROVED"
    });
    expect(component.claimForm.valid).toBeTruthy();
  });

  it("should call emit method of AddPersonClaim", () => {
    spyOn(component.formReady, "emit");
    fixture.detectChanges();
    expect(component.formReady.emit).toHaveBeenCalled();
  });

  // it("getExistingPersonDetails()",()=>{
  //   localStorage.setItem('pageName','');
  //   component.seltabName=true;
  //   component.selectedPersonId=123456789;
  //   localStorage.setItem('personIdDetails',JSON.stringify(component.objPersonInfo))
  //   spyOn(searchinsuredpersonservice, 'getCurrentPersonDetails').and.returnValue(of());
  //   spyOn(utilservice, 'setInsuredPersonInfo').and.returnValue(of());

  // })

  it("should have a defined component", () => {
    expect(component).toBeDefined();
  });

  it("should create a FormGroup comprised of FormControls", () => {
    component.ngOnInit();
    expect(component.claimForm instanceof FormGroup).toBe(true);
  });
});
