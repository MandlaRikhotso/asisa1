import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { BehaviorSubject} from "rxjs";
import { environment } from "../../environments/environment";

@Injectable({
  providedIn: "root"
})
export class AuthoriseService {
 constructor(private http: HttpClient) { }
  ///// User information //////
  setNotificationCount = new BehaviorSubject(null);
  getNotificationCount = this.setNotificationCount.asObservable();

  changeNotificationCount(objData) {
    this.setNotificationCount.next(objData);
  }
  getAuthoriseImp() {
    return this.http.get(`${environment.apiEndpoint}/impairments/authorize`, {
      observe: 'response'
    });
  }
  getAuthoriseClaim() {
    return this.http.get(`${environment.apiEndpoint}/claims/authorize`, {
      observe: 'response'
    });
  }

  approveImpairments(updateReferenceData) {
    return this.http.post(
      environment.apiEndpoint + '/impairments/approval',
      updateReferenceData,
      { observe: 'response' }
    );
  }

  approveClaims(updateReferenceData) {
    return this.http.post(
      environment.apiEndpoint + '/claims/approval',
      updateReferenceData,
      { observe: 'response' }
    );
  }

  getAuthoriseImpairmentDetails(notificationID) {
    return this.http.get(
      environment.apiEndpoint + '/impairments/authorize/' + notificationID,
      { observe: 'response' }
    );
  }

  getAuthoriseClaimDetails(notificationID) {
    return this.http.get(
      environment.apiEndpoint + '/claims/authorize/' + notificationID,
      { observe: 'response' }
    );
  }
  getAuthoriseCount() {
    return this.http.get(`${environment.apiEndpoint}/claims/authorizecount`);
  }
  getBuildVersion() {
    return this.http.get(`${environment.apiEndpoint}/buildProperties`);
  }
}
