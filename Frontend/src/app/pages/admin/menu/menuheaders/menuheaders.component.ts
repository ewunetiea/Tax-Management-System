import { Component } from '@angular/core';
import { Table } from 'primeng/table';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { InputIconModule } from 'primeng/inputicon';
import { SelectButtonModule } from 'primeng/selectbutton';
import { IconFieldModule } from 'primeng/iconfield';
import { MultiSelectModule } from 'primeng/multiselect';
import { AccordionModule } from 'primeng/accordion';
import { TabViewModule } from 'primeng/tabview';
import { ListboxModule } from 'primeng/listbox';
import { DropdownModule } from 'primeng/dropdown';
import { MenuHeader } from '../../../../models/admin/menu-headers';
import { MenuItems } from '../../../../models/admin/menu-items';
import { Role } from '../../../../models/admin/role';
import { RoleService } from '../../../../service/admin/roleService';
import { InputTextModule } from 'primeng/inputtext';
import { MenuService } from '../../../../service/admin/menu-service';
import { StorageService } from '../../../../service/sharedService/storage.service';

@Component({
    selector: 'app-menuheaders',
    imports: [
        DialogModule,
        MultiSelectModule,
        AccordionModule,
        TabViewModule,
        ListboxModule,
        DropdownModule,
        ButtonModule,
        TableModule,
        FormsModule,
        ToolbarModule,
        CommonModule,
        ToastModule,
        CardModule,
        BreadcrumbModule,
        SelectButtonModule,
        InputIconModule,
        IconFieldModule,
        InputTextModule
    ],
    providers: [MessageService, ConfirmationService],
    templateUrl: './menuheaders.component.html',
    styleUrl: './menuheaders.component.scss'
})
export class MenuheadersComponent {
    menuHeaders: MenuHeader[] = [];
    passMenuHeaders: MenuHeader[] = [];
    menuItems: MenuItems[] = [];
    roles: Role[] = [];
    loading: boolean = true;
    filters: any;
    menuHeaderDialog: boolean = false;
    deleteMenuHeaderDialog: boolean = false;
    deleteMenuHeadersDialog: boolean = false;
    activateMenuHeaderDialog: boolean = false;
    activateMenuHeadersDialog: boolean = false;
    roleDialog: boolean = false;
    itemDialog: boolean = false;

    // Selections and Forms
    selectedRoles: Role[] = [];
    selectedMenuItems: MenuItems[] = [];
    selectedMenuHeaders: MenuHeader[] = [];
    menuHeader: MenuHeader = new MenuHeader();
    activeIndex: number = 0;

    // Expansion and filters
    expandedRows: any[] = [];
    allExpanded: boolean = false;
    globalFilter: string = '';
    globalFilterValue: string = '';
    headerTouched: boolean = false;
    headerExists: boolean = false;
    header_target = [
        { value: 'Sidebar', title: 'Sidebar' },
        { value: 'Topbar', title: 'Topbar' },
        { value: 'All', title: 'All' }
    ];
    activeIndex1: number = 0;
    activeState: boolean[] = [true, false, false];
    breadcrumbText: string = 'Manage menu headers';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    sizes!: any[];
    selectedSize: any = 'normal';

    constructor(
        private menuService: MenuService,
        private roleService: RoleService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private storageService: StorageService
    ) {}

    ngOnInit(): void {
        this.breadcrumbText = 'Manage Menu Headers';
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.menuService.getMenuHeaders().subscribe({
            next: (data: MenuHeader[]) => {
                this.menuHeaders = data;
                this.loading = false;
            },
            error: (error) => {
                this.loading = false;
                let errorMessage = 'Error while fetching menu headers.';

                if (error.status === 401) {
                    errorMessage = 'Unauthorized: You do not have the required permissions.';
                } else if (error.status === 403) {
                    errorMessage = 'Forbidden: Access token expired.';
                } else {
                    errorMessage = error.error?.message || errorMessage;
                }

                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: errorMessage,
                    life: 3000
                });
            }
        });
    }

    toggle(index: number) {
        this.activeState[index] = !this.activeState[index];
    }

    clear(table: Table) {
        table.clear();
    }

    onGlobalFilter(table: Table, event: Event) {
        const inputValue = (event.target as HTMLInputElement).value;
    }

    openManageRoles(header: MenuHeader): void {
        this.menuHeader = header;
        this.selectedRoles = header.roles || [];
        this.fetchRoles();
        this.roleDialog = true;
        this.activeIndex = 1;
    }

    openManageMenuItems(header: MenuHeader): void {
        this.menuHeader = header;
        this.selectedMenuItems = header.menuItems || [];
        this.fetchItems();
        this.activeIndex = 2;
        this.itemDialog = true;
    }

    fetchRoles(): void {
        this.roleService.getCommonRoles().subscribe({
            next: (data: Role[]) => {
                this.roles = data;
            },
            error: (error) => {
                let errorMessage = 'Error happened while Relating Menu Header with roles.';

                if (error.status === 401) {
                    errorMessage = 'Unauthorized: You do not have the required permissions to access this resource.';
                } else if (error.status === 403) {
                    errorMessage = 'Forbidden: Your access token has expired, and you are not authorized to access this resource.';
                } else if (error.error?.message) {
                    errorMessage = error.error.message;
                }

                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: errorMessage,
                    life: 3001
                });
            }
        });
    }

    fetchItems(): void {
        this.menuService.getMenuItems().subscribe({
            next: (data: MenuItems[]) => {
                this.menuItems = data;
                this.selectedMenuItems = this.menuItems.filter((item) => this.selectedMenuItems.some((selected) => selected.id === item.id));
            },
            error: (error) => {
                let errorMessage = 'Something went wrong while fetching menu items.';

                if (error.status === 401) {
                    errorMessage = 'Unauthorized: You do not have the required permissions to access this resource.';
                } else if (error.status === 403) {
                    errorMessage = 'Forbidden: Your access token has expired, and you are not authorized to access this resource.';
                } else if (error.error?.message) {
                    errorMessage = error.error.message;
                }

                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: errorMessage,
                    life: 3001
                });
            }
        });
    }

    editMenuRoles(): void {
        try {
            this.menuHeader.roles = this.selectedRoles ?? [];

            const menuRole = {
                header_id: this.menuHeader.id,
                maker_id: this.storageService.getUser().id,
                status: true,
                roles: this.selectedRoles ?? []
            };

            this.menuService.manageMenuRoles(menuRole).subscribe({
                next: () => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Roles mapped successfully.',
                        life: 3000
                    });
                    this.roleDialog = false;
                },
                error: (error) => {
                    let errorMessage = 'Error mapping roles.';
                    if (error.status === 401) {
                        errorMessage = 'Unauthorized: You do not have the required permissions.';
                    } else if (error.status === 403) {
                        errorMessage = 'Forbidden: Access token expired.';
                    } else if (error.error?.message) {
                        errorMessage = error.error.message;
                    }

                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: errorMessage,
                        life: 3000
                    });
                }
            });
        } catch (err) {
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Unexpected error occurred while mapping roles.',
                life: 3000
            });
            console.error(err);
        }
    }

    editMenuItems(): void {
        try {
            this.menuHeader.menuItems = this.selectedMenuItems ?? [];

            const menuHeaderItems = {
                header_id: this.menuHeader.id,
                creator_id: this.storageService.getUser().id,
                menuItems: this.selectedMenuItems ?? []
            };

            this.menuService.manageMenuHeaderMenuItems(menuHeaderItems).subscribe({
                next: () => {
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Menu items mapped successfully.',
                        life: 3000
                    });
                    this.itemDialog = false;
                },
                error: (error) => {
                    let errorMessage = 'Error mapping menu items.';
                    if (error.status === 401) {
                        errorMessage = 'Unauthorized: You do not have the required permissions.';
                    } else if (error.status === 403) {
                        errorMessage = 'Forbidden: Access token expired.';
                    } else if (error.error?.message) {
                        errorMessage = error.error.message;
                    }

                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: errorMessage,
                        life: 3000
                    });
                }
            });
        } catch (err) {
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'Unexpected error occurred while mapping menu items.',
                life: 3000
            });
            console.error(err);
        }
    }

    openNew(): void {
        this.fetchRoles();
        this.fetchItems();
        this.menuHeader = new MenuHeader();
        this.menuHeader.icon = 'pi pi-fw pi-';
        this.menuHeaderDialog = true;
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < (this.menuHeaders as any)?.length; i++) {
            if ((this.menuHeaders as any)[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    saveMenuHeader(menuHeaderData: MenuHeader): void {
        try {
            menuHeaderData.maker_id = this.storageService.getUser().id;
            menuHeaderData.target_roles = this.getRoleNames(menuHeaderData.roles || []);

            this.menuService.addMenuHeader(menuHeaderData).subscribe({
                next: (res) => {
                    if (menuHeaderData.header?.trim()) {
                        const index = this.findIndexById(menuHeaderData.id ?? 0);
                        if (menuHeaderData.id && index !== -1) {
                            this.menuHeaders[index] = menuHeaderData;
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Successful',
                                detail: 'Menu header updated successfully.',
                                life: 3001
                            });
                        } else {
                            this.menuHeaders.push(menuHeaderData);
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Successful',
                                detail: 'Menu header created successfully.',
                                life: 3001
                            });
                        }

                        this.menuHeaderDialog = false;
                        this.menuHeader = {} as MenuHeader;
                    }
                },
                error: (error) => {
                    let errorMessage = 'Error creating menu header.';

                    if (error.status === 401) {
                        errorMessage = 'Unauthorized: You do not have the required permissions to access this resource.';
                    } else if (error.status === 403) {
                        errorMessage = 'Forbidden: Your access token has expired, and you are not authorized to access this resource.';
                    } else if (error.error?.message) {
                        errorMessage = error.error.message;
                    }

                    this.messageService.add({ severity: 'error', summary: 'Error', detail: errorMessage, life: 3001 });
                }
            });
        } catch (error) {
            console.error('Error creating menu item:', error);
        }
    }

    getRoleNames(roles: Role[]): string {
        return roles.map((role) => role.code).join(', ');
    }

    editMenuHeader(menuHeader: MenuHeader): void {
        this.menuHeader = menuHeader;
        this.fetchRoles();
        this.fetchItems();
        this.menuHeaderDialog = true;
    }

    confirmDeactivateMenuHeader(menuHeaderData: MenuHeader): void {
        menuHeaderData.delete = false;
        this.menuHeader = menuHeaderData;
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected menu headers?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.deactivateMenuHeader()
        });
        this.deleteMenuHeaderDialog = true;
    }

    deleteMenuDialog(menuHeaderData: MenuHeader): void {
        menuHeaderData.delete = true;
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete selected menu headers?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.deleteMenuHeaderTotally(menuHeaderData)
        });
        this.deleteMenuHeaderDialog = true;
    }

    confirmActivateMenuHeader(menuHeader: MenuHeader): void {
        this.menuHeader = menuHeader;
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected menu headers?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.activateMenuHeader()
        });
        this.activateMenuHeaderDialog = true;
    }

    confirmDeactivateSelected(): void {
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected menu headers?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.deactivateSelectedMenuHeaders()
        });
    }

    confirmActivateSelected(): void {
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected menu headers?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.activateSelectedMenuItems()
        });
    }

    deleteMenuHeaderTotally(menuHeaderData: MenuHeader): void {
        try {
            if (menuHeaderData) {
                menuHeaderData.maker_id = this.storageService.getUser().id;
                this.passMenuHeaders.push(menuHeaderData);

                this.menuService.manageMenuHeaderStatus(this.passMenuHeaders).subscribe({
                    next: () => {
                        this.passMenuHeaders = [];
                        menuHeaderData.status = false;
                        const index = this.findIndexById(menuHeaderData.id ?? 0);
                        this.menuHeaders[index] = menuHeaderData;

                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Menu header deleted successfully.',
                            life: 3001
                        });
                    },
                    error: (error) => {
                        const errorMessage = 'Error deleting menu header.';
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: errorMessage,
                            life: 3001
                        });
                    }
                });
            }
        } catch (error) {
            console.error('Error deleting menu header:', error);
        }
    }

    deactivateMenuHeader(): void {
        try {
            if (this.menuHeader) {
                this.menuHeader.status = false;
                this.menuHeader.maker_id = this.storageService.getUser().id;
                this.passMenuHeaders.push(this.menuHeader);

                this.menuService.manageMenuHeaderStatus(this.passMenuHeaders).subscribe({
                    next: () => {
                        this.passMenuHeaders = [];

                        const index = this.findIndexById(this.menuHeader.id ?? 0);
                        this.menuHeaders[index] = { ...this.menuHeader };

                        this.deleteMenuHeaderDialog = false;
                        this.menuHeader = {} as MenuHeader;

                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Menu header deactivated successfully.',
                            life: 3001
                        });
                    },
                    error: (error) => {
                        const errorMessage = ' Error deactivating menu header.';
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: errorMessage,
                            life: 3001
                        });
                    }
                });
            }
        } catch (error) {
            console.error('Error deactivating menu header:', error);
        }
    }

    activateMenuHeader(): void {
        try {
            if (this.menuHeader) {
                this.menuHeader.status = true;
                this.menuHeader.maker_id = this.storageService.getUser().id;
                this.passMenuHeaders.push(this.menuHeader);

                this.menuService.manageMenuHeaderStatus(this.passMenuHeaders).subscribe({
                    next: () => {
                        this.passMenuHeaders = [];

                        const index = this.findIndexById(this.menuHeader.id ?? 0);
                        this.menuHeaders[index] = { ...this.menuHeader };

                        this.activateMenuHeaderDialog = false;
                        this.menuHeader = {} as MenuHeader;

                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Menu header activated successfully.',
                            life: 3001
                        });
                    },
                    error: (error) => {
                        const errorMessage = 'Error activating menu header.';
                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: errorMessage,
                            life: 3001
                        });
                    }
                });
            }
        } catch (error) {
            console.error('Error activating role:', error);
        }
    }

    deactivateSelectedMenuHeaders(): void {
        try {
            if (this.selectedMenuHeaders && this.selectedMenuHeaders.length) {
                this.selectedMenuHeaders[0].status = false;
                this.selectedMenuHeaders[0].maker_id = this.storageService.getUser().id;

                this.menuService.manageMenuHeaderStatus(this.selectedMenuHeaders).subscribe({
                    next: (res) => {
                        const _menuHeaders = [...this.menuHeaders];

                        for (const role of this.selectedMenuHeaders) {
                            role.status = false;
                            const index = this.findIndexById(role.id ?? 0);
                            _menuHeaders[index] = role;
                        }

                        this.menuHeaders = _menuHeaders;
                        this.deleteMenuHeadersDialog = false;
                        this.selectedMenuHeaders = [];

                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Menu headers deactivated successfully.',
                            life: 3001
                        });
                    },
                    error: (error) => {
                        let errorMessage = 'Error while deactivating menu headers.';
                        if (error.status === 401) {
                            errorMessage = 'Unauthorized: You do not have the required permissions to access this resource.';
                        } else if (error.status === 403) {
                            errorMessage = 'Forbidden: Your access token has expired.';
                        } else {
                            errorMessage = error.error?.message || errorMessage;
                        }

                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: errorMessage,
                            life: 3001
                        });
                    }
                });
            }
        } catch (error) {
            console.error('Error while deactivating:', error);
        }
    }

    activateSelectedMenuItems(): void {
        try {
            if (this.selectedMenuHeaders && this.selectedMenuHeaders.length) {
                this.selectedMenuHeaders[0].status = true;
                this.selectedMenuHeaders[0].maker_id = this.storageService.getUser().id;

                this.menuService.manageMenuHeaderStatus(this.selectedMenuHeaders).subscribe({
                    next: (res) => {
                        const _menuHeaders = [...this.menuHeaders];

                        for (const role of this.selectedMenuHeaders) {
                            role.status = true;
                            const index = this.findIndexById(role.id ?? 0);
                            _menuHeaders[index] = role;
                        }

                        this.menuHeaders = _menuHeaders;
                        this.activateMenuHeadersDialog = false;
                        this.selectedMenuHeaders = [];

                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Menu headers activated successfully.',
                            life: 3001
                        });
                    },
                    error: (error) => {
                        let errorMessage = 'Error while activating menu headers.';
                        if (error.status === 401) {
                            errorMessage = 'Unauthorized: You do not have the required permissions to access this resource.';
                        } else if (error.status === 403) {
                            errorMessage = 'Forbidden: Your access token has expired.';
                        } else {
                            errorMessage = error.error?.message || errorMessage;
                        }

                        this.messageService.add({
                            severity: 'error',
                            summary: 'Error',
                            detail: errorMessage,
                            life: 3001
                        });
                    }
                });
            }
        } catch (error) {
            console.error('Error while activating:', error);
        }
    }

    excelExport(): void {}
}
