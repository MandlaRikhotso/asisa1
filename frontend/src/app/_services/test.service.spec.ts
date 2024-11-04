import { TestBed, async } from '@angular/core/testing';
import { HttpClientModule } from '@angular/common/http';
import {
    HttpClientTestingModule,
    HttpTestingController,
} from '@angular/common/http/testing';
import { SearchInsuredPersonService, PersonDetailsService, AuthoriseService, ConfirmModalService } from 'src/app/_services';

import { environment } from '../../environments/environment';
describe('UsersService', () => {
    let searchInsuredPersonService: SearchInsuredPersonService;
    let personDetailsService: PersonDetailsService;
    let authoriseService: AuthoriseService;
    let confirmModalService: ConfirmModalService;
    let httpMock: HttpTestingController;
    beforeEach(() => {
        TestBed.configureTestingModule({
            imports: [HttpClientModule, HttpClientTestingModule],
        });

        searchInsuredPersonService = TestBed.get(SearchInsuredPersonService);
        personDetailsService = TestBed.get(PersonDetailsService);
        authoriseService = TestBed.get(AuthoriseService);
        confirmModalService = TestBed.get(ConfirmModalService);
        httpMock = TestBed.get(HttpTestingController);
    });

    it('SearchInsuredPersonService should be created', () => {
        expect(searchInsuredPersonService).toBeTruthy();
    });


    it('should get the IdTypes successful', async(() => {
        searchInsuredPersonService.getIdTypes().subscribe((data: any = []) => {
            expect(data.identityTypes).toBeDefined();
            expect(data.identityTypes.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/identityTypes`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the Gender successful', async(() => {
        personDetailsService.getAllGenders().subscribe((data: any = []) => {
            expect(data.genders).toBeDefined();
            expect(data.genders.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/genders`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));
    it('should get the Titles successful', async(() => {
        personDetailsService.getAllPretitles().subscribe((data: any = []) => {
            expect(data.titles).toBeDefined();
            expect(data.titles.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/titles`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the Impairment successful', async(() => {
        personDetailsService.getAllImpairments().subscribe((data: any = []) => {
            expect(data.impairmentCodes).toBeDefined();
            expect(data.impairmentCodes.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/impairmentCodes`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the ImpairmentBenefit successful', async(() => {
        personDetailsService.getAllImpairmentBenefits().subscribe((data: any = []) => {
            expect(data.policyBenefits).toBeDefined();
            expect(data.policyBenefits.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/policyBenefits`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the Symbol successful', async(() => {
        personDetailsService.getAllSymbols().subscribe((data: any = []) => {
            expect(data.symbols).toBeDefined();
            expect(data.symbols.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/symbols`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the SpecialInvestigation successful', async(() => {
        personDetailsService.getAllSpecialInvestigates().subscribe((data: any = []) => {
            expect(data.specialInvestigations).toBeDefined();
            expect(data.specialInvestigations.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/specialInvestigations`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the CategoryStatus successful', async(() => {
        personDetailsService.getClaimStatus().subscribe((data: any = []) => {
            expect(data.claimStatuses).toBeDefined();
            expect(data.claimStatuses.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/claimStatuses`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the CategoryIds successful', async(() => {
        personDetailsService.getCategoryIds().subscribe((data: any = []) => {
            expect(data.claimCategories).toBeDefined();
            expect(data.claimCategories.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/claimCategories`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the CauseEvent successful', async(() => {
        personDetailsService.getCauseOfEvents().subscribe((data: any = []) => {
            expect(data.claimCauses).toBeDefined();
            expect(data.claimCauses.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/claimCauses`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));

    it('should get the Paymethod successful', async(() => {
        personDetailsService.getPayMethods().subscribe((data: any = []) => {
            expect(data.paymentMethods).toBeDefined();
            expect(data.paymentMethods.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/refdata/paymentMethods`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));
    it('should get impaiment history data', async(() => {
        // tslint:disable-next-line:no-unused-expression
        let identityNumberObj = {
            identityTypeCode: 'SA ID',
            identityNumber: '111',
            personID: '123'
        };
        personDetailsService.getCurrentPersonImpairmentDetails(identityNumberObj)
            .subscribe((data: any) => {
                expect(data.identityNumber).toBe('identityNumberObj.identityNumber');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            // tslint:disable-next-line:max-line-length
            `${environment.apiEndpoint}/iphistory/${identityNumberObj.identityTypeCode}/${identityNumberObj.identityNumber}/${identityNumberObj.personID}`,
            'post to api'
        );
        expect(req.request.method).toBe('POST');
        httpMock.verify();
    }));

    it('should get claim history data', async(() => {
        // tslint:disable-next-line:no-unused-expression
        let identityNumberObj = {
            identityTypeCode: 'SA ID',
            identityNumber: '111',
            personID: '123'
        };
        personDetailsService.getCurrentPersonCliamDetails(identityNumberObj)
            .subscribe((data: any) => {
                expect(data.identityNumber).toBe('identityNumberObj.identityNumber');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            // tslint:disable-next-line:max-line-length
            `${environment.apiEndpoint}/iphistory/${identityNumberObj.identityTypeCode}/${identityNumberObj.identityNumber}/${identityNumberObj.personID}`,
            'post to api'
        );
        expect(req.request.method).toBe('POST');
        httpMock.verify();
    }));

    it('should add impairment data', async(() => {
        // tslint:disable-next-line:no-unused-expression
        let impairmentData = {
            'insuredPerson': {
                'identityTypeCode': [
                    {
                        'code': '1',
                        'description': 'SA ID'
                    }
                ],
                'identityNumber': '123456789',
                'surname': 'Moolamalla',
                'givenName1': 'Tirumal',
                'givenName2': null,
                'givenName3': null,
                'dateOfBirth': '01/07/2018',
                'gender': [
                    {
                        'code': '1',
                        'description': 'MALE'
                    }
                ],
                'addressLine1': null,
                'addressLine2': null,
                'addressLine3': null,
                'postalCode': 0,
                'title': [
                    {
                        'code': 'Mr',
                        'description': null
                    }
                ]
            },
            'notifications': [
                {
                    'notes': [
                        {
                            'isScratchpad': false,
                            'noteText': 'RISK/DEATH BENEFIT',
                            'scratchpad': false
                        },
                        {
                            'isScratchpad': true,
                            'noteText': 'RISK/DEATH BENEFIT',
                            'scratchpad': true
                        }
                    ],
                    'notifiableClaims': [],
                    'notifiableImpairments': [
                        {
                            'timeSignal': [
                                {
                                    'code': '1',
                                    'description': '2010'
                                }
                            ],
                            'impairmentCode': [
                                {
                                    'code': 'AD01',
                                    'description': 'WEIGHT: OVERWEIGHT',
                                    'impairmentCodeGroupId': 'OLD',
                                    'codes': 'AD01 - WEIGHT: OVERWEIGHT'
                                }
                            ],
                            'readings': '11/21',
                            'specialInvestigationCode': [
                                {
                                    'code': 'W0',
                                    'description': 'Medical Report'
                                }
                            ],
                            'symbolCode': [
                                {
                                    'code': 'N',
                                    'description': 'Non-Medical'
                                }
                            ]
                        }
                    ],
                    'policyBenefit': [
                        {
                            'code': '1',
                            'description': 'RISK/DEATH BENEFIT'
                        }
                    ],
                    'policyNumber': '111'
                }
            ]
        };
        personDetailsService.addPersonImpairments(impairmentData)
            .subscribe((data: any) => {
                expect(data).toBe('impairmentData');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/notifiableImpairments`,
            'post to api'
        );
        expect(req.request.method).toBe('POST');
        httpMock.verify();
    }));


    it('should add claim data', async(() => {
        let claimData = {
            'insuredPerson': {
                'identityTypeCode': [
                    {
                        'code': '1',
                        'description': 'SA ID'
                    }
                ],
                'identityNumber': '123456789',
                'surname': 'Moolamalla',
                'givenName1': 'Tirumal',
                'givenName2': null,
                'givenName3': null,
                'dateOfBirth': '01/07/2018',
                'gender': [
                    {
                        'code': '1',
                        'description': 'MALE'
                    }
                ],
                'addressLine1': null,
                'addressLine2': null,
                'addressLine3': null,
                'postalCode': 0,
                'title': [
                    {
                        'code': 'Mr',
                        'description': null
                    }
                ]
            },
            'notifications': [
                {
                    'notes': [
                        {
                            'isScratchpad': false,
                            'noteText': 'RISK/DEATH BENEFIT',
                            'scratchpad': false
                        },
                        {
                            'isScratchpad': true,
                            'noteText': 'RISK/DEATH BENEFIT',
                            'scratchpad': true
                        }
                    ],
                    'notifiableClaims': [
                        {
                            'bi1663Number': '111',
                            'claimCategoryCode': [
                                {
                                    'code': '4303',
                                    'description': 'EARLY CLM POLICY IN FORCE < 1 YRS FROM DOE, ETC'
                                }
                            ],
                            'claimReasonCode': [
                                {
                                    'code': 'AD01',
                                    'description': 'WEIGHT: OVERWEIGHT',
                                    'impairmentCodeGroupId': 'OLD'
                                }
                            ],
                            'claimStatusCode': [
                                {
                                    'code': '15',
                                    'description': 'BASIC PAID'
                                }
                            ],
                            'eventCauseCode': [
                                {
                                    'code': '3',
                                    'description': 'ACCIDENT IN THE HOME'
                                }
                            ],
                            'eventDate': '07/02/2019',
                            'eventDeathCertificateNo': '111',
                            'eventDeathPlace': 'aaa',
                            'paymentMethodCode': [
                                {
                                    'code': '10',
                                    'description': 'EFT'
                                }
                            ],
                            'updateReason': ''
                        }
                    ],
                    'notifiableImpairments': [],
                    'policyBenefit': [
                        {
                            'code': '1',
                            'description': 'RISK/DEATH BENEFIT'
                        }
                    ],
                    'policyNumber': '111'
                }
            ]
        };
        personDetailsService.addPersonClaims(claimData)
            .subscribe((data: any) => {
                expect(data).toBe('impairmentData');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/notifiableClaims`,
            'post to api'
        );
        expect(req.request.method).toBe('POST');
        httpMock.verify();
    }));

    it('should update impaiment data', () => {
        let updateImpairmentData = {
            'delete': false,
            'impairmentCode': [
                {
                    'code': 'AD01',
                    'description': 'WEIGHT: OVERWEIGHT',
                    'codes': 'AD01 - WEIGHT: OVERWEIGHT'
                }
            ],
            'notes': [
                {
                    'isScratchpad': false,
                    'noteText': 'RISK/DEATH BENEFIT',
                    'scratchpad': false
                },
                {
                    'isScratchpad': true,
                    'noteText': 'RISK/DEATH BENEFIT',
                    'scratchpad': true
                }
            ],
            'notifiableImpairmentID': '1',
            'notificationID': '1',
            'readings': '11/21',
            'specialInvestigationCode': [
                {
                    'code': 'W0',
                    'description': 'Medical Report'
                }
            ],
            'symbolCode': [
                {
                    'code': 'N',
                    'description': 'Non-Medical'
                }
            ],
            'timeSignal': [
                {
                    'code': '1',
                    'description': '2010'
                }
            ],
            'updateReason': 'aa'
        };
        personDetailsService.updatePersonImpairment(updateImpairmentData)
            .subscribe((data: any) => {
                expect(data).toBe('updateImpairmentData');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/notifiableImpairment`,
            'put to api'
        );
        expect(req.request.method).toBe('PUT');
        httpMock.verify();
    });

    it('should update claim data', () => {
        let updateClaimData = {
            'bi1663Number': '111',
            'claimCategoryCode': [
                {
                    'code': '4303',
                    'description': 'EARLY CLM POLICY IN FORCE < 1 YRS FROM DOE, ETC'
                }
            ],
            'claimReasonCode': [
                {
                    'code': 'AD01',
                    'description': 'WEIGHT: OVERWEIGHT'
                }
            ],
            'claimStatusCode': [
                {
                    'code': '10',
                    'description': 'REPUDIATED WITH EX GRATIA'
                }
            ],
            'claimType': [
                {
                    'code': '1',
                    'description': 'RISK/DEATH BENEFIT'
                }
            ],
            'delete': false,
            'eventCauseCode': [
                {
                    'code': '3',
                    'description': 'ACCIDENT IN THE HOME'
                }
            ],
            'eventDate': '02/07/2019',
            'eventDeathCertificateNo': '111',
            'eventDeathPlace': 'aaa',
            'notes': [
                {
                    'isScratchpad': false,
                    'noteText': 'RISK/DEATH BENEFIT',
                    'scratchpad': false
                },
                {
                    'isScratchpad': true,
                    'noteText': 'RISK/DEATH BENEFIT',
                    'scratchpad': true
                }
            ],
            'notifiableClaimRefID': '1',
            'notificationID': '1',
            'paymentMethodCode': [
                {
                    'code': '10',
                    'description': 'EFT'
                }
            ],
            'updateReason': 'aa'
        };
        personDetailsService.updatePersonClaim(updateClaimData)
            .subscribe((data: any) => {
                expect(data).toBe('updateClaimData');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/notifiableClaim`,
            'put to api'
        );
        expect(req.request.method).toBe('PUT');
        httpMock.verify();
    });
    it('should delete impairment data', () => {
        let deleteImpairmentData = {
            'delete': true,
            'impairmentCode': [],
            'notes': [],
            'notifiableImpairmentID': 1,
            'notificationID': 1,
            'readings': '',
            'specialInvestigationCode': [],
            'symbolCode': [],
            'timeSignal': [],
            'updateReason': 'aa'
        };
        confirmModalService.delImpairment(deleteImpairmentData)
            .subscribe((data: any) => {
                expect(data).toBe('deleteImpairmentData');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/notifiableImpairment`,
            'put to api'
        );
        expect(req.request.method).toBe('PUT');
        httpMock.verify();
    });
    it('should delete claim data', () => {
        let deleteClaimData = {
            'bi1663Number': '',
            'claimCategoryCode': [],
            'claimReasonCode': [],
            'claimStatusCode': [],
            'claimType': [],
            'delete': true,
            'eventCauseCode': [],
            'eventDate': '',
            'eventDeathCertificateNo': '',
            'eventDeathPlace': '',
            'notes': [],
            'notifiableClaimRefID': 2,
            'paymentMethodCode': [],
            'updateReason': 'aa'
        };
        confirmModalService.delClaim(deleteClaimData)
            .subscribe((data: any) => {
                expect(data).toBe('deleteImpairmentData');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/notifiableClaim`,
            'put to api'
        );
        expect(req.request.method).toBe('PUT');
        httpMock.verify();
    });

    it('should get search person data', async(() => {
        // tslint:disable-next-line:no-unused-expression
        let personSearchData = {
            'dateOfBirth': '',
            'identityNumber': 123456789,
            'identityTypeCode': '1',
            'surname': ''
        };
        searchInsuredPersonService.getAllPersons(personSearchData)
            .subscribe((data: any) => {
                expect(data.identityNumber).toBe('personSearchData.identityNumber');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/enquiry/enquireInsuredPersons`,
            'post to api'
        );
        expect(req.request.method).toBe('POST');
        httpMock.verify();
    }));
    it('should get the Authorize impairment List successful', async(() => {
        authoriseService.getAuthoriseImp().subscribe((res: any = []) => {
            expect(res.data).toBeDefined();
            // expect(data.claimCauses.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/impairments/authorize`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));
    it('should get the Authorize claim List successful', async(() => {
        authoriseService.getAuthoriseClaim().subscribe((res: any = []) => {
            expect(res.data).toBeDefined();
            // expect(data.claimCauses.length).toBeGreaterThan(1);
            const req = httpMock.expectOne(`${environment.apiEndpoint}/claims/authorize`, 'call to api');
            expect(req.request.method).toBe('GET');
            httpMock.verify();
        },
            (error) => {
                expect(true).toBeFalsy();
            }
        );
    }));
    it('should get authorize impairment details on select specific impairment', async(() => {
        // tslint:disable-next-line:no-unused-expression
        let notifiableImpairmentId = 1;
        authoriseService.getAuthoriseImpairmentDetails(notifiableImpairmentId)
            .subscribe((res: any) => {
                expect(res.data.new.notifiableImpairmentId).toBe('notifiableImpairmentId');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/impairments/authorize/${notifiableImpairmentId}`,
            'post to api'
        );
        expect(req.request.method).toBe('GET');
        httpMock.verify();
    }));
    it('should get authorize claim details on select specific claim', async(() => {
        // tslint:disable-next-line:no-unused-expression
        let notificationID = 1;
        authoriseService.getAuthoriseClaimDetails(notificationID)
            .subscribe((res: any) => {
                expect(res.data.new.notificationID).toBe('notificationID');
            },
            (error) => {
                expect(true).toBeFalsy();
            }
            );
        const req = httpMock.expectOne(
            `${environment.apiEndpoint}/claims/authorize/${notificationID}`,
            'post to api'
        );
        expect(req.request.method).toBe('GET');
        httpMock.verify();
    }));
});
