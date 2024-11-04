import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { InsuredPersonEnquiryComponent } from './insured-person-enquiry/insured-person-enquiry.component';
import { InsuredPersonDetailsComponent } from './insured-person-details/insured-person-details.component';
import { AuthoriseComponent } from './authorise/authorise.component';
import { AuthoriseClaimsComponent } from './authorise-claims/authorise-claims.component';
import { AuthoriseImpairmentsComponent } from './authorise-impairments/authorise-impairments.component';
import { PersonDetailsComponent } from './person-details/person-details.component';
import { NotFoundComponent } from './not-found/not-found.component';
import { ImpairmentsSavedComponent } from './impairments-saved/impairments-saved.component';
import { AuthGuard } from './_guards';
import { AppComponent } from './app.component';
import { NotAuthorizedComponent } from './not-authorized/not-authorized.component';

export const routes: Routes = [
  { path: 'index.html', component: AppComponent },
  { path: 'insured-person-enquiry', component: InsuredPersonEnquiryComponent, canActivate: [AuthGuard] },
  { path: 'insured-person-details', component: InsuredPersonDetailsComponent, canActivate: [AuthGuard] },
  { path: 'authorise', component: AuthoriseComponent, canActivate: [AuthGuard] },
  { path: 'authorise-impairment/:id', component: AuthoriseImpairmentsComponent, canActivate: [AuthGuard] },
  { path: 'authorise-claim/:id', component: AuthoriseClaimsComponent, canActivate: [AuthGuard] },
  { path: 'person-details', component: PersonDetailsComponent, canActivate: [AuthGuard] },
  { path: 'person-details/:id', component: PersonDetailsComponent, canActivate: [AuthGuard] },
  { path: 'impairments-saved', component: ImpairmentsSavedComponent, canActivate: [AuthGuard] },
  { path: 'not-authorized', component: NotFoundComponent },
  { path: 'login', component: NotFoundComponent },
  { path: 'unauthorized', component: NotAuthorizedComponent },
  { path: 'logout', component: AppComponent },
  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
