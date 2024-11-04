import { ComponentFixture, TestBed, async } from "@angular/core/testing";
import { AddInsuredPersonComponent } from "./add-insured-person.component";
import {
  SearchInsuredPersonService,
  PersonDetailsService,
  AlertService
} from "../_services";
import { BsDatepickerModule } from "ngx-bootstrap";
import { ReactiveFormsModule, FormGroup } from "@angular/forms";
import { NgxSpinnerService } from "ngx-spinner";
import { NO_ERRORS_SCHEMA } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";
import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
import { RouterTestingModule } from "@angular/router/testing";
import { of } from "rxjs";
fdescribe("AddInsuredPersonComponent", () => {
  let component: AddInsuredPersonComponent;
  let fixture: ComponentFixture<AddInsuredPersonComponent>;
  let persondetailsservice;
let searchinsuredpersonservice;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddInsuredPersonComponent],
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
    fixture = TestBed.createComponent(AddInsuredPersonComponent);
    component = fixture.componentInstance;
    persondetailsservice = fixture.debugElement.injector.get(
      PersonDetailsService
    );
    searchinsuredpersonservice = fixture.debugElement.injector.get(SearchInsuredPersonService);
    component.arrTabData = [
      {
        tabName: "ADD PERSON",
        active: false,
        show: true,
        tabContent: true,
        borderBottomClass: true,
        formValid: false
      },
      {
        tabName: "ADD IMPAIRMENT",
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

  it("should call getGender and return list of gender", async(() => {
    const response = {
      body: { genders: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(persondetailsservice, "getAllGenders").and.returnValue(of(response));
    component.getGenders();
    fixture.detectChanges();
    expect(component.genders).toEqual(response["body"]["genders"]);
  }));

  it("should call getTitle and return list of title", async(() => {
    const response = {
      body: { titles: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(persondetailsservice, "getAllPretitles").and.returnValue(
      of(response)
    );
    component.getPretitles();
    fixture.detectChanges();
    expect(component.pretitles).toEqual(response["body"]["titles"]);
  }));

  it("should call getIdType and return list of Idtype", () => {
    const response = {
      body: { identityTypes: [] },
      ok: true,
      status: 200,
      statusText: "OK",
      type: 4
    };
    spyOn(searchinsuredpersonservice, "getIdTypes").and.returnValue(of(response));
    component.getIdTypes();
    fixture.detectChanges();
    expect(component.idTypes).toEqual(response["body"]["identityTypes"]);
  });

  it("AddInsuredPerson form be invalid", () => {
    fixture.detectChanges();
    component.addPersonForm.controls["idType"].patchValue([]);
    component.addPersonForm.controls["gender"].patchValue([]);
    component.addPersonForm.controls["pretitle"].patchValue([]);
    component.addPersonForm.controls["idNumber"].patchValue("");
    component.addPersonForm.controls["dateOfBirth"].patchValue("");
    component.addPersonForm.controls["surname"].patchValue("");
    component.addPersonForm.controls["givenName1"].patchValue("");
    expect(component.addPersonForm.valid).toBeFalsy();
  });

  it("should call validate data invalid form", async(() => {
    fixture.detectChanges();
    component.addPersonForm.controls["idType"].patchValue([]);
    component.addPersonForm.controls["gender"].patchValue([]);
    component.addPersonForm.controls["pretitle"].patchValue([]);
    component.addPersonForm.controls["idNumber"].patchValue("");
    component.addPersonForm.controls["dateOfBirth"].patchValue("");
    component.addPersonForm.controls["surname"].patchValue("");
    component.addPersonForm.controls["givenName1"].patchValue("");
    spyOn(persondetailsservice, "getTabInformation").and.returnValue(of());
    component.validateData();
    fixture.detectChanges();
    expect(component.arrTabData[0].active).toBeTruthy();
  }));

  it("should check for valid form", async(() => {
    fixture.detectChanges();
    component.addPersonForm.controls["idType"].patchValue([123154]);
    component.addPersonForm.controls["gender"].patchValue(["Female"]);
    component.addPersonForm.controls["pretitle"].patchValue(["Ms"]);
    component.addPersonForm.controls["idNumber"].patchValue("1215432");
    component.addPersonForm.controls["dateOfBirth"].patchValue("01/01/2019");
    component.addPersonForm.controls["surname"].patchValue("axcvx");
    component.addPersonForm.controls["givenName1"].patchValue("dfsf");
    expect(component.addPersonForm.valid).toBeTruthy();
  }));

//   it("should call validate method", async(() => {
//     fixture.detectChanges();
//     component.addPersonForm.controls["idType"].patchValue([1]);
//     component.addPersonForm.controls["gender"].patchValue(["Female"]);
//     component.addPersonForm.controls["pretitle"].patchValue(["Ms"]);
//     component.addPersonForm.controls["idNumber"].patchValue("1215432");
//     component.addPersonForm.controls["dateOfBirth"].patchValue("01/01/2019");
//     component.addPersonForm.controls["surname"].patchValue("axcvx");
//     component.addPersonForm.controls["givenName1"].patchValue("dfsf");
//     component.strCustomerNotFound = false;

//     spyOn(persondetailsservice, "getTabInformation").and.returnValue(of());
//     let objAction = {
//       action: "ADD IMPAIRMENT"
//     };
//     localStorage.setItem("pageAction", JSON.stringify(objAction));
//     component.userAction = JSON.parse(localStorage.getItem("pageAction"));
//     component.validateData();
//     fixture.detectChanges();
//     expect(component.addPersonForm.invalid).toBeTruthy();
//   }));

  it("should call emit method of AddInsuredPerson", () => {
    spyOn(component.formReady, "emit");
    fixture.detectChanges();
    expect(component.formReady.emit).toHaveBeenCalled();
  });

  it("should have a defined component", () => {
    expect(component).toBeDefined();
  });

  it("should create a FormGroup comprised of FormControls", () => {
    component.ngOnInit();
    expect(component.addPersonForm instanceof FormGroup).toBe(true);
  });

//   it("should have a defined idtypedropdownsettings", () => {
//     let idtypedropdownsettings = {
//       singleSelection: "true",
//       text: "Select",
//       labelKey: "description",
//       primaryKey: "code",
//       enableSearchFilter: "true",
//       showCheckbox: "false",
//       classes: "myclass custom-class"
//     };
//     //  expect(component.idtypedropdownsettings).toBeDefined();
//     expect(component.idtypedropdownsettings).toEqual(
//       JSON.stringify(idtypedropdownsettings)
//     );
//   });
});
