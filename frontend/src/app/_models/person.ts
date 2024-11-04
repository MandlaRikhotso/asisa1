export class Person {
    idType: string;
    idNumber: string;
    initials: string;
    surname: string;
    givenName1: string;
    dateOfBirth: string;
    gender: string;

    sayHello(): string {
        return `Hi, ${this.givenName1}`;
    }
}



