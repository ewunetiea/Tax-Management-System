// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  auditorFileApi: 'http://localhost:8442/api',//to hnadle auditor uploading files
  auditeeFileApi: 'http://localhost:8442/api', // to handle auditee response files
  imagesUserApi: 'http://localhost:8442/api/user/image/',
  blankPic: 'assets/img/Blank-Profile-Picture.jpg',
  backendUrl: 'http://localhost:8442/api',
  webSocketUrl: 'http://localhost:8442/api',
  idleTimeout: 6 * 60 * 1000, // 6 minutes
  idleCountdownSeconds: 60, // 1 minute
  warningTimeout: 5 * 60 * 1000, // 5 minutes
};


// export const environment = {
//   production: false,
//   auditorFileApi: 'http://localhost:8443/api',//to hnadle auditor uploading files
//   auditeeFileApi: 'http://localhost:8443/api', // to handle auditee response files
//   imagesUserApi: 'http://localhost:8443/api/user/image/',
//   blankPic: 'assets/img/Blank-Profile-Picture.jpg',
//   backendUrl: 'http://localhost:8443/api',
//   webSocketUrl: 'http://localhost:8443/api',
//   idleTimeout: 20 * 60 * 1000, // 6 minutes
//   idleCountdownSeconds: 60, // 1 minute
//   warningTimeout: 19 * 60 * 1000, // 5 minutes
// };


// export const environment = {
//   production: false,
//   auditorFileApi: 'https://10.10.101.60:8443/tmsbackend/api',//to hnadle auditor uploading files
//   auditeeFileApi: 'https://10.10.101.60:8443/tmsbackend/api', // to handle auditee response files
//   imagesUserApi: 'https://10.10.101.60:8443/tmsbackend/api/user/image/',
//   blankPic: 'assets/img/Blank-Profile-Picture.jpg',
//   backendUrl: 'https://10.10.101.60:8443/tmsbackend/api',
//   webSocketUrl: 'https://10.10.101.60:8443/tmsbackend/api',
//   idleTimeout: 6 * 60 * 1000, // 6 minutes
//   idleCountdownSeconds: 60, // 1 minute
//   warningTimeout: 5 * 60 * 1000, // 5 minutes
// };


// export const environment = {
//   production: false,
//   filesAuditApi: 'https://10.10.32.57:8443/afrfmsbackend/api/auditee/ism/files/',
//   imagesUserApi: 'https://10.10.32.57:8443/afrfmsbackend/api/user/images/',
//   blankPic: 'assets/img/Blank-Profile-Picture.jpg',
//   backendUrl: 'https://10.10.32.57:8443/afrfmsbackend/api',
// };


// export const environment = {
//   production: false,
//   auditorFileApi: 'https://afrfmsbackend.awashbank.com/financialbackend/api', //to hnadle auditor uploading files
//   auditeeFileApi: 'https://afrfmsbackendsecondary.awashbank.com/financialbackend/api', // to handle auditee response files
//   imagesUserApi: 'https://afrfmsbackendsecondary.awashbank.com/financialbackend/api/user/image/', // to handle user images
//   blankPic: 'assets/img/Blank-Profile-Picture.jpg',
//   backendUrl: 'https://afrfmsbackendlb.awashbank.com/financialbackend/api',
//   webSocketUrl: 'https://afrfmsbackendlb.awashbank.com/afrfmsbackend/api',
//   idleTimeout: 6 * 60 * 1000, // 6 minutes
//   idleCountdownSeconds: 60, // 1 minute
//   warningTimeout: 5 * 60 * 1000, // 5 minutes
// };


/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
