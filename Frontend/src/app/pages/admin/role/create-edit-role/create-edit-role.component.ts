import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Rights } from '../../../../models/admin/rights';
import { Role } from '../../../../models/admin/role';
import { ValidationService } from '../../../service/admin/validationService';
import { RoleService } from '../../../service/admin/roleService';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DialogModule } from 'primeng/dialog';
import { ButtonModule } from 'primeng/button';
import { InputText, InputTextModule } from 'primeng/inputtext';
import { CardModule } from 'primeng/card';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { RadioButton } from 'primeng/radiobutton';
import { ToggleSwitch } from 'primeng/toggleswitch';
import { TextareaModule } from 'primeng/textarea';
import { MessageModule } from 'primeng/message';

@Component({
  selector: 'app-create-edit-role',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DialogModule,
    ButtonModule,
    InputTextModule,
    InputText,
    CardModule,
    ConfirmDialogModule,
    RadioButton,
    ToggleSwitch,
    TextareaModule,
    MessageModule
  ],
  templateUrl: './create-edit-role.component.html',
  styleUrl: './create-edit-role.component.scss'
})
export class CreateEditRoleComponent {
  role_code: string='';
  role_name: string='';
  role_name_status = false;
  role_code_status = false;
  role: Role =new Role();
  // role: Role = {
  //   code: '',
  //   name: '',
  //   role_level: 0,
  //   description: '',
  //   rights: [],
  //   dynamic_menu: false,
  // };
  levels: any = [];
  allRights: Rights[] = [];
  rightStore: Rights[] = [];
  errorMessage: string = '';
  dropdownList: any = [];
  selectedItems: any = [];
  confrimationDialog = false;
  loading = false;

  @Input() passedRole: any[] = [];
  @Output() editedRole: EventEmitter<any> = new EventEmitter();
  isEditData = false;

  constructor(
    private roleRightService: RoleService,
    private validationService: ValidationService,
    private messageService: MessageService,
  ) { }

  ngOnInit(): void {
    this.isEditData = this.passedRole[1];
    if (this.isEditData) {
      this.openNew();
    } else {
      this.editRole(this.passedRole);
    }
  }

  editRole(passedData: any[]) {
    this.role = passedData[0];
    // this.role_name_status = true;
    // this.role_code_status = true;
  }

  openNew() {
    this.role = new Role();
    this.role_name_status = false;
    this.role_code_status = false;
  }

  emitData(data: any[]) {
    this.editedRole.emit(data);
  }

  saveRole(): void {
    this.loading = true;
    this.roleRightService.createRole(this.role).subscribe({
      next: (res) => {
        this.loading = false;
        if (this.role.id) {
          this.messageService.add({
            severity: 'success',
            summary: ` ${this.role.name} successfully updated`,
            detail: '',
            life: 3000,
          });
        } else {
          this.messageService.add({
            severity: 'success',
            summary: ` ${this.role.name} successfully created`,
            detail: '',
            life: 3000,
          });
          this.role = new Role();
        }
        this.passedRole = [];
        this.passedRole.push(this.role);
        this.passedRole.push(this.isEditData);
        this.emitData(this.passedRole);
      },
      error: (error: HttpErrorResponse) => {
        this.loading = false;
        this.messageService.add({
          severity: 'error',
          summary:
            error.status == 401
              ? 'You are not permitted to perform this action!'
              : 'Something went wrong while creating role !',
          detail: '',
          life: 3000,
        });
      },
    });
  }

  checkNameStatus(event: any) {
    this.role_name = event.target.value;
    this.validationService.checRoleName(this.role_name).subscribe({
      next: (res: any) => {
        if (res) {
          this.role_name_status = true;
        } else {
          this.role_name_status = false;
        }
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error.message;
      },
    });
  }

  checkCodeStatus(event: any) {
    this.role_code = event.target.value;
    this.validationService.checkRoleCode(this.role_code).subscribe({
      next: (res: any) => {
        if (res) {
          this.role_code_status = true;
        } else {
          this.role_code_status = false;
        }
      },
      error: (error: HttpErrorResponse) => {
        this.errorMessage = error.error.message;
      },
    });
  }

}
