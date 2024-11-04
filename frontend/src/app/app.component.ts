import { Component, OnInit } from "@angular/core";
import { Router, ActivatedRoute } from "@angular/router";
import { NgxSpinnerService } from "ngx-spinner";
import { AlertService, AuthenticationService } from "./_services";
import { first } from "rxjs/operators";
import { Utils } from "./utils";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit {
  param: any;
  userdata: any;
  successMsg: any;
  constructor(
    private router: Router,
    private spinner: NgxSpinnerService,
    private alertService: AlertService,
    private authenticationService: AuthenticationService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.spinner.show();
    localStorage.clear();
    setTimeout(() => {
    this.authenticationService
      .login()
      .pipe(first())
      .subscribe(data => {
        if (Utils.chkResponseSuccess(data)) {
          this.userdata = data['body'];
          localStorage.setItem("currentUser", JSON.stringify(data['body']));
          this.authenticationService.changeUser(data['body']);
          this.router.navigate(["/insured-person-enquiry"]);
          this.spinner.hide();
        }
        // } else {
        //   this.router.navigate(["/not-authorized"]);
        //   this.spinner.hide();
        // }
      }, error => {
        this.handleServiceError(error);
        this.spinner.hide();
      });
    });
  }
  handleServiceError(error) {
    // Handle service error here
    const strMsg: any = Utils.chkResponseSuccess(error);
    // this._alertService.error(strMsg);
    localStorage.setItem("successMsg", strMsg);
    this.router.navigate(["/login"]);
  }
  onActivate(_event) {
    window.scroll(0, 0);
  }
}
