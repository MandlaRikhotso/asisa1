import * as jspdf from 'jspdf';
import html2canvas from 'html2canvas';
import * as moment from 'moment';
import { isTemplateMiddleOrTemplateTail } from 'typescript';

export class Utils {

    public static downloadSnapshot(id, fileName) {
        const data = document.getElementById(id);
        html2canvas(data).then(canvas => {
            // Few necessary setting options
            const imgWidth = 208;
            const pageHeight = 295;
            const imgHeight = canvas.height * imgWidth / canvas.width;
            const heightLeft = imgHeight;
            const contentDataURL = canvas.toDataURL('image/png');
            const pdf = new jspdf('p', 'mm', 'a4'); // A4 size page of PDF
            const position = 0;
            pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight);
            pdf.save(fileName + '.pdf'); // Generated PDF
        });
    }

    public static defaultTabs() {
        let obj;

        if (this.checkUserPermission('ADD_UPDATE_IMPAIRMENT')) {
            obj = {
                'tabs': [
                    {
                        'tabName': 'ADD PERSON',
                        'active': true,
                        'show': true,
                        'tabContent': true,
                        'borderBottomClass': true,
                        'formValid': false
                    },
                    {
                        'tabName': 'ADD IMPAIRMENT',
                        'active': false,
                        'show': true,
                        'tabContent': false,
                        'borderBottomClass': false,
                        'formValid': false
                    },
                    {
                        'tabName': 'ADD CLAIM',
                        'active': false,
                        'show': false,
                        'tabContent': false,
                        'borderBottomClass': false,
                        'formValid': false
                    },
                    {
                        'tabName': 'ADD NOTE',
                        'active': false,
                        'show': true,
                        'tabContent': false,
                        'borderBottomClass': false,
                        'formValid': false
                    },
                    {
                        'tabName': 'ADD SCRATCHPAD',
                        'active': false,
                        'show': true,
                        'tabContent': false,
                        'borderBottomClass': false,
                        'formValid': false
                    }
                ],
                'submitActive': false,
                'noteActive': false,
                'scratchpadtActive': false,
                'personId': ''
            };
        } else if (this.checkUserPermission('ADD_UPDATE_CLAIM')) {
            obj = {
                'tabs': [
                    {
                        'tabName': 'ADD PERSON',
                        'active': true,
                        'show': true,
                        'tabContent': true,
                        'borderBottomClass': true,
                        'formValid': false
                    },
                    {
                        'tabName': 'ADD IMPAIRMENT',
                        'active': false,
                        'show': false,
                        'tabContent': false,
                        'borderBottomClass': false,
                        'formValid': false
                    },
                    {
                        'tabName': 'ADD CLAIM',
                        'active': false,
                        'show': true,
                        'tabContent': false,
                        'borderBottomClass': false,
                        'formValid': false
                    },
                    {
                        'tabName': 'ADD NOTE',
                        'active': false,
                        'show': true,
                        'tabContent': false,
                        'borderBottomClass': false,
                        'formValid': false
                    },
                    {
                        'tabName': 'ADD SCRATCHPAD',
                        'active': false,
                        'show': true,
                        'tabContent': false,
                        'borderBottomClass': false,
                        'formValid': false
                    }
                ],
                'submitActive': false,
                'noteActive': false,
                'scratchpadtActive': false,
                'editEnableActive': false,
                'editNoteActive': false ,
                'editSctratchpadActive': false,
                'personId': ''
            };
        }
        return obj;
    }

    public static defaultPersonInfo() {
        const obj = {
            'insuredPerson': {
                'addressLine1': '',
                'addressLine2': '',
                'addressLine3': '',
                'dateOfBirth': '',
                'gender': '',
                'givenName1': '',
                'givenName2': '',
                'givenName3': '',
                'identityNumber': '',
                'identityTypeCode': '',
                'postalCode': 0,
                'surname': '',
                'title': ''
            },
            'notifications': []
        };
        return obj;
    }

    public static convertArrToString(arrData) {
        let str = '';
        if (arrData) {
            arrData.forEach((element, index) => {
                if (index === 0) {
                    str = element['code'];
                } else {
                    str += ', ' + element['code'];
                }
            });
        }
        return str;
    }

    public static checkNoteData(objData) {
        objData.note.notes.forEach((itemArray, itemindex) => {
            if (itemArray.note !== null) {
                return true;
            }
        });
        return false;
    }
    public static setImpairmentsData(objImpairments) {
        if (objImpairments.impairments.policies[0].policyNumber !== '') {
            let i = 0;
            const insuredPersonDetailsInfo: any = this.defaultPersonInfo();
            if (objImpairments['personinfo']) {
                insuredPersonDetailsInfo['insuredPerson'] = {
                    'addressLine1': objImpairments['personinfo']['addressLine1'],
                    'addressLine2': objImpairments['personinfo']['addressLine2'],
                    'addressLine3': objImpairments['personinfo']['addressLine3'],
                    'dateOfBirth': this.dtFormat(objImpairments['personinfo']['dateOfBirth']),
                    'gender': this.getObject(objImpairments['personinfo']['gender']),
                    'givenName1': objImpairments['personinfo']['givenName1'],
                    'givenName2': objImpairments['personinfo']['givenName2'],
                    'givenName3': objImpairments['personinfo']['givenName3'],
                    'identityNumber': objImpairments['personinfo']['idNumber'],
                    'identityType': this.getObject(objImpairments['personinfo']['idType']),
                    'postalCode': objImpairments['personinfo']['postalCode'],
                    'surname': objImpairments['personinfo']['surname'],
                    'title': this.getObject(objImpairments['personinfo']['pretitle'])
                };
            } else {
                insuredPersonDetailsInfo['insuredPerson'] = JSON.parse(localStorage.getItem('exitingPersonInfo'));
            }

            objImpairments.impairments.policies.forEach((item, index) => {
                const arrImp = [];
                let formData: any;
                item.impairments.forEach(impData => {
                    formData = {
                        // 'createdBy': userCode,
                        'timeSignal': impData.timeSignal,
                        'impairment': this.getObject(impData.impairment),
                        'readings': impData.reading + '/' + impData.reading1,
                        'specialInvestigation': impData.specialInvestigate,
                        'symbol': impData.symbol,
                        'notifiableImpairmentID': 0,
                        'notificationID': 0,
                        'updateReason': ''
                    };
                    arrImp.push(formData);
                });
                let tempArray: any;
                const noteArray: any = [];
                    objImpairments.note.notes.forEach((_element, _elementindex) => {
                        if (_element.policyNumber === item.policyNumber) {
                            // tslint:disable-next-line:max-line-length
                            const noteDesc = (objImpairments.note['notes'][_elementindex]) ? objImpairments.note['notes'][_elementindex]['note'] : '';
                            // tslint:disable-next-line:max-line-length
                            if (noteDesc) {
                                const objNote = {
                                    'noteText': noteDesc,
                                    'scratchpad': 'N'
                                };
                                noteArray.push(objNote);
                            }
                        }
                    });
                    objImpairments.scratch.scratchpads.forEach((_element, _elementindex) => {
                        if (_element.policyNumber === item.policyNumber) {
                            // tslint:disable-next-line:max-line-length
                            const scratchDesc = (objImpairments.scratch['scratchpads'][_elementindex]) ? objImpairments.scratch['scratchpads'][_elementindex]['comment'] : '';
                            if (scratchDesc) {
                                const objScratch = {
                                    'noteText': scratchDesc,
                                    'scratchpad': 'Y'
                                };
                                noteArray.push(objScratch);
                            }
                        }
                    });
                    tempArray = {
                        'notes': noteArray,
                        'notifiableImpairments': (item.policyNumber) ? arrImp : null,
                        'policyBenefit': (item.policyBenefit) ? item.policyBenefit[0] : null,
                        'policyNumber': (item.policyNumber) ? item.policyNumber : null
                    };
                    insuredPersonDetailsInfo['notifications'][i] = tempArray;
                    i++;
            });
            return insuredPersonDetailsInfo;
        } else {
            const dataInfo = this.setScratchpadData(objImpairments);
            return dataInfo;

        }
    }
    public static setScratchpadData(objScratchpad) {
        let i = 0;
        const insuredPersonDetailsInfo: any = this.defaultPersonInfo();
        if (objScratchpad['personinfo']) {
            insuredPersonDetailsInfo['insuredPerson'] = {
                'addressLine1': objScratchpad['personinfo']['addressLine1'],
                'addressLine2': objScratchpad['personinfo']['addressLine2'],
                'addressLine3': objScratchpad['personinfo']['addressLine3'],
                'dateOfBirth': this.dtFormat(objScratchpad['personinfo']['dateOfBirth']),
                'gender': this.getObject(objScratchpad['personinfo']['gender']),
                'givenName1': objScratchpad['personinfo']['givenName1'],
                'givenName2': objScratchpad['personinfo']['givenName2'],
                'givenName3': objScratchpad['personinfo']['givenName3'],
                'identityNumber': objScratchpad['personinfo']['idNumber'],
                'identityType': this.getObject(objScratchpad['personinfo']['idType']),
                'postalCode': objScratchpad['personinfo']['postalCode'],
                'surname': objScratchpad['personinfo']['surname'],
                'title': this.getObject(objScratchpad['personinfo']['pretitle'])
            };
        } else {
            insuredPersonDetailsInfo['insuredPerson'] = JSON.parse(localStorage.getItem('exitingPersonInfo'));
        }
        let tempArray: any;
        const noteArray: any = [];
        const scratchDesc = (objScratchpad.scratch.scratchpads[0].comment) ? objScratchpad.scratch.scratchpads[0].comment : '';
        if (scratchDesc) {
            const objScratch = {
                'noteText': scratchDesc,
                'scratchpad': 'Y'
            };
            noteArray.push(objScratch);
        }
        const policyNumberWith = objScratchpad.scratch.scratchpads[0].policyNumber;

        if (Utils.checkUserPermission('ADD_UPDATE_CLAIM')) {
            tempArray = {
                'notes': noteArray,
                'notifiableClaims': [],
                'policyBenefit': {},
                'policyNumber': policyNumberWith ? policyNumberWith : null,
            };
        } else if (Utils.checkUserPermission('ADD_UPDATE_IMPAIRMENT')) {
            tempArray = {
                'notes': noteArray,
                'notifiableImpairments': [],
                'policyBenefit': {},
                'policyNumber': policyNumberWith ? policyNumberWith : null,
            };

        }
        insuredPersonDetailsInfo['notifications'][i] = tempArray;
        i++;
        return insuredPersonDetailsInfo;
    }
    public static setClaimsData(objClaims) {
        if (objClaims.claims.policies[0].policyNumber !== '') {
            let i = 0;
            const insuredPersonDetailsInfo: any = this.defaultPersonInfo();
            if (objClaims['personinfo']) {
                insuredPersonDetailsInfo['insuredPerson'] = {
                    'addressLine1': objClaims['personinfo']['addressLine1'],
                    'addressLine2': objClaims['personinfo']['addressLine2'],
                    'addressLine3': objClaims['personinfo']['addressLine3'],
                    'dateOfBirth': this.dtFormat(objClaims['personinfo']['dateOfBirth']),
                    'gender': this.getObject(objClaims['personinfo']['gender']),
                    'givenName1': objClaims['personinfo']['givenName1'],
                    'givenName2': objClaims['personinfo']['givenName2'],
                    'givenName3': objClaims['personinfo']['givenName3'],
                    'identityNumber': objClaims['personinfo']['idNumber'],
                    'identityType': this.getObject(objClaims['personinfo']['idType']),
                    'postalCode': objClaims['personinfo']['postalCode'],
                    'surname': objClaims['personinfo']['surname'],
                    'title': this.getObject(objClaims['personinfo']['pretitle'])
                };
            } else {
                insuredPersonDetailsInfo['insuredPerson'] = JSON.parse(localStorage.getItem('exitingPersonInfo'));
            }

            objClaims.claims.policies.forEach((item, index) => {
                let formData: any;
                               let tempArray: any;
                    const noteArray: any = [];
                    const arrClaim: any = [];
                    objClaims.note.notes.forEach((_element, _elementindex) => {
                            if (_element.policyNumber === item.policyNumber) {
                                // tslint:disable-next-line:max-line-length
                                const noteDesc = (objClaims.note) ? objClaims.note['notes'][index]['note'] : '';
                                // tslint:disable-next-line:max-line-length
                                if (noteDesc) {
                                    const objNote = {
                                        'noteText': noteDesc,
                                        'scratchpad': 'N'
                                    };
                                    noteArray.push(objNote);
                                }
                            }
                        });
                        objClaims.scratch.scratchpads.forEach((_element, _elementindex) => {
                            if (_element.policyNumber === item.policyNumber) {
                                // tslint:disable-next-line:max-line-length
                                const scratchDesc = (objClaims.scratch['scratchpads'][_elementindex]) ? objClaims.scratch['scratchpads'][_elementindex]['comment'] : '';
                                if (scratchDesc) {
                                    const objScratch = {
                                        'noteText': scratchDesc,
                                        'scratchpad': 'Y'
                                    };
                                    noteArray.push(objScratch);
                                }
                            }
                        });
                    const selectedPayMethod: any = {};
                    const selectedda: any = "";
                    formData = {
                        // 'createdBy': userCode,
                        'dha1663Number': (item.dha1663Number  === null || item.dha1663Number  === "") ? selectedda : item.dha1663,
                        'claimCategory': item.categoryId[0],
                        'claimReason': item.claimReason ? item.claimReason : [],
                        // tslint:disable-next-line:max-line-length
                        'claimStatus':  (item.claimStatus.length === 0 || item.claimStatus === "") ? selectedPayMethod : this.getObject(item.claimStatus),
                        // tslint:disable-next-line:max-line-length
                        'eventCause': (item.causeOfEvent.length === 0 || item.causeOfEvent === "") ? selectedPayMethod : this.getObject(item.causeOfEvent),
                        'eventDate': (this.dtFormat(item.dateOfEvent) === 'Invalid date') ? '' : this.dtFormat(item.dateOfEvent),
                        'eventDeathCertificateNo': item.dateCertificateNumber ?  item.dateCertificateNumber : '',
                        'eventDeathPlace': item.placeOfDeath ? item.placeOfDeath : '',
                        // tslint:disable-next-line:max-line-length
                        'paymentMethod': (item.payMethod.length === 0 || item.payMethod === "") ? selectedPayMethod : this.getObject(item.payMethod) ,
                        'updateReason': '',
                    };
                    arrClaim.push(formData);
                    tempArray = {
                        'notes': noteArray,
                        'notifiableClaims': arrClaim,
                        'policyBenefit': item.claimType[0],
                        'policyNumber': item.policyNumber
                    };
                    insuredPersonDetailsInfo['notifications'][i] = tempArray;
                    i++;
                // });
            });
            return insuredPersonDetailsInfo;
        } else {
            const dataInfo = this.setScratchpadData(objClaims);
            return dataInfo;

        }
    }
    public static setImpForUpdate(objImpairments) {
        let obj: any;
        objImpairments.impairments.policies.forEach((item, index) => {
            const noteArray: any = [];
            const noteDesc = (objImpairments.note) ? objImpairments.note['notes'][index]['note'] : '';
            const scratchDesc = (objImpairments.scratch) ? objImpairments.scratch['scratchpads'][index]['comment'] : '';

            if (noteDesc) {
                const objNote = {
                    'noteText': noteDesc,
                    'scratchpad': 'N'
                };
                noteArray.push(objNote);
            }

            if (scratchDesc) {
                const objScratch = {
                    'noteText': scratchDesc,
                    'scratchpad': 'Y'
                };
                noteArray.push(objScratch);
            }
            obj = {
                'delete': false,
                'impairment': this.getObject(item.impairments[0]['impairment']),
                'notes': noteArray,
                'notifiableImpairmentID': localStorage.getItem('selectedImpId'),
                'notificationID': localStorage.getItem('notificationID'),
                'readings': item.impairments[0]['reading'] + '/' + item.impairments[0]['reading1'],
                'specialInvestigation': item.impairments[0]['specialInvestigate'],
                'symbol': item.impairments[0]['symbol'],
                'timeSignal': item.impairments[0]['timeSignal'],
                'updateReason': item['editReason']
            };
        });
        return obj;
    }

    public static setClaimForUpdate(objClaims) {
        let obj: any;
        objClaims.claims.policies.forEach((item, index) => {
            const noteArray: any = [];
            const noteDesc = (objClaims.note) ? objClaims.note['notes'][index]['note'] : '';
            const scratchDesc = (objClaims.scratch) ? objClaims.scratch['scratchpads'][index]['comment'] : '';

            if (noteDesc) {
                const objNote = {
                    'noteText': noteDesc,
                    'scratchpad': 'N'
                };
                noteArray.push(objNote);
            }

            if (scratchDesc) {
                const objScratch = {
                    'noteText': scratchDesc,
                    'scratchpad': 'Y'
                };
                noteArray.push(objScratch);
            }
            const selectedPayMethod: any = {};
            const selectedda: any = "";
            obj = {
                'dha1663Number': (item.dha1663Number  === null || item.dha1663Number  === "") ? "" : item.dha1663,
                'claimCategory': item.categoryId[0],
                'claimReason': item.claimReason,
                'claimStatus':  (item.claimStatus.length === 0 || item.claimStatus === "" || item.claimStatus === null) ? selectedPayMethod : this.getObject(item.claimStatus),
                // 'claimType': item.claimType,
                'delete': false,
                'eventCause': (item.causeOfEvent.length === 0 || item.causeOfEvent === ""|| item.causeOfEvent === null) ? selectedPayMethod : this.getObject(item.causeOfEvent),
                'eventDate': this.dtFormat(item.dateOfEvent),
                'eventDeathCertificateNo': (item.dateCertificateNumber !== null) ? item.dateCertificateNumber : '',
                'eventDeathPlace': (item.placeOfDeath !== null) ? item.placeOfDeath : '',
                'notes': noteArray,
                'notifiableClaimRefID': localStorage.getItem('selectedClaimId'),
                'notificationID': localStorage.getItem('notificationID'),
                'paymentMethod': (item.payMethod.length === 0 || item.payMethod === "" || item.payMethod === null) ? selectedPayMethod : this.getObject(item.payMethod) ,
                'updateReason': item.editReason

            };
        });
        return obj;
    }

    public static setInsuredPersonInfo(objFormData, key: boolean = false) {
        let obj: any;
        switch (key) {
            case true:
                obj = {
                    'identityType': objFormData['identityType'],
                    'identityNumber': objFormData['identityNumber'],
                    'surname': objFormData['surname'],
                    'givenName1': objFormData['givenName1'],
                    'givenName2': objFormData['givenName2'],
                    'givenName3': objFormData['givenName3'],
                    'dateOfBirth': this.dtFormat(objFormData['dateOfBirth']),
                    'gender': objFormData['gender'],
                    'addressLine1': objFormData['addressLine1'],
                    'addressLine2': objFormData['addressLine2'],
                    'addressLine3': objFormData['addressLine3'],
                    'postalCode': objFormData['postalCode'],
                    'title': objFormData['title']
                };
                return obj;
                break;
            default:
                obj = {
                    'identityType': this.getObject(objFormData['idType']['value']),
                    'identityNumber': objFormData['idNumber']['value'],
                    'surname': objFormData['surname']['value'],
                    'givenName1': objFormData['givenName1']['value'],
                    'givenName2': objFormData['givenName2']['value'],
                    'givenName3': objFormData['givenName3']['value'],
                    'dateOfBirth': this.dtFormat(objFormData['dateOfBirth']['value']),
                    'gender': this.getObject(objFormData['gender']['value']),
                    'addressLine1': objFormData['addressLine1']['value'],
                    'addressLine2': objFormData['addressLine2']['value'],
                    'addressLine3': objFormData['addressLine3']['value'],
                    'postalCode': objFormData['postalCode']['value'],
                    'title': this.getObject(objFormData['pretitle']['value'])
                };
                return obj;
                break;
        }
    }

    public static chkResponseSuccess(response) {
        if (response['status'] === 200 && response['statusText'] === 'OK') {
            return true;
        } else {
            let errorMessage = '';
            if (response.error instanceof ErrorEvent) {
                // client-side error
                errorMessage = `Error: ${response.error}`;
            } else {
                // server-side error
                errorMessage = `Error Code: ${response.status}\nMessage: ${response.message}`;
                if (response.error.fieldErrors) {
                    response.error.fieldErrors.forEach(element => {
                        errorMessage += '\n' + element.message;
                    });
                } else if (response.error.globalErrors) {
                    response.error.globalErrors.forEach(element => {
                        errorMessage += '\n' + element.message;
                    });
                }
            }
            return errorMessage;
        }
    }

    public static getObject(arr) {
        return arr[0];
    }

    public static dtFormat(date) {
        const dateStringE: any = '';
        const dt = moment(date).format();
        if (dt !== 'Invalid date') {
            return moment(date).format('DD/MM/YYYY');
        } else if (date === '') {
            return dateStringE;
        } else if (date === null) {
            return dateStringE;
        } else if (date === 'Invalid date') {
            return dateStringE;
        } else {
            const myDate = moment(date, 'DD/MM/YYYY').toDate();
            return moment(myDate).format('DD/MM/YYYY');
        }
    }
    public static convertDate(inputFormat) {
        function pad(s) { return (s < 10) ? '0' + s : s; }
        const d = new Date(inputFormat);
        return [pad(d.getMonth()+1), pad(d.getDate()), d.getFullYear()].join('/');
      }

    public static reformatDate(dateStr) {
        const dArr = dateStr.split('-');  // ex input "2010-01-18"
        return dArr[2] + '/' + dArr[1] + '/' + dArr[0]; //ex out: "18/01/10"
    }

    public static checkUserPermission(strActivity) {
        const obj = JSON.parse(localStorage.getItem('currentUser'));
        const arrPermission: any = obj['authorities'][0]['activityCode'];
        if (arrPermission.indexOf(strActivity) > -1) {
            return true;
        }
        return false;
    }
}
