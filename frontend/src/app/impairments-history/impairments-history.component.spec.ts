// import { ComponentFixture, TestBed ,inject, async  } from "@angular/core/testing";
// import { ImpairmentsHistoryComponent } from './impairments-history.component';
// import { SearchInsuredPersonService, PersonDetailsService, AlertService } from '../_services';
// import { BsDatepickerModule } from 'ngx-bootstrap';
// import { ReactiveFormsModule } from '@angular/forms';
// import { OrderPipe } from 'ngx-order-pipe';
// import { NgxSpinnerService } from 'ngx-spinner';
// import { NO_ERRORS_SCHEMA, Component } from "@angular/core";
// import { HttpClientModule } from "@angular/common/http";
// import { AngularMultiSelectModule } from "angular2-multiselect-dropdown";
// import { RouterTestingModule } from "@angular/router/testing";
// import { of, Observable } from "rxjs";

// describe('ImpairmentsHistoryComponent', () => {
//     let component: ImpairmentsHistoryComponent;
//     let fixture: ComponentFixture<ImpairmentsHistoryComponent>;
//     let mockSearchInsuredPersonService;
//     let objCurrentUser;
//     let strUserRole ;
//     let HEROES;
//     let mockPaste;
//     // let spy: jasmine.Spy;
//      let searchInsuredPersonService: SearchInsuredPersonService;
//     //  let personDetailsService: jasmine.SpyObj<PersonDetailsService>;
//     //  personDetailsService = jasmine.createSpyObj('PersonDetailsService', [
//     //         'getAllGenders'
//     //     ]);
//     beforeEach(() => {
//         HEROES = [
//             { id: 1, name: 'Hero1', strength: 8 },
//             { id: 2, name: 'Hero2', strength: 24 },
//             { id: 3, name: 'Hero3', strength: 55 },
//         ];
//         // Mocking Service
//         mockSearchInsuredPersonService = jasmine.createSpyObj([
//             'getCurrentPersonDetails'
//         ]);
//         TestBed.configureTestingModule({
//             declarations: [ImpairmentsHistoryComponent , OrderPipe],
//             imports: [ReactiveFormsModule, AngularMultiSelectModule,
//                 BsDatepickerModule.forRoot(), HttpClientModule, RouterTestingModule],
//             providers: [ AlertService ,  NgxSpinnerService , OrderPipe ,
//                 { provide: SearchInsuredPersonService, useValue: mockSearchInsuredPersonService }
//             ],
//             schemas: [NO_ERRORS_SCHEMA]
//         });
//         fixture = TestBed.createComponent(ImpairmentsHistoryComponent);
//         component = fixture.componentInstance;

//     });

//     it('should ImpairmentsHistoryComponent Defined', () => {
//         objCurrentUser = JSON.parse(localStorage.getItem("currentUser"));
//         strUserRole = objCurrentUser ? objCurrentUser.activityCode : null;
//         expect(true).toBe(true);
//         // expect(component.loginUser).toBeDefined();
//         // expect(component.boolAddUpdtImp).toBeDefined();
//     });
//     it('Should get datae', () => {
//         mockSearchInsuredPersonService.getCurrentPersonDetails.and.returnValue(of(HEROES));
//         fixture.detectChanges();
//         component.impairments = HEROES;
//         expect(component.impairments.length).toBe(3);
//     });
// });
