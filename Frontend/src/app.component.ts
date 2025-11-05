import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AutoLogoutService } from 'app/service/sharedService/auto-logout.service';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterModule],
    template: `<router-outlet></router-outlet>`
})
export class AppComponent {
    constructor(
    private autoLogoutService: AutoLogoutService
  ) {
    this.autoLogoutService.startAfterLogin();
  }

}

