import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MessageService, ConfirmationService } from 'primeng/api';
import { User } from '../../../../../models/admin/user';
import { UserSecurity } from '../../../../../models/admin/user-security';
import { UserService } from '../../../../service/admin/user.service';
import { ConfirmDialog } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { FormsModule } from '@angular/forms';
import { CalendarModule } from 'primeng/calendar';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';

@Component({
  selector: 'app-user-security',
  imports: [ConfirmDialog, ToastModule, FormsModule, CalendarModule, InputNumberModule, DialogModule,],
  templateUrl: './user-security.component.html',
  styleUrl: './user-security.component.scss',
  providers: [MessageService, ConfirmationService]
})
export class UserSecurityComponent {
  user_sec = new User();
  loading = false;
  today: Date = new Date();
  password_created_date = new Date();
  isEditData = false;

  @Input() passedUser: any[] = [];
  @Output() editedUser: EventEmitter<any> = new EventEmitter();

  userSecurity: UserSecurity = {
    accountNonExpired: false,
    number_of_attempts: 0,
    password_modified_date: this.today,
    password_created_date: this.today,
    credentialsNonExpired: false,
    accountNonLocked: false,
  };

  constructor(
    private userService: UserService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService
  ) {}

  ngOnInit(): void {
    this.isEditData = this.passedUser[1];
    this.user_sec = this.passedUser[0];
    this.user_sec.user_security = this.userSecurity;
  }

  emitData(data: any[]) {
    this.editedUser.emit(data);
  }

  saveUser(): void {
    this.loading = true;
    let passwordcreated_date: any;
    passwordcreated_date = this.password_created_date;
    this.user_sec.user_security!.password_created_date = passwordcreated_date;
    this.userService.unlockUserAccount(this.user_sec).subscribe({
      next: (res) => {
        this.loading = false;
        this.messageService.add({
          severity: 'success',
          summary: ` ${
            this.user_sec.first_name + '  ' + ' ' + this.user_sec.middle_name
          } status is updated successfully`,
          detail: '',
        });
        this.passedUser = [];
        this.passedUser.push(this.user_sec);
        this.passedUser.push(false);
        this.emitData(this.passedUser);
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        this.messageService.add({
          severity: 'error',
          summary:
            error.status == 401
              ? 'You are not permitted to perform this action!'
              : 'Something went wrong while update user status !',
          detail: '',
        });
      },
    });
  }

}
