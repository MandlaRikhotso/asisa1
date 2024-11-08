// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  apiEndpoint: './api',
  //  apiEndpoint: 'http://localhost:8080/api',
  // apiEndpoint: 'https://asisa-registers-dit-api.app.ocp.dr.za.omlac.net/api',
  gcsEndpoint: 'https://apigwint.dev.za.omlac.net/omem/dev-int/insurance/life/party/v9/persons'
};

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
