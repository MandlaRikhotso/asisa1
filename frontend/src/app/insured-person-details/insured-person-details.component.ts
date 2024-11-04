import { Component, OnInit } from '@angular/core';
import { SearchInsuredPersonService, PersonDetailsService, AuthenticationService, AlertService } from '../_services';
import { Utils } from '../utils';
import { Router } from '@angular/router';

@Component({
  selector: 'app-insured-person-details',
  templateUrl: './insured-person-details.component.html',
  styleUrls: ['./insured-person-details.component.css']
})
export class InsuredPersonDetailsComponent implements OnInit {
  perfectMatch: any;
  loginUser: any;
  InsuredPersonHistory : any = [];
  constructor(
    private searchInsuredPersonService: SearchInsuredPersonService,
    private _personDetailsService: PersonDetailsService,
    private _authenticationService: AuthenticationService,
    private _alertService: AlertService,
    private _router: Router,
  ) {
    this.loginUser = JSON.parse(localStorage.getItem('currentUser'));
    this._authenticationService.changeUser(this.loginUser);
  }

  ngOnInit() {
    const objSelPerson = JSON.parse(localStorage.getItem('personIdDetails'));
    // this.perfectMatch = objSelPerson.perfectMatch;

    if (objSelPerson == null) {
      this._router.navigate(['/insured-person-enquiry']);
    } else {
      this.perfectMatch = objSelPerson.perfectMatch;
    }
   this.getHistoryData();
  }

  getHistoryData() {
  const objIdPerson = JSON.parse(localStorage.getItem('personIdDetails'));
    if (objIdPerson) {
      this.searchInsuredPersonService.getCurrentPersonDetails(objIdPerson).subscribe(response => {
        if (Utils.chkResponseSuccess(response)) {
          this.InsuredPersonHistory =  response['body'];
                           }
      }, error => {
        this.handleServiceError(error);
      });
    }
  }
   /**
    Description: Function to handle service error
    @parameters: No
    @Return: No
  */
 handleServiceError(error) {
  // Handle service error here
  const strMsg: any = Utils.chkResponseSuccess(error);
  this._alertService.error(strMsg);
}
  /**
  Description: Function to download snapshot of the screen
  @parameters: No
  @Return: No
*/
  capPersonDetailScreen() {
    Utils.downloadSnapshot('personDetails', 'person-detail-screen');
  }
}
