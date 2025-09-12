import { Component } from '@angular/core';

import { AppFloatingConfigurator } from '../../layout/component/app.floatingconfigurator';
import { SharedUiModule } from '../../../shared-ui';
import { SharedModule } from 'primeng/api';

@Component({
    selector: 'app-access',
    standalone: true,
    imports: [SharedUiModule, AppFloatingConfigurator],
    templateUrl: './access.component.html'
})
export class Access {}
