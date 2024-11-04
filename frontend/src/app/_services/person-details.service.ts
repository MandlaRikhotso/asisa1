import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import {
    Impairment, Impairmentbenefit, Symbol, Gender, Pretitle, SpecialInvestigate,
    ClaimTypes, ClaimCategory, ClaimReason, ClaimStatus, PaymentMethod, CauseOfEvents
} from '../_models';
import { environment } from '../../environments/environment';

const httpOptions = {
    // tslint:disable-next-line:max-line-length
    headers: new HttpHeaders({ 'x-ibm-client-id': 'fc703459-3f79-4783-ba29-1a38f94e46a7', 'x-ibm-client-secret': 'dM2nO6sH0wP6uP0sD3oM0uO6sI2sB0xW1jQ0nW7dD3dR1iP6mJ', 'Content-Type': 'application/json' })
};

@Injectable({
    providedIn: 'root'
})

export class PersonDetailsService {
    constructor(private http: HttpClient) { }

    ///// Tabs information //////
    setTabInformation = new BehaviorSubject(null);
    getTabInformation = this.setTabInformation.asObservable();

    ///// Person information  /////////
    setPersonInfo = new BehaviorSubject(null);
    getPersonInfo = this.setPersonInfo.asObservable();

    ///// Person information  /////////
    setFormStatus = new BehaviorSubject(null);
    getFormStatus = this.setFormStatus.asObservable();

    ///// Clear Data  /////////
    setClearData = new BehaviorSubject(null);
    getClearData = this.setClearData.asObservable();

    ///// Clear Data  /////////
    setPolicyData = new BehaviorSubject(null);
    getPolicyData = this.setPolicyData.asObservable();

    ///// Clear Data  /////////
    setEditPolicyData = new BehaviorSubject(null);
    getEditPolicyData = this.setEditPolicyData.asObservable();

    setPolicyScratchpadData = new BehaviorSubject(null);
    getPolicyScratchpadData = this.setPolicyScratchpadData.asObservable();

    changeTabInformation(objTabInfo) {
        this.setTabInformation.next(objTabInfo);
    }
    changeFormStatus(obj) {
        this.setFormStatus.next(obj);
    }
    changePersonInfo(objPerson) {
        this.setPersonInfo.next(objPerson);
    }
    changeClearData(objClearData) {
        this.setClearData.next(objClearData);
    }

    changePolicyData(objPolicyData) {
        this.setPolicyData.next(objPolicyData);
    }
    changeEditPolicyData(objPolicyData) {
        this.setEditPolicyData.next(objPolicyData);
    }
    changePolicyScratchpadData(objPolicyData) {
        this.setPolicyScratchpadData.next(objPolicyData);
    }

    getAllImpairments() {
        return this.http.get<Impairment[]>(`${environment.apiEndpoint}/refdata/impairmentCodes`, {
            observe: 'response'
        });
    }
    getAllImpairmentBenefits() {
        return this.http.get<Impairmentbenefit[]>(`${environment.apiEndpoint}/refdata/policyTypes`, {
            observe: 'response'
        });
    }

    getAllSymbols() {
        return this.http.get<Symbol[]>(`${environment.apiEndpoint}/refdata/lifeSymbols`, {
            observe: 'response'
        });
    }
    getAllGenders() {
        return this.http.get<Gender[]>(`${environment.apiEndpoint}/refdata/genders`, {
        observe: 'response'
        });
    }
    getAllPretitles() {
        return this.http.get<Pretitle[]>(`${environment.apiEndpoint}/refdata/titles`, {
            observe: 'response'
        });
    }
    getAllSpecialInvestigates() {
        return this.http.get<SpecialInvestigate[]>(`${environment.apiEndpoint}/refdata/lifeSpecs`, {
            observe: 'response'
        });
    }
    getPersonDetailsBYCustomerId(objData) {
        return this.http.post(environment.gcsEndpoint, objData, httpOptions);
    }
    getCurrentPersonImpairmentDetails(objIdPerson) {
        return this.http.post(environment.apiEndpoint + '/iphistory/'
            + objIdPerson.identityTypeCode + '/' + objIdPerson.identityNumber + '/' + objIdPerson.personID, '', {
                observe: 'response'
            });
    }

    getCurrentPersonCliamDetails(objIdPerson) {
        return this.http.post(environment.apiEndpoint
            + '/iphistory/' + objIdPerson.identityTypeCode + '/' + objIdPerson.identityNumber + '/' + objIdPerson.personID, '', {
                observe: 'response'
            });
    }
    addPersonImpairments(objData) {
        return this.http.post(environment.apiEndpoint + '/impairments/notifiableImpairments', objData, {
            observe: 'response'
        });
    }
    addPersonClaims(objData) {
        return this.http.post(environment.apiEndpoint + '/claims/notifiableClaims', objData, {
            observe: 'response'
        });
    }
    getAllClaimTypes() {
        return this.http.get<ClaimTypes[]>(`${environment.apiEndpoint}/refdata/claimTypes`, {
            observe: 'response'
        });
    }
    getCategoryIds() {
        return this.http.get<ClaimCategory[]>(`${environment.apiEndpoint}/refdata/claimCategories`, {
            observe: 'response'
        });
    }
    getClaimReasons() {
        return this.http.get<ClaimReason[]>(environment.apiEndpoint + 'claim-reasons.json', {
            observe: 'response'
        });
    }
    getClaimStatus() {
        return this.http.get<ClaimStatus[]>(`${environment.apiEndpoint}/refdata/claimStatuses`, {
            observe: 'response'
        });
    }
    getPayMethods() {
        return this.http.get<PaymentMethod[]>(`${environment.apiEndpoint}/refdata/paymentMethods`, {
            observe: 'response'
        });
    }
    getCauseOfEvents() {
        return this.http.get<CauseOfEvents[]>(`${environment.apiEndpoint}/refdata/claimCauses`, {
            observe: 'response'
        });
    }
    updatePersonImpairment(objData) {
        return this.http.put(environment.apiEndpoint + '/impairments/notifiableImpairment', objData, {
            observe: 'response'
        });
    }
    updatePersonClaim(objData) {
        return this.http.put(environment.apiEndpoint + '/claims/notifiableClaim', objData, {
            observe: 'response'
        });
    }
}
