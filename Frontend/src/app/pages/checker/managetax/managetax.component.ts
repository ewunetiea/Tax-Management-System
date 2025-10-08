import { Component } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { SharedUiModule } from '../../../../shared-ui';

@Component({
  selector: 'app-managetax',
  providers: [MessageService, ConfirmationService],
  imports: [SharedUiModule],
  templateUrl: './managetax.component.html',
  styleUrl: './managetax.component.scss'
})
export class ManagetaxComponent {

}
