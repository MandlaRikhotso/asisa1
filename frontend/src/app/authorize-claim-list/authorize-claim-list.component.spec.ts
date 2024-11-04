import { ComponentFixture, TestBed } from "@angular/core/testing";
import { AuthorizeClaimListComponent } from "./authorize-claim-list.component";
import {
  SearchInsuredPersonService,
  PersonDetailsService,
  AlertService,
  AuthoriseService,
  AuthenticationService
} from "../_services";
import { NgxSpinnerService } from "ngx-spinner";
import { NO_ERRORS_SCHEMA } from "@angular/core";
import { HttpClientModule } from "@angular/common/http";
import { RouterTestingModule } from "@angular/router/testing";
import { of } from "rxjs";
import { Router } from "@angular/router";
describe("AuthorizeClaimListComponent", () => {
  let component: AuthorizeClaimListComponent;
  let fixture: ComponentFixture<AuthorizeClaimListComponent>;
  let authoriseservice;
  let router: Router;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AuthorizeClaimListComponent],
      imports: [HttpClientModule, RouterTestingModule.withRoutes([])],
      // tslint:disable-next-line:max-line-length
      providers: [
        SearchInsuredPersonService,
        AlertService,
        NgxSpinnerService,
        PersonDetailsService,
        AuthoriseService,
        AuthenticationService,
        Location
      ],
      schemas: [NO_ERRORS_SCHEMA]
    });
    router = TestBed.get(Router);
    //location = TestBed.get(Location);
    fixture = TestBed.createComponent(AuthorizeClaimListComponent);
    component = fixture.componentInstance;
    authoriseservice = fixture.debugElement.injector.get(AuthoriseService);
  });

  //   it('navigate to "" redirects you to /home', fakeAsync(() => { (1)
  //     router.navigate(['']); (2)
  //     tick(); (3)
  //     expect(location.pa).toBe('/home'); (4)
  //   }));

  // fit("should navigate", () => {
  //   let component = fixture.componentInstance;
  //   let navigateSpy = spyOn(router, "navigate");

  //   component.getAuthoriseClaim("31231");
  //   expect(navigateSpy).toHaveBeenCalledWith(["/authorise-claim"]);
  // });

  // it("should call getAuthClaims", () => {
  //   const response = {
  //     body: { claims: [] },
  //     ok: true,
  //     status: 200,
  //     statusText: "OK",
  //     type: 4
  //   };
  //   const currentUser = {
  //     authorities: [{ activityCode: "AUTH_UPDATES_CLAIMS" }]
  //   };
  //   localStorage.setItem("currentUser", JSON.stringify(currentUser));
  //   spyOn(authoriseservice, "getAuthoriseClaim").and.returnValue(of(response));
  //   component.getAuthClaims();
  //   fixture.detectChanges();
  //   expect(component.claims).toEqual(response["body"]["claims"]);
  // });
});
