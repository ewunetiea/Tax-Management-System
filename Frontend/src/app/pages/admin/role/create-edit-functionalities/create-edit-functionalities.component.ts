import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { RoleService } from '../../../../service/admin/roleService';
import { MessageService, SelectItem } from 'primeng/api';
import { Functionalities } from '../../../../models/admin/functionalities';
import { HttpErrorResponse } from '@angular/common/http';
import { User } from '../../../../models/admin/user';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { TextareaModule } from 'primeng/textarea';
import { MessageModule } from 'primeng/message';
import { Select } from 'primeng/select';
import { RoleFunctionalityService } from '../../../../service/admin/roleFunctionalityService';
import { StorageService } from '../../../../service/sharedService/storage.service';

@Component({
    selector: 'app-create-edit-functionalities',
    imports: [ConfirmDialogModule, FormsModule, ReactiveFormsModule, CommonModule, DialogModule, ButtonModule, InputTextModule, DropdownModule, MultiSelectModule, TextareaModule, MessageModule, Select],
    templateUrl: './create-edit-functionalities.component.html',
    styleUrl: './create-edit-functionalities.component.scss'
})
export class CreateEditFunctionalitiesComponent {
    functionalityForm: FormGroup;
    nameExists: boolean = false;
    functionalityLoading: boolean = false;
    functionalityDialog: boolean = false;
    nameTouched: boolean = false;
    loading: boolean = false;
    user: User = new User();
    isEditData = false;
    functionalities: Functionalities[] = [];
    functionality: Functionalities = new Functionalities();
    categories: SelectItem[] = [];
    methods: SelectItem[] = [
        { label: 'GET', value: 'GET' },
        { label: 'POST', value: 'POST' },
        { label: 'PUT', value: 'PUT' },
        { label: 'DELETE', value: 'DELETE' }
    ];

    @Input() passedFunctionality: any[] = [];
    @Output() editedFunctionality: EventEmitter<any> = new EventEmitter();

    constructor(
        private roleFunctionalityService: RoleFunctionalityService,
        private roleRightService: RoleService,
        private messageService: MessageService,
        private fb: FormBuilder,
        private storageService: StorageService
    ) {
        this.functionalityForm = this.fb.group({
            id: [null],
            name: ['', [Validators.required]],
            description: ['', [Validators.required]],
            categories: [[], [Validators.required]],
            method: ['', [Validators.required]],
            status: [false]
        });
    }

    ngOnInit(): void {
        this.user = this.storageService.getUser();
        if (this.passedFunctionality && this.passedFunctionality.length > 0) {
            this.isEditData = this.passedFunctionality[1];
            if (this.isEditData) {
                this.editFunctionality(this.passedFunctionality[0]);
            } else {
                this.openNew();
            }
        }
    }

    editFunctionality(data: any) {
        this.functionality = { ...data };
        this.nameExists = false;

        this.functionalityForm.patchValue({
            id: this.functionality.id,
            name: this.functionality.name,
            description: this.functionality.description,
            categories: this.functionality.category ? this.functionality.category.split(',').map((c: string) => c.trim()) : [],
            method: this.functionality.method,
            status: this.functionality.status
        });

        this.getRolesCode();
    }

    openNew() {
        this.functionality = new Functionalities();
        this.nameExists = false;
        this.getRolesCode();
    }

    emitData(data: any[]) {
        this.editedFunctionality.emit(data);
    }

    saveFunctionality(): void {
        this.checkNameExists();
        if (this.functionalityForm.invalid || this.nameExists) {
            this.functionalityForm.markAllAsTouched();
            return;
        }

        const formValue = this.functionalityForm.value;
        const functionality: Functionalities = {
            ...formValue,
            category: formValue.categories?.join(', ')
        };

        this.functionalityLoading = true;
        this.roleFunctionalityService.savePermissions(functionality).subscribe({
            next: (data) => {
                this.messageService.add({
                    severity: 'success',
                    summary: 'Success',
                    detail: functionality.id ? 'Functionality updated' : 'Functionality created'
                });
                this.functionalityDialog = false;
                this.functionalityLoading = false;
                this.loadFunctionalities();
            },
            error: (error) => {
                this.handleError('Error saving functionality', error);
            }
        });
    }

    checkNameExists() {
        this.nameTouched = true;
        const name = this.functionalityForm.get('name')?.value;
        const currentId = this.functionalityForm.get('id')?.value;
        if (name && name.trim() !== '') {
            this.nameExists = this.functionalities.some((f) => f.name?.toLowerCase() === name.toLowerCase() && f.id !== currentId && f.method === this.functionalityForm.get('method')?.value);
        } else {
            this.nameExists = false;
        }
    }

    loadFunctionalities(): void {
        this.loading = true;
        this.roleFunctionalityService.getAllRoleFunctionalities().subscribe({
            next: (resp: any) => {
                this.functionalities = resp;
                this.loading = false;
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.handleError(error.status === 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching functionalities!', error);
            }
        });
    }

    private handleError(summary: string, error: any): void {
        this.messageService.add({
            severity: 'error',
            summary: summary,
            detail: error.message || ''
        });
    }

    hideDialog(): void {
        this.functionalityDialog = false;
        this.nameTouched = false;
        this.functionalityForm.reset();
    }

    getRolesCode(): void {
        this.roleRightService.getCommonRoles().subscribe({
            next: (roles: any) => {
                this.categories = roles.map((role: any) => ({
                    label: role.code,
                    value: role.code
                }));
                this.categories.push({
                    label: 'ALL',
                    value: 'ALL'
                });
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.handleError(error.status === 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching roles!', error);
            }
        });
    }
}
