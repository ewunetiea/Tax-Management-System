import { Component, ViewChild } from '@angular/core';
import { ConfirmationService, MenuItem } from 'primeng/api';
import { MessageService } from 'primeng/api';
import { MenuItems } from '../../../../models/admin/menu-items';
import { MenuHeader } from '../../../../models/admin/menu-headers';
import { Functionalities } from '../../../../models/admin/functionalities';
import { Table, TableModule } from 'primeng/table';
import { HttpErrorResponse } from '@angular/common/http';
import { MenuItemPermissions } from '../../../../models/admin/menu-item-permissions';
import { Role } from '../../../../models/admin/role';
import { FormsModule } from '@angular/forms';
import { MultiSelectModule } from 'primeng/multiselect';
import { CardModule } from 'primeng/card';
import { ToolbarModule } from 'primeng/toolbar';
import { CommonModule } from '@angular/common';
import { Dialog } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { AccordionModule } from 'primeng/accordion';
import { ListboxModule } from 'primeng/listbox';
import { MessagesModule } from 'primeng/messages';
import { InputNumberModule } from 'primeng/inputnumber';
import { TabViewModule } from 'primeng/tabview';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { IconFieldModule } from 'primeng/iconfield';
import { SelectButtonModule } from 'primeng/selectbutton';
import { InputIconModule } from 'primeng/inputicon';
import { MenuService } from '../../../../service/admin/menu-service';
import { RoleFunctionalityService } from '../../../../service/admin/roleFunctionalityService';
import { StorageService } from '../../../../service/sharedService/storage.service';

@Component({
    selector: 'app-menuitems',
    imports: [
        FormsModule,
        MultiSelectModule,
        CardModule,
        ToolbarModule,
        CommonModule,
        Dialog,
        InputTextModule,
        AccordionModule,
        ListboxModule,
        MessagesModule,
        InputNumberModule,
        TabViewModule,
        ButtonModule,
        TableModule,
        ToastModule,
        BreadcrumbModule,
        IconFieldModule,
        SelectButtonModule,
        InputIconModule
    ],
    providers: [MessageService, ConfirmationService],
    templateUrl: './menuitems.component.html',
    styleUrl: './menuitems.component.scss'
})
export class MenuitemsComponent {
    @ViewChild('dt') dt: any;
    passMenuItems: MenuItems[] = [];
    menuItems: MenuItems[] = [];
    menuHeaders: MenuHeader[] = [];
    filters: any;
    loading: boolean = false;
    menuItemDialog: boolean = false;
    deactivateMenuItemDialog: boolean = false;
    deactivateMenuItemsDialog: boolean = false;
    activateMenuItemDialog: boolean = false;
    activatemenuItemsDialog: boolean = false;
    menuItem: MenuItems = new MenuItems();
    selectedMenuItems: MenuItems[] | null = null;
    submitted: boolean = false;
    globalFilter: string = '';
    globalFilterValue: string = '';
    itemLabelTouched: boolean = false;
    itemToTouched: boolean = false;
    itemLabelExists: boolean = false;
    itemToExists: boolean = false;
    activeIndex: number = 0;
    expandedRows: any[] = [];
    allExpanded: boolean = false;
    permissions: Functionalities[] = [];
    selectedPermissions: Functionalities[] = [];
    permisionDialog: boolean = false;
    activeIndex1: number = 0;
    activeState: boolean[] = [true, false, false];
    breadcrumbText: string = 'Manage menu headers';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;
    sizes!: any[];
    selectedSize: any = 'normal';

    constructor(
        private menuService: MenuService,
        private messageService: MessageService,
        private roleFunctionalityService: RoleFunctionalityService,
        private confirmationService: ConfirmationService,
        private storageService: StorageService
    ) {}

    ngOnInit(): void {
        this.breadcrumbText = 'Manage Menu Items';
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.fetchMenuItems();
    }

    fetchMenuItems(): void {
        this.loading = true;
        this.menuService.getMenuItems().subscribe({
            next: (data) => {
                this.menuItems = data;
                this.loading = false;
            },
            error: (error) => {
                let errorMessage = 'Failed to fetch menu items.';
                if (error.status === 401) {
                    errorMessage = 'Unauthorized: You do not have the required permissions.';
                } else if (error.status === 403) {
                    errorMessage = 'Forbidden: Your access token has expired.';
                } else if (error.error?.message) {
                    errorMessage = error.error.message;
                }

                this.messageService.add({
                    severity: 'error',
                    summary: 'Error',
                    detail: errorMessage,
                    life: 3000
                });
                this.loading = false;
            }
        });
    }

    checkItemLabelExistence(itemLabel: string): void {
        const exists = this.menuItems.some((p) => p.item_label?.trim().toLowerCase() === itemLabel.trim().toLowerCase());
        this.itemLabelExists = exists;
        this.submitted = true;
    }

    checkItemToExistence(itemTo: string): void {
        const exists = this.menuItems.some((p) => p.item_to?.trim().toLowerCase() === itemTo.trim().toLowerCase());
        this.itemToExists = exists;
    }

    openNew(): void {
        this.fetchCommonMenuHeaders();
        this.menuItem = new MenuItems();
        this.menuItem.item_icon = 'pi pi-fw pi-';
        this.submitted = false;
        this.menuItemDialog = true;
    }

    editMenuItem(menuitem: MenuItems): void {
        menuitem = menuitem || new MenuItems();
        this.fetchCommonMenuHeaders();
        this.menuItemDialog = true;
    }

    clear(table: Table) {
        table.clear();
    }

    onGlobalFilter(table: Table, event: Event) {
        const inputValue = (event.target as HTMLInputElement).value;
    }

    hideDialog(): void {
        this.submitted = false;
        this.menuItemDialog = true;
    }

    fetchCommonMenuHeaders(): void {
        this.menuService.getCommonMenuHeaders().subscribe({
            next: (data: any) => {
                this.menuHeaders = data;
            },
            error: (error: HttpErrorResponse) => {
                let errorMessage = 'Failed to fetch common menu headers.';

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

    saveMenuItem(menuItemData: MenuItems): void {
        this.submitted = true;
        menuItemData.maker_id = this.storageService.getUser()?.id;

        this.menuService.addMenuItem(menuItemData).subscribe({
            next: (res) => {
                if (menuItemData.item_label?.trim()) {
                    const _menuItems = [...this.menuItems];
                    const _menuItem = menuItemData;

                    if (menuItemData.id) {
                        const index = this.findIndexById(menuItemData.id);
                        _menuItems[index] = _menuItem;

                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Menu Item Updated',
                            life: 3001
                        });
                    } else {
                        _menuItems.push(_menuItem);
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Menu Item Created',
                            life: 3001
                        });
                    }

                    this.menuItems = _menuItems;
                    this.menuItemDialog = false;
                    this.menuItem = new MenuItems();
                }
            },
            error: (error: HttpErrorResponse) => {
                let errorMessage = 'Failed to save menu item.';

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

    confirmDeactivateMenuItem(menuItemData: MenuItems): void {
        this.menuItem = menuItemData;
        this.deactivateMenuItemDialog = true;
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected menu item?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.deactivateMenuItem()
        });
    }

    confirmActivateMenuItem(menuItemData: MenuItems): void {
        this.menuItem = menuItemData;
        this.activateMenuItemDialog = true;
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected menu item?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.activateMenuItem()
        });
    }

    confirmDeactivateSelected(): void {
        this.deactivateMenuItemsDialog = true;
        this.confirmationService.confirm({
            message: 'Are you sure you want to deactivate selected menu items?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.deactivateSelectedMenuItems()
        });
    }

    confirmActivateSelected(): void {
        this.activatemenuItemsDialog = true;
        this.confirmationService.confirm({
            message: 'Are you sure you want to activate selected menu items?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.activateselectedMenuItems()
        });
    }

    deleteItemDialog(menuItemData: MenuItems): void {
        menuItemData.delete = true;
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete selected menu item?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => this.deleteMenuItem(menuItemData)
        });
    }

    deleteMenuItem(menuItemData: MenuItems) {
        if (menuItemData) {
            menuItemData.maker_id = this.storageService.getUser()?.id;
            this.passMenuItems.push(menuItemData);

            this.menuService.manageMenuItemStatus(this.passMenuItems).subscribe({
                next: (res) => {
                    this.passMenuItems = [];
                    menuItemData.status = false;

                    const index = this.findIndexById(menuItemData.id ?? 0);
                    this.menuItems[index] = menuItemData;

                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Menu item deleted successfully',
                        life: 3001
                    });
                },
                error: (error) => {
                    let errorMessage = 'Error deleting menu item';
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
    }

    deactivateMenuItem() {
        if (this.menuItem) {
            this.menuItem.status = false;
            this.menuItem.maker_id = this.storageService.getUser()?.id;
            this.passMenuItems.push(this.menuItem);

            this.menuService.manageMenuItemStatus(this.passMenuItems).subscribe({
                next: (res) => {
                    this.passMenuItems = [];

                    const index = this.findIndexById(this.menuItem.id ?? 0);
                    this.menuItems[index] = { ...this.menuItem };

                    this.deactivateMenuItemDialog = false;
                    this.menuItem = new MenuItems();

                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Menu item deactivated successfully',
                        life: 3001
                    });
                },
                error: (error) => {
                    let errorMessage = 'Error deactivating menu item';
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
    }

    activateMenuItem(): void {
        if (this.menuItem) {
            this.menuItem.status = true;
            this.menuItem.maker_id = this.storageService.getUser()?.id;
            this.passMenuItems = [this.menuItem];

            this.menuService.manageMenuItemStatus(this.passMenuItems).subscribe({
                next: () => {
                    const index = this.findIndexById(this.menuItem.id ?? 0);
                    this.menuItems[index] = { ...this.menuItem };

                    this.activateMenuItemDialog = false;
                    this.menuItem = new MenuItems();

                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Menu item activated successfully',
                        life: 3001
                    });
                },
                error: (error) => {
                    let errorMessage = 'Error activating menu item';
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
    }

    deactivateSelectedMenuItems(): void {
        if (this.selectedMenuItems) {
            const userId = this.storageService.getUser()?.id;
            this.selectedMenuItems = this.selectedMenuItems.map((item) => ({
                ...item,
                status: false,
                maker_id: userId
            }));

            this.menuService.manageMenuItemStatus(this.selectedMenuItems).subscribe({
                next: () => {
                    for (const item of this.selectedMenuItems!) {
                        const index = this.findIndexById(item.id ?? 0);
                        this.menuItems[index] = { ...item };
                    }

                    this.deactivateMenuItemsDialog = false;
                    this.selectedMenuItems = null;

                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Menu items deactivated successfully',
                        life: 3001
                    });
                },
                error: (error) => {
                    let errorMessage = 'Error deactivating menu items';
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
                    this.deactivateMenuItemsDialog = false;
                }
            });
        }
    }

    activateselectedMenuItems(): void {
        if (this.selectedMenuItems) {
            const userId = this.storageService.getUser()?.id;
            this.selectedMenuItems = this.selectedMenuItems.map((item) => ({
                ...item,
                status: true,
                maker_id: userId
            }));

            this.menuService.manageMenuItemStatus(this.selectedMenuItems).subscribe({
                next: () => {
                    for (const item of this.selectedMenuItems!) {
                        const index = this.findIndexById(item.id ?? 0);
                        this.menuItems[index] = { ...item };
                    }

                    this.activatemenuItemsDialog = false;
                    this.selectedMenuItems = null;

                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Menu items activated successfully',
                        life: 3001
                    });
                },
                error: (error) => {
                    let errorMessage = 'Error activating menu items';
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
                    this.activatemenuItemsDialog = false;
                }
            });
        }
    }

    fetchPermissions(role: Role): void {
        this.roleFunctionalityService.getFunctionalitiesByRoleCategory(role).subscribe({
            next: (data: Functionalities[]) => {
                this.permissions = data;
            }
        });
    }

    openManagePermissions(menuItem: MenuItems): void {
        this.menuItem = { ...menuItem };
        this.selectedPermissions = menuItem.permissions || [];

        if (menuItem.menuHeader?.roles || menuItem.menuHeader?.roles?.length) {
            this.fetchPermissions(menuItem.menuHeader.roles[0]);
        }

        this.permisionDialog = true;
        this.activeIndex = 1;
    }

    editItemPermission(): void {
        this.menuItem.permissions = this.selectedPermissions || [];

        const menuItemPermission: MenuItemPermissions = {
            item_id: this.menuItem.id,
            maker_id: this.storageService.getUser()?.id,
            status: true,
            permissions: this.selectedPermissions || []
        };

        try {
            this.menuService.manageItemPermissions(menuItemPermission).subscribe({
                next: () => {
                    const index = this.findIndexById(this.menuItem.id ?? 0);
                    if (index !== -1) {
                        this.menuItems[index] = { ...this.menuItem };
                    }
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Permissions updated successfully',
                        life: 3001
                    });
                    this.permisionDialog = false;
                },
                error: () => {
                    this.messageService.add({
                        severity: 'error',
                        summary: 'Error',
                        detail: 'Failed to update permissions',
                        life: 3001
                    });
                }
            });
        } catch (error) {
            this.messageService.add({
                severity: 'error',
                summary: 'Error',
                detail: 'An unexpected error occurred while updating permissions',
                life: 3001
            });
        }
    }

    concatenateUniqueHeader(menuHeaders: MenuHeader[], separator: string = ', '): string {
        const seenTargets = new Set<string>(); // To keep track of added targets

        return menuHeaders
            .filter((menu_header) => menu_header.header && menu_header.header.trim() !== '') // Exclude empty or undefined headers
            .map((menu_header) => menu_header.header!.trim()) // Extract and trim the header
            .filter((header) => {
                if (seenTargets.has(header)) {
                    return false; // Skip if the header has already been added
                }
                seenTargets.add(header); // Mark the target as added
                return true;
            })
            .join(separator);
    }

    concatenateUniqueTargets(menuHeaders: MenuHeader[], separator: string = ', '): string {
        const seenTargets = new Set<string>(); // To keep track of added targets

        return menuHeaders
            .filter((header) => header.target_roles && header.target_roles.trim() !== '') // Exclude empty or undefined target_roless
            .map((header) => header.target_roles!.trim()) // Extract and trim the target_roles
            .filter((target_roles) => {
                if (seenTargets.has(target_roles)) {
                    return false; // Skip if the target_roles has already been added
                }
                seenTargets.add(target_roles); // Mark the target as added
                return true;
            })
            .join(separator);
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < (this.menuItems as any)?.length; i++) {
            if ((this.menuItems as any)[i].id === id) {
                index = i;
                break;
            }
        }

        return index;
    }
}
