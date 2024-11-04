import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { AuthoriseService, PersonDetailsService, AuthenticationService } from '../_services';
import { HttpClient } from '@angular/common/http';
import { Utils } from '../utils';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})


export class HeaderComponent implements OnInit {
  notificationCount: number = 0;
  count: any;
  route: string;
  objTabInfo: any;
  loadedCharacter: any;
  boolAuth: boolean = false;
  boolEnquiry: boolean = false;
  loginUser: any;
  strCurrentUserName: string;
  strCurrentGivenName: string;
  strCurrentSurName: string;
  strUserRole: string;
  constructor(
    private location: Location,
    private router: Router,
    private authoriseService: AuthoriseService,
    private personDetailsService: PersonDetailsService,
    private http: HttpClient,
    private _authenticationService: AuthenticationService,
  ) {
  }

  ngOnInit() {
    this._authenticationService.getLoginUser.subscribe(response => {
      if (response) {
        this.loginUser = JSON.parse(localStorage.getItem('currentUser'));
        this.strUserRole = this.loginUser['authorities'][0].authority;
        this.strCurrentUserName = this.loginUser['username'];
        this.strCurrentGivenName = this.loginUser['givenName'];
        this.strCurrentSurName = this.loginUser['surname'];
        if (Utils.checkUserPermission('ENQUIRY')) {
          this.boolEnquiry = true;
        }
        if (Utils.checkUserPermission('AUTH_UPDATES_IMPAIRMENTS') || Utils.checkUserPermission('AUTH_UPDATES_CLAIMS')) {
          this.boolAuth = true;
          if (this.boolAuth === true) {
            this.authoriseService.getAuthoriseCount().subscribe(data => {
              this.count = (data > 0) ? data : 0;
              this.authoriseService.changeNotificationCount(this.count);
            });
          }
        }
      }
    }, error => {

    });

    this.router.events.subscribe((val) => {
      if (this.location.path() !== '') {
        this.route = this.location.path();

      } else {
        this.route = '404';
      }
    });
    this.authoriseService.getNotificationCount.subscribe(response => {
      this.notificationCount = response;
    });
  }
  redirectTo() {
    if (this.strUserRole) {
      localStorage.setItem('exitingPersonInfo', 'null');
      localStorage.setItem('insuredPersonId', 'null');
      setTimeout(() => {
        this.router.navigate(["/insured-person-enquiry"]);
      }, 2000);
    }
    else {
    document.location.href = '/';
    }
  }
  logout() {
    this.boolAuth = false;
    this.boolEnquiry = false;
    this.loginUser = null;
    localStorage.clear();
    document.location.href = '/logout';
  }
}
