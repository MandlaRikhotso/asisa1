import { Component, OnInit } from "@angular/core";
import { NgxSpinnerService } from "ngx-spinner";
import { AlertService, AuthenticationService } from "../_services";
@Component({
  selector: "app-authorise",
  templateUrl: "./authorise.component.html",
  styleUrls: ["./authorise.component.css"]
})
export class AuthoriseComponent implements OnInit {
  count: any;
  successStatus: any;
  objCurrentUser: any;
  message: any;
  constructor(
    private alertService: AlertService,
    private _authenticationService: AuthenticationService,
    private spinner: NgxSpinnerService
  ) {
    this.objCurrentUser = JSON.parse(localStorage.getItem("currentUser"));
    this._authenticationService.changeUser(this.objCurrentUser);
  }

  ngOnInit() {}
}
