import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { AngularMultiSelectModule } from 'angular2-multiselect-dropdown';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { InsuredPersonEnquiryComponent } from './insured-person-enquiry/insured-person-enquiry.component';
import { InsuredPersonDetailsComponent } from './insured-person-details/insured-person-details.component';
import { AddInsuredPersonComponent } from './add-insured-person/add-insured-person.component';
import { AddPersonImpairmaintComponent } from './add-person-impairmaint/add-person-impairmaint.component';
import { AddPersonClaimComponent } from './add-person-claim/add-person-claim.component';
import { AddNoteComponent } from './add-note/add-note.component';
import { AddScratchpadComponent } from './add-scratchpad/add-scratchpad.component';
import { AuthoriseComponent } from './authorise/authorise.component';
import { AuthoriseClaimsComponent } from './authorise-claims/authorise-claims.component';
import { AuthoriseImpairmentsComponent } from './authorise-impairments/authorise-impairments.component';
import { PersonDetailsComponent } from './person-details/person-details.component';
import { TabsComponent } from './tabs/tabs.component';
import { ImpairmentsHistoryComponent } from './impairments-history/impairments-history.component';
import { ClaimsHistoryComponent } from './claims-history/claims-history.component';
import { NotesHistoryComponent } from './notes-history/notes-history.component';
import { ScratchpadHistoryComponent } from './scratchpad-history/scratchpad-history.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { ImpairmentsSavedComponent } from './impairments-saved/impairments-saved.component';
import { SearchInsuredPersonService, ConfirmModalService, AuthoriseService, AlertService, AuthenticationService } from './_services';
import { BsDatepickerModule } from 'ngx-bootstrap';
import { PersonInformationComponent } from './person-information/person-information.component';
import { NgxSpinnerModule } from 'ngx-spinner';
import { ConfirmModalClaimComponent } from './_component/confirm-modal-claim/confirm-modal-claim.component';
import { ConfirmModalImpairmentComponent } from './_component/confirm-modal-impairment/confirm-modal-impairment.component';
import { AlertComponent } from './_component/alert/alert.component';
import { AuthGuard } from './_guards';
import { JwtInterceptor, ErrorInterceptor } from './_helpers';
import { ConfirmModalClaimRejectComponent } from './_component/confirm-modal-claim-reject/confirm-modal-claim-reject.component';
// tslint:disable-next-line:max-line-length
import { ConfirmModalImpairmentRejectComponent } from './_component/confirm-modal-impairment-reject/confirm-modal-impairment-reject.component';

import { TooltipModule } from 'ngx-tooltip';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { OrderModule } from 'ngx-order-pipe';
import { AuthorizeImpairmentListComponent } from './authorize-impairment-list/authorize-impairment-list.component';
import { AuthorizeClaimListComponent } from './authorize-claim-list/authorize-claim-list.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    InsuredPersonEnquiryComponent,
    InsuredPersonDetailsComponent,
    AddInsuredPersonComponent,
    AddPersonImpairmaintComponent,
    AddPersonClaimComponent,
    AddNoteComponent,
    AddScratchpadComponent,
    AuthoriseComponent,
    AuthoriseClaimsComponent,
    AuthoriseImpairmentsComponent,
    PersonDetailsComponent,
    TabsComponent,
    ImpairmentsHistoryComponent,
    ClaimsHistoryComponent,
    NotesHistoryComponent,
    ScratchpadHistoryComponent,
    NotFoundComponent,
    PersonInformationComponent,
    ConfirmModalClaimComponent,
    ConfirmModalImpairmentComponent,
    AlertComponent,
    ImpairmentsSavedComponent,
    ConfirmModalClaimRejectComponent,
    ConfirmModalImpairmentRejectComponent,
    AuthorizeImpairmentListComponent,
    AuthorizeClaimListComponent,
    NotAuthorizedComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxSpinnerModule.forRoot(),
    BsDatepickerModule.forRoot(),
    AngularMultiSelectModule,
    TooltipModule,
    OrderModule
  ],
  providers: [
    AuthGuard,
    AuthenticationService,
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
