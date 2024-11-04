import { ComponentFixture, TestBed } from "@angular/core/testing";

import { AddScratchpadComponent } from "./add-scratchpad.component";
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

describe("AddScratchpadComponent", () => {
  let component: AddScratchpadComponent;
  let fixture: ComponentFixture<AddScratchpadComponent>;

  let persondetailsservice;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddScratchpadComponent],
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
    fixture = TestBed.createComponent(AddScratchpadComponent);
    component = fixture.componentInstance;
    persondetailsservice = fixture.debugElement.injector.get(
      PersonDetailsService
    );
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

  it("addScratchForm form be invalid", () => {
    fixture.detectChanges();
    component.addScratchForm.controls["scratchpads"]["controls"][0].patchValue({
      comment: ""
    });
    expect(component.addScratchForm.valid).toBeFalsy();
  });

  it("addScratchForm form be valid", () => {
    fixture.detectChanges();
    component.addScratchForm.controls["scratchpads"]["controls"][0].patchValue({
      comment: "Test"
    });
    expect(component.addScratchForm.valid).toBeTruthy();
  });

  it("should have a defined component", () => {
    expect(component).toBeDefined();
  });

  it("should create a FormGroup comprised of FormControls", () => {
    component.ngOnInit();
    expect(component.addScratchForm instanceof FormGroup).toBe(true);
  });
});
