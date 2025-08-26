// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

// export const environment = {
//   production: false,
//   imagesStaffApi: 'https://lms.awashbank.com/bigbackend/api/v1/staff/images/',
//   imagesUserApi: 'https://lms.awashbank.com/bigbackend/api/user/images/',
//   blankPic: 'assets/img/Blank-Profile-Picture.jpg',
//   backendUrl: 'https://lms.awashbank.com/bigbackend/api',
// };

export const environment = {
  production: false,
  filesAuditApi: 'http://localhost:8442/api/auditee/ism/files/',
  imagesUserApi: 'http://localhost:8442/api/user/image/',
  blankPic: 'assets/img/Blank-Profile-Picture.jpg',
  backendUrl: 'http://localhost:8442/api',
};

// export const environment = {
//   production: false,
//   filesAuditApi: 'https://10.10.101.60:8442/inspectionbackend/api/auditee/ism/files/',
//   imagesUserApi: 'https://10.10.101.60:8442/inspectionbackend/api/user/images/',
//   blankPic: 'assets/img/Blank-Profile-Picture.jpg',
//   backendUrl: 'https://10.10.101.60:8442/inspectionbackend/api',
// };

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
