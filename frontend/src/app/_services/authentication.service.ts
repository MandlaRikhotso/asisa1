
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class AuthenticationService {


    constructor(private http: HttpClient) { }

    ///// User information //////
    setLoginUser = new BehaviorSubject(null);
    getLoginUser = this.setLoginUser.asObservable();

    changeUser(objUser) {
        this.setLoginUser.next(objUser);
    }

    login() {
        return this.http.get(environment.apiEndpoint + '/admin/getUserAuthorities',{
            observe: 'response'
        });
    }
}
