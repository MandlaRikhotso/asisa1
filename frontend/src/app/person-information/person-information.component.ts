import { Component, OnInit, Input } from '@angular/core';
import { SearchInsuredPersonService, PersonDetailsService, AlertService } from '../_services';
import { Utils } from '../utils';

@Component({
  selector: 'app-person-information',
  templateUrl: './person-information.component.html',
  styleUrls: ['./person-information.component.css']
})

export class PersonInformationComponent implements OnInit {
  personInfo: any;
  @Input() insuredPersonInfo: any;
  constructor(
    private _searchInsuredPersonService: SearchInsuredPersonService,
    private _personDetailsService: PersonDetailsService,
    private _alertService: AlertService,
  ) { }

  ngOnInit() {
    this._personDetailsService.getPersonInfo.subscribe(response => {
      if (response) {
        const pgName = localStorage.getItem('pageName');
        if (pgName === 'Enquiry') {
          this.personInfo = null;
        } else {

          this.personInfo = Utils.setInsuredPersonInfo(response, true);
        }
      } else {
        // this.personInformation();
        setTimeout(() => {
          this.personInformation();
        }, 2000);
      }
    });
  }
  /**
    Description: Function to get person details
    @parameters: No
    @Return: No
  */
  personInformation() {
    const pgName = localStorage.getItem('pageName');
    if (pgName === 'Enquiry') {
      this.personInfo = null;
    } else {
      if (this.insuredPersonInfo) {
        this.personInfo = Utils.setInsuredPersonInfo(this.insuredPersonInfo['insuredPerson'], true);
      }
    }
  }
}
