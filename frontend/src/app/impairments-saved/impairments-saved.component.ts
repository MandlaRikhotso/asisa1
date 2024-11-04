import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Utils } from '../utils';
import { PersonDetailsService } from '../_services';
@Component({
  selector: 'app-impairments-saved',
  templateUrl: './impairments-saved.component.html',
  styleUrls: ['./impairments-saved.component.css']
})
export class ImpairmentsSavedComponent implements OnInit {
  arrTabData: any;
  objTabInfo: any;
  subActive: boolean = false;
  strUserRole: any;
  constructor(
    private router: Router,
    private _personDetailsService: PersonDetailsService
  ) { }

  ngOnInit() { }

  redirectToAddPerson() {
    // const obj = Utils.defaultTabs();
    this._personDetailsService.getTabInformation.subscribe(response => {
      if (response) {
        this.arrTabData = response.tabs;
        if (this.arrTabData) {
          this.arrTabData[1].active = false;
          this.arrTabData[2].active = false;
        }
        this.objTabInfo = {
          'tabs': this.arrTabData,
        };
      }
    });
    this._personDetailsService.changeTabInformation(this.objTabInfo);
    localStorage.setItem("selectedImpId", null);
    setTimeout(() => {
      this.router.navigate(['/person-details']);
    }, 2000);
  }

  redirectToEnquiry() {
    const obj = Utils.defaultTabs();
    this._personDetailsService.changeTabInformation(obj);
    localStorage.setItem("selectedImpId", null);
    setTimeout(() => {
      this.router.navigate(['/insured-person-enquiry']);
    }, 2000);
  }

}
