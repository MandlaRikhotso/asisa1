import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class TabserviceService {
  status: boolean = true;
  // showPersonInfo: boolean = false;
  personDetail = {
    'showPersonInfo': false,
    'personInfo': {
      idType: '',
      idNumber: '',
      dateOfBirth: '',
      gender: '',
      initials: '',
      surname: '',
      givenName1: '',
      givenName2: '',
      givenName3: '',
    }
  };
  private setshowAddPersonTab = new BehaviorSubject(this.status);
  private setshowImpairmentTab = new BehaviorSubject(this.status);
  private statusSource = new BehaviorSubject(this.status);
  private status1Source = new BehaviorSubject(this.status);
  private setshowPersonInfo = new BehaviorSubject(this.personDetail);
  currentStatus = this.statusSource.asObservable();
  getaddPersonTabStatus = this.setshowAddPersonTab.asObservable();
  getshowImpairmentTab = this.setshowImpairmentTab.asObservable();
  current1Status = this.status1Source.asObservable();
  getshowPersonInfo = this.setshowPersonInfo.asObservable();
  constructor() { }


  changeAddPersonTabStatus(status) {
    this.setshowAddPersonTab.next(status);
  }
  changeImpairmentTabStatus(status) {
    this.setshowImpairmentTab.next(status);
  }
  changeStatus(status) {
    this.statusSource.next(status);
  }
  change1Status(status) {
    this.status1Source.next(status);
  }
  changeshowPersonInfo(personDetail) {
    this.setshowPersonInfo.next(personDetail);
  }
}
