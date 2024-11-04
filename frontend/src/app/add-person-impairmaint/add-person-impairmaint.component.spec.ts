import { async, ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ReactiveFormsModule, FormGroup } from '@angular/forms';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { HttpClientModule } from '@angular/common/http';
import { SearchInsuredPersonService } from '../_services';
import { AddPersonImpairmaintComponent } from './add-person-impairmaint.component';
import { By } from "@angular/platform-browser";
import { NO_ERRORS_SCHEMA } from "@angular/core";
import {
  PersonDetailsService,
  AlertService
} from "../_services";
import { NgxSpinnerService } from "ngx-spinner";
import { of } from 'rxjs';
import { BsDatepickerModule } from 'ngx-bootstrap';

describe('AddPersonImpairmaintComponent', () => {
  let component: AddPersonImpairmaintComponent;
  let fixture: ComponentFixture<AddPersonImpairmaintComponent>;
  let persondetailsservice;
  let searchinsuredpersonservice;
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddPersonImpairmaintComponent],
      imports: [ReactiveFormsModule, AngularMultiSelectModule,
        BsDatepickerModule.forRoot(), HttpClientModule, RouterTestingModule],
      providers: [SearchInsuredPersonService, AlertService, NgxSpinnerService, PersonDetailsService
      ],
      schemas: [NO_ERRORS_SCHEMA]
    });
    fixture = TestBed.createComponent(AddPersonImpairmaintComponent);
    component = fixture.componentInstance;
    persondetailsservice = fixture.debugElement.injector.get(PersonDetailsService);
    searchinsuredpersonservice = fixture.debugElement.injector.get(SearchInsuredPersonService);
    component.arrTabData = [
      {
        'tabName': 'ADD PERSON',
        'active': true,
        'show': true,
        'tabContent': true,
        'borderBottomClass': true,
        'formValid': false
      },
      {
        'tabName': 'EDIT IMPAIRMENT',
        'active': false,
        'show': true,
        'tabContent': false,
        'borderBottomClass': false,
        'formValid': false
      },
      {
        'tabName': 'ADD CLAIM',
        'active': false,
        'show': false,
        'tabContent': false,
        'borderBottomClass': false,
        'formValid': false
      },
      {
        'tabName': 'ADD NOTE',
        'active': false,
        'show': true,
        'tabContent': false,
        'borderBottomClass': false,
        'formValid': false
      },
      {
        'tabName': 'ADD SCRATCHPAD',
        'active': false,
        'show': true,
        'tabContent': false,
        'borderBottomClass': false,
        'formValid': false
      }
    ];
  });

  it('check isReadOnly', () => {
    component.selectedimpairmentId = "12334";
    const result = component.isReadOnly();
    expect(result).toBeTruthy();
  });


  it("should call getImpairmentBenefits", () => {
    const response = {
      "body": { "policyTypes": [] },
      "ok": true,
      "status": 200,
      "statusText": "OK",
      "type": 4
    };
    spyOn(persondetailsservice, 'getAllImpairmentBenefits').and.returnValue(of(response))
    component.getImpairmentBenefits();

    fixture.detectChanges();
    expect(component.impairmentBenefits).toEqual(response['body']['policyTypes']);
  });




  it("should call getSymbols", () => {
    const response = {
      "body": { "symbols": [] },
      "ok": true,
      "status": 200,
      "statusText": "OK",
      "type": 4
    };
    spyOn(persondetailsservice, 'getAllSymbols').and.returnValue(of(response))
    component.getSymbols();
    fixture.detectChanges();
    expect(component.symbols).toEqual(response['body']['symbols']);
  });



  it("should call getSpecialInvestigates", () => {
    const response = {
      "body": { "lifeSpecs": [] },
      "ok": true,
      "status": 200,
      "statusText": "OK",
      "type": 4
    };
    spyOn(persondetailsservice, 'getAllSpecialInvestigates').and.returnValue(of(response))
    component.getSpecialInvestigates();

    fixture.detectChanges();
    expect(component.specialInvestigates).toEqual(response['body']['lifeSpecs']);
  });



  it("should call getImpairments", () => {
    const response = {
      "body": { "impairmentCodes": [] },
      "ok": true,
      "status": 200,
      "statusText": "OK",
      "type": 4
    };
    spyOn(persondetailsservice, 'getAllImpairments').and.returnValue(of(response))
    component.getImpairments();

    fixture.detectChanges();
    expect(component.impairments).toEqual(response['body']['impairmentCodes']);
  });

  it("addImpairmentForm form be invalid", () => {

    fixture.detectChanges();

    component.addImpairmentForm.controls['policies']['controls'][0].patchValue({
      policyNumber:
        ''
    });

    component.addImpairmentForm.controls['policies']['controls'][0].patchValue({
      policyBenefit:
        ''
    });

    component.addImpairmentForm.controls['policies']['controls'][0].controls['impairments']['controls'][0].patchValue({
      timeSignal:
        ''
    });

    component.addImpairmentForm.controls['policies']['controls'][0].controls['impairments']['controls'][0].patchValue({
      impairment:
        []
    });

    expect(component.addImpairmentForm.valid).toBeFalsy();

  });



  it("addImpairmentForm form be valid", () => {

    fixture.detectChanges();

    component.addImpairmentForm.controls['policies']['controls'][0].patchValue({
      policyNumber:
        '111'
    });

    component.addImpairmentForm.controls['policies']['controls'][0].patchValue({
      policyBenefit:
        'RISK/DEATH BENEFIT'
    });

    component.addImpairmentForm.controls['policies']['controls'][0].controls['impairments']['controls'][0].patchValue({
      timeSignal:
        '2010'
    });

    component.addImpairmentForm.controls['policies']['controls'][0].controls['impairments']['controls'][0].patchValue({
      impairment:
        'OVERWEIGHT'
    });

    expect(component.addImpairmentForm.valid).toBeTruthy();

  });

  // it("initpolicy()",()=>{
  //   const test={}
  //   // const chk=this._fb.group({
  //   //   policyNumber: ["", Validators.required],
  //   //   policyBenefit: ["", Validators.required],
  //   //   editReason: ["", Validators.required],
  //   //   impairments: this._fb.array([this.initImpairment()])
  //   // })
  //   component.selectedimpairmentId="12334";
  //   const result =component.initPolicy();
  //   expect(result).toEqual(test);

  // });

  // it('There should be Clear Button event', async(() => {

  //   spyOn(component, 'clearData');
  //   let btn = fixture.debugElement.query(By.css('*'));
  //   btn.triggerEventHandler('click', null);
  //   // tick(); // simulates the passage of time until all pending asynchronous activities finish
  //   fixture.detectChanges();
  //   expect(component.clearData).toHaveBeenCalled();
  // }));

  it('should call emit method of AddPersonImpairment', () => {
    spyOn(component.formReady, 'emit');
    fixture.detectChanges();
    expect(component.formReady.emit).toHaveBeenCalled();
 });
 it('should have a defined component', () => {
          expect(component).toBeDefined();
      });

  it('should create a FormGroup comprised of FormControls', () => {
            component.ngOnInit();
            expect(component.addImpairmentForm instanceof FormGroup).toBe(true);
        });
  });
