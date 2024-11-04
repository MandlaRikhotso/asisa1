import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { IdTypes } from '../_models';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root',
})

export class SearchInsuredPersonService {
    objData: any;
    objInfo: any;

    constructor(private http: HttpClient) { }

    private setPersonDetails = new BehaviorSubject(this.objData);
    getPersonDetails = this.setPersonDetails.asObservable();

    private setPersonSearchInfo = new BehaviorSubject(this.objInfo);
    getPersonSearchInfo = this.setPersonSearchInfo.asObservable();

    changePersonDetails(objData) {
        this.setPersonDetails.next(objData);
    }

    changePersonSearchInfo(objInfo) {
        this.setPersonSearchInfo.next(objInfo);
    }

    getIdTypes() {
        return this.http.get<IdTypes[]>(`${environment.apiEndpoint}/refdata/identityTypes`, {
            observe: 'response'
        });
    }

    getAllPersons(searchParams) {
        return this.http.post(environment.apiEndpoint + '/enquiry/enquireInsuredPersons', searchParams, {
            observe: 'response'
        });
    }
    getExistingPerson(searchParams) {
        return this.http.post(environment.apiEndpoint + '/enquiry/enquireInsuredPersonExists', searchParams, {
            observe: 'response'
        });
    }

    getCurrentPersonDetails(objIdPerson) {
        return this.http.post(environment.apiEndpoint
            + '/iphistory/' + objIdPerson.identityTypeCode + '/' + objIdPerson.identityNumber + '/' + objIdPerson.personID, '', {
                observe: 'response'
            });
    }

    getIdNumbers(idNumber) {
        return this.http.get(environment.apiEndpoint + '/autosuggest/identityNumber/' + idNumber, {
            observe: 'response'
        });
    }

    getSurnames(surname) {
        return this.http.get(environment.apiEndpoint + '/autosuggest/surname/' + surname, {
            observe: 'response'
        });
    }
}
