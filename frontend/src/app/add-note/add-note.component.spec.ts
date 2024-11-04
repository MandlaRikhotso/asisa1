import { async, ComponentFixture, TestBed } from "@angular/core/testing";

import { AddNoteComponent } from "./add-note.component";
import { SearchInsuredPersonService } from "../_services";
import { inject } from "@angular/core/testing";

import { PersonDetailsService, AlertService } from "../_services";
import { BsDatepickerModule } from "ngx-bootstrap";
import { ReactiveFormsModule, FormGroup } from "@angular/forms";
import { NgxSpinnerService } from "ngx-spinner";
import { NO_ERRORS_SCHEMA, Component } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { RouterTestingModule } from "@angular/router/testing";
import { of, Observable } from "rxjs";
import { Gender } from "../_models";
describe("AddNoteComponent", () => {
  let component: AddNoteComponent;
  let fixture: ComponentFixture<AddNoteComponent>;
  let searchinsuredpersonservice;
  let persondetailservice;
  let objPersonInfo;

  beforeEach(() => {
    objPersonInfo = {
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
    TestBed.configureTestingModule({
      declarations: [AddNoteComponent],
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
        PersonDetailsService
      ],
      schemas: [NO_ERRORS_SCHEMA]
    });
    fixture = TestBed.createComponent(AddNoteComponent);
    component = fixture.componentInstance;
    searchinsuredpersonservice = fixture.debugElement.injector.get(
      SearchInsuredPersonService
    );
    persondetailservice = fixture.debugElement.injector.get(
      PersonDetailsService
    );
  });

  it("should call removeFormElements", async(() => {
    localStorage.setItem("removePolicyAt", "1");
    component.removeFormElements();
    fixture.detectChanges();
    expect(component.removeFormElements()).toHaveBeenCalled();
  }));

//   it("getExistingPersonDetails()", () => {
//     const objIdDeatils: any = {
//       personID: "123466",
//       identityTypeCode: "1",
//       identityNumber: "123456789",
//       perfectMatch: "perfectMatch"
//     };
//     const response = {
//       body: {
//         insuredPerson: [
//           {
//             code: "1",
//             description: "SA ID"
//           }
//         ],
//         identityNumber: "123456789"
//       },
//       ok: true,
//       status: 200,
//       statusText: "OK",
//       type: 4
//     };
//     localStorage.setItem("objExistingperson", JSON.stringify(objPersonInfo));

//     component.selectedPersonId = 123456789;
//     localStorage.setItem("personIdDetails", JSON.stringify(objIdDeatils));
//     spyOn(
//       searchinsuredpersonservice,
//       "getCurrentPersonDetails"
//     ).and.returnValue(of(response));
//     spyOn(persondetailservice, "changePersonInfo").and.returnValue(of());
//     expect(
//       localStorage.getItem("exitingPersonInfo").length
//     ).toBeGreaterThanOrEqual(0);
//   });

  it("noteForm form be invalid", () => {
    fixture.detectChanges();

    component.addNoteForm.controls["notes"]["controls"][0].patchValue({
      note: ""
    });
    expect(component.addNoteForm.valid).toBeFalsy();
  });

  it("noteForm form be valid", () => {
    fixture.detectChanges();

    component.addNoteForm.controls["notes"]["controls"][0].patchValue({
      note: "test note "
    });
    expect(component.addNoteForm.valid).toBeTruthy();
  });

  it("should call emit method of Add Note", () => {
    spyOn(component.formReady, "emit");
    fixture.detectChanges();
    expect(component.formReady.emit).toHaveBeenCalled();
  });

  it("should have a defined component", () => {
    expect(component).toBeDefined();
  });

  it("should create a FormGroup comprised of FormControls", () => {
    component.ngOnInit();
    expect(component.addNoteForm instanceof FormGroup).toBe(true);
  });
});
