import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
    providedIn: 'root',
})

export class ConfirmModalService {
    obj: any;

    constructor(private http: HttpClient) { }

    private setObj = new BehaviorSubject(this.obj);
    getObj = this.setObj.asObservable();

    changeObj(obj) {
        this.setObj.next(obj);
    }

    delImpairment(objImpairment) {
        return this.http.put(environment.apiEndpoint + '/impairments/notifiableImpairment', objImpairment);
    }

    delClaim(objClaim) {
        return this.http.put(environment.apiEndpoint + '/claims/notifiableClaim', objClaim);
    }

}