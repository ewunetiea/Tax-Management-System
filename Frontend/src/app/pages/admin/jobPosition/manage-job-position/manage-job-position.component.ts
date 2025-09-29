import { Component } from '@angular/core';
import { Table, TableModule } from 'primeng/table';
import { JobPosition } from '../../../../models/admin/job-position';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { RoleService } from '../../../../service/admin/roleService';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ListboxModule } from 'primeng/listbox';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ToolbarModule } from 'primeng/toolbar';
import { ToastModule } from 'primeng/toast';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { PaginatorPayLoad } from '../../../../models/admin/paginator-payload';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { SelectButton, SelectButtonModule } from 'primeng/selectbutton';
import { TooltipModule } from 'primeng/tooltip';
import { CommonModule } from '@angular/common';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';

@Component({
    selector: 'app-manage-job-position',
    imports: [
        CommonModule,
        ConfirmDialogModule,
        ListboxModule,
        TableModule,
        ConfirmDialogModule,
        FormsModule,
        CardModule,
        ToolbarModule,
        ToastModule,
        BreadcrumbModule,
        InputIconModule,
        IconFieldModule,
        SelectButtonModule,
        TooltipModule,
        SelectButton,
        ButtonModule,
        InputTextModule
    ],
    
    templateUrl: './manage-job-position.component.html',
    styleUrl: './manage-job-position.component.scss'
})
export class ManageJobPositionComponent {
    jobPositions: JobPosition[] = [];
    selectedJobPositions: JobPosition[] = [];
    loading = true;
    sizes!: any[];
    selectedSize: any = undefined;
    breadcrumbText: string = 'Manage job positions';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    paginatorPayload = new PaginatorPayLoad();

    constructor(
        private jobPositionService: RoleService
    ) {}

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.selectedSize = this.sizes[1].value;
        this.getJobPosition();
    }

    getJobPosition() {
        this.jobPositionService.getMappedJobPositions().subscribe({
            next: (data) => {
                this.jobPositions = data;
                this.loading = false;
            },
            error: () => {
               this.loading = false;
            }
        }
    );

    }


    clear(table: Table) {
        table.clear();
    }

    onGlobalFilter(table: Table, event: Event) {
        const input = event.target as HTMLInputElement;
        table.filterGlobal(input.value, 'contains');
    }
}
