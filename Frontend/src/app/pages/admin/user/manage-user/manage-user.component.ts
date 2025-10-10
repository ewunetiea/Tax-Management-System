import { Component, ViewChild } from '@angular/core';
import { environment } from '../../../../../environments/environment.prod';
import { User } from '../../../../models/admin/user';
import { HttpErrorResponse } from '@angular/common/http';
import { MessageService, ConfirmationService, MenuItem } from 'primeng/api';
import { Region } from '../../../../models/admin/region';
import { Role } from '../../../../models/admin/role';
import { Branch } from '../../../../models/admin/branch';
import { MultiUserRole } from '../../../../models/admin/multiUserRole';
import { CreateEditUserComponent } from '../create-edit-user/create-edit-user.component';
import { Table, TableRowCollapseEvent, TableRowExpandEvent } from 'primeng/table';
import { UserSearchEngineComponent } from '../user-search-engine/user-search-engine.component';
import { SharedUiModule } from '../../../../../shared-ui';
import { RoleService } from '../../../../service/admin/roleService';
import { UserService } from '../../../../service/admin/user.service';
import { RegionService } from '../../../../service/admin/regionService';
import { ExportExcelService } from '../../../../service/sharedService/export-excel.service';
import { BranchService } from '../../../../service/admin/branchService';

@Component({
    selector: 'app-manage-user',
    imports: [SharedUiModule, CreateEditUserComponent, UserSearchEngineComponent],
    templateUrl: './manage-user.component.html',
    styleUrl: './manage-user.component.scss',
    providers: [MessageService, ConfirmationService]
})
export class ManageUserComponent {
    environment = environment;
    exportSettings = {
        columnsHeader: true,
        fileName: 'users',
        hiddenColumns: false
    };
    expandedRows = {};

    //used for exporting, Header titles given
    data2 = new Array();
    user: User = {
        roles: []
    };
    columns: any[] = [];
    users!: User[];
    selectedUser: User = new User();
    loading: boolean = true;
    fetching: boolean = false;
    userEditDialog: boolean = false;
    selectedUsers: User[] = [];
    passUsers: User[] = [];
    activeIndex1: number = 0;
    activeState: boolean[] = [true, false, false];
    
    events1: any[] = [];
    risk_levels: any[] = [];
    auditor = new User();
    category = '';
    outputUser: any[] = [];
    isEditData = false;
    roleDialog = false;
    branchDialog = false;
    roles: Role[] = [];
    categoryRoles: Role[] = [];
    allRoles: Role[] = [];
    branches: Branch[] = [];
    hoWorkPlaces: Branch[] = [];
    loadLazyTimeout: any;
    loading_dropdown = true;
    regionDropdownOptions: Region[] = [];
    branchDropdownOptions: Branch[] = [];
    hoDropdownOptions: Branch[] = [];
    radio_value: string = '';
    role_radio_value: string = 'Specific';
    regions: Region[] = [];
    regionFilter: Region[] = [];
    ho: Region[] = [];
    unitLoading = false;
    multipleUserRoleDialog = false;
    originalBranches: Branch[] = [];
    originalHOUnits: Branch[] = [];
    multipleUserRole: MultiUserRole = {
        intial_role: new Role(),
        selected_roles: []
    };
    multipleRoleUserLoading = false;
    @ViewChild('dt') dt!: any;
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Users';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;

    constructor(
        private userService: UserService,
        private exportService: ExportExcelService,
        private messageService: MessageService,
        private roleService: RoleService,
        private branchService: BranchService,
        private confirmationService: ConfirmationService,
        private regionService: RegionService
    ) {}

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.getUsers();
    }

    expandAll() {
        // this.expandedRows = this.products.reduce((acc, p) => (acc[p.id] = true) && acc, {});
    }

    collapseAll() {
        this.expandedRows = {};
    }

    onRowExpand(event: TableRowExpandEvent) {
        this.messageService.add({ severity: 'info', summary: 'User Information Expanded', detail: event.data.name, life: 3000 });
    }

    onRowCollapse(event: TableRowCollapseEvent) {
        this.messageService.add({ severity: 'success', summary: 'User information Collapsed', detail: event.data.name, life: 3000 });
    }
    getUsers() {
        this.userService.getUsers().subscribe(
            (response) => {
                this.users = response;
                this.loading = false;
                const now = new Date();
                var date = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
                this.exportSettings = {
                    columnsHeader: true,
                    fileName: `Awash Bank - System Users ${date}`,
                    hiddenColumns: false
                };
            },
            (error) => (error: HttpErrorResponse) => {
                this.loading = false;

                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching users data!',
                    detail: ''
                });
            }
        );
    }

    toggle(index: number) {
        this.activeState[index] = !this.activeState[index];
    }

    onGlobalFilter(table: Table, event: Event) {
        const input = event.target as HTMLInputElement;
        table.filterGlobal(input.value, 'contains');
    }

    clear(table: Table) {
        table.clear();
    }

    openRoleDropDown() {
        const value = this.role_radio_value;

        if (value.includes('Specific')) {
            this.roles = this.categoryRoles;
            if (this.selectedUser.roles) {
                for (const role of this.selectedUser.roles) {
                    this.roles[this.findRoleIndexById(role.id)] = role;
                }
            }
        } else {
            this.roles = this.allRoles;

            console.log(this.allRoles);
            if (this.selectedUser.roles) {
                for (const role of this.selectedUser.roles) {
                    this.roles.push(role);
                }
            }
        }
    }

    openModal(user: User) {
        try {
            this.getRoles(user.category);
            this.getAllRoles();
        } catch (error) {}
        this.roles = this.categoryRoles;
        this.selectedUser = user;
        if (this.selectedUser.roles) {
            for (const role of this.selectedUser.roles) {
                this.roles.push(role);
                // this.roles[this.findRoleIndexById(role.id)] = role;
            }
        }
        this.roleDialog = true;
    }

    openBranchModal(user: User) {
        this.branchDialog = true;
        this.selectedUser = user;
        if (this.selectedUser.region && this.selectedUser.branch) {
            this.radio_value = 'Region';
            if (this.selectedUser.branch.region?.name?.toLocaleLowerCase().trim() === 'ho' || this.selectedUser.branch.region?.name?.toLocaleLowerCase().trim() === 'head office') {
                this.radio_value = this.radio_value + ', HO';
            } else {
                this.radio_value = this.radio_value + ', Branch';
            }
        } else if (this.selectedUser.region && !this.selectedUser.branch) {
            this.radio_value = 'Region';
        } else if (!this.selectedUser.region && this.selectedUser.branch) {
            if (this.selectedUser.branch.region?.name?.toLocaleLowerCase().trim() === 'ho' || this.selectedUser.branch.region?.name?.toLocaleLowerCase().trim() === 'head office') {
                this.radio_value = 'HO';
            } else {
                this.radio_value = 'Branch';
            }
        }

        this.getRegions();
        this.getBranches();
        // if (this.selectedUser.roles) {
        //   for (const role of this.selectedUser.roles) {
        //     this.roles.push(role);
        //   }
        // }
    }

    manageRoles() {
        this.userService.manageRoles(this.selectedUser).subscribe({
            next: (data) => {
                this.roleDialog = false;
                this.messageService.add({
                    severity: 'success',
                    summary: ` Mapping user with roles updated successfully`,

                    detail: ''
                });
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while updating roles !',
                    detail: ''
                });
            }
        });
    }

    getBranches(): void {
        this.branchService.getActiveBranchesList().subscribe({
            next: (data) => {
                this.branches = data.filter((branch) => branch.region?.id != this.ho[0].id);
                this.originalBranches = this.branches;
                this.hoWorkPlaces = data.filter((branch) => branch.region?.id === this.ho[0].id);
                this.originalHOUnits = this.hoWorkPlaces;
            },
            error: (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching branches data!',
                    detail: ''
                });
            }
        });
    }

    filterBranches(user: User) {
        this.branches = this.originalBranches.filter((branch) => branch.region?.id == user?.region?.id);

        this.hoWorkPlaces = this.originalHOUnits.filter((branch) => branch.region?.id == user?.region?.id);
    }

    getRegions() {
        this.regionService.getActiveRegions().subscribe({
            next: (data: any) => {
                this.regions = data;
                this.regionFilter = this.regions.filter((region) => region.name?.toLocaleLowerCase().trim() != 'ho' || region.name?.toLocaleLowerCase().trim() != 'head office');

                this.ho = this.regions.filter((region) => region.name?.toLocaleLowerCase().trim() === 'ho' || region.name?.toLocaleLowerCase().trim() === 'head office');
            },

            error: (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching regions data!',
                    detail: ''
                });
            }
        });
    }

    transferBranch() {
        this.unitLoading = true;
        this.userService.transferBranch(this.selectedUser).subscribe({
            next: (data) => {
                this.branchDialog = false;
                this.unitLoading = false;
                this.messageService.add({
                    severity: 'success',
                    summary: ` User ${this.selectedUser.first_name}  ${this.selectedUser.middle_name} is transferred successfully`,
                    detail: ''
                });
            },
            error: (error: HttpErrorResponse) => {
                this.loading = false;
                this.unitLoading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while transferring user !',
                    detail: ''
                });
            }
        });
    }

    getRoles(category: any) {
        this.roleService.getRolesCategory(category).subscribe({
            next: (data) => {
                this.categoryRoles = data;
            },
            error: (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching roles !',
                    detail: ''
                });
            }
        });
    }

    openMultipleUserRoleDialog() {
        this.getAllRoles();
        this.multipleUserRoleDialog = true;
    }

    getAllRoles() {
        this.roleService.getRoles().subscribe({
            next: (data) => {
                this.allRoles = data;
            },
            error: (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching roles !',
                    detail: ''
                });
            }
        });
    }

    openNew() {
        this.outputUser = [];
        this.user = new User();
        this.isEditData = true;
        this.outputUser.push(this.user);
        this.outputUser.push(this.isEditData);
        this.userEditDialog = true;
    }

    editUser(user: User) {
        this.outputUser = [];
        this.user = { ...user };
        this.isEditData = false;
        this.outputUser.push(this.user);
        this.outputUser.push(this.isEditData);
        this.userEditDialog = true;
    }

    onDataChange(data: any) {
        if (data[1]) {
            this.getUsers();
            this.users = [...this.users];
            this.userEditDialog = false;
            this.user = new User();
        } else {
            this.users[this.findIndexById(data[0].id)] = data[0];
        }
        this.userEditDialog = false;
    }

    onDataGenerated(data: User[]) {
        this.loading = false;
        if (data != null) {
            this.fetching = true;
            this.users = data;
            const now = new Date();
            var date = now.getFullYear() + '-' + (now.getMonth() + 1) + '-' + now.getDate();
            this.exportSettings = {
                columnsHeader: true,
                fileName: `Awash Bank - System Users ${date}`,
                hiddenColumns: false
            };
        }
    }

    onDropdownLoad(event: any, identifier: String) {
        this.loading_dropdown = true;
        if (this.loadLazyTimeout) {
            clearTimeout(this.loadLazyTimeout);
        }
        this.loadLazyTimeout = setTimeout(
            () => {
                const { first, last } = event;
                if (identifier == 'Region') {
                    this.regionDropdownOptions = this.regionFilter;
                } else if (identifier == 'Branch') {
                    this.branchDropdownOptions = this.branches;
                } else if (identifier == 'HO') {
                    this.hoDropdownOptions = this.hoWorkPlaces;
                }
                this.loading_dropdown = false;
            },
            Math.random() * 1000 + 250
        );
    }

    openDropDown(item: any) {
        if (item.name.includes('HO') || item.name.includes('ALL') || item.name.includes('Branch')) {
            this.getBranches();
        }
        if (item.name.includes('Region') || item.name.includes('ALL')) {
            this.getRegions();
        }
    }

    findRoleIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.roles.length; i++) {
            if (this.users[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.users.length; i++) {
            if (this.users[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    generateData(): any[] {
        let data = new Array();
        let i = 0;
        for (const user of this.users) {
            let row: any = {
                branch_code: '',
                region_code: '',
                assigned_place: ''
            };
            row['first_name'] = user.first_name;
            row['last_name'] = user.last_name;
            row['phone_number'] = user.phone_number;
            row['email'] = user.email;
            row['gender'] = user.gender;
            row['employee_id'] = user.employee_id;
            if (user.branch) {
                row['assigned_place'] = user.branch?.name;
                row['branch_code'] = user.branch?.code;
            } else if (user.region) {
                row['assigned_place'] = user.region?.name;
                row['region_code'] = user.region?.code;
            }
            row['status'] = user.status;
            row['roles'] = user.roles;
            row['id'] = user.id;
            data[i] = row;

            let row2: any = {};

            row2['First Name'] = user.first_name;
            row2['Last Name'] = user.last_name;
            row2['Phone Number'] = user.phone_number;
            row2['Email'] = user.email;
            row2['Gender'] = user.gender;
            row2['Employee ID'] = user.employee_id;
            if (user.branch) {
                row2['Assigned Place'] = user.branch?.name;
                row2['Branch Code'] = user.branch?.code;
            } else if (user.region) {
                row2['Assigned Place'] = user.region?.name;
                row2['Region Code'] = user.region?.code;
            }
            row2['Status'] = user.status ? 'Active' : 'Inactive';
            row2['Roles'] = user.roles?.map((item) => item['name']).toString();
            this.data2[i] = row2;
            i++;
        }
        // console.log(this.data2)
        return data;
    }

    generateExportData(): any[] {
        return this.data2;
    }

    excelExport(): void {
        let reportData = {
            sheet_name: 'System Users',
            title: 'FCY & Loan Management System - System Users',
            data: this.generateExportData(),
            headers: Object.keys(this.generateExportData()[0])
        };
        this.exportService.exportExcel(reportData);
    }

    makeSpecialUser(user: User) {
        user.special_user = true;
        this.passUsers.push(user);
        this.confirmationService.confirm({
            message: 'Are you sure you want to change selected user category to special?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.userService.makeUserSpecial(this.passUsers).subscribe({
                    next: (response) => {
                        // this.users = this.users.filter((val) => val.id !== user.id);
                        this.passUsers = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Selected user category updated to special.',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while updating user category!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    makeSpecialSelectedUsers() {
        this.selectedUsers[0].special_user = true;
        this.confirmationService.confirm({
            message: 'Are you sure you want to update the selected users category to Special?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.userService.makeUserSpecial(this.selectedUsers).subscribe({
                    next: (response) => {
                        // this.users = this.users.filter(
                        //   (val) => !this.selectedUsers.includes(val)
                        // );
                        this.selectedUsers = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Selected users category updated to special.',
                            life: 3000
                        });
                    },
                    error: (error: HttpErrorResponse) => {
                        this.loading = false;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while updating users category!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    // Function to filter users based on role_id
    filterUsersByRoleId = (users: User[], roleId: number): User[] => {
        return users.filter((user) => user.roles?.some((role) => role.id === roleId) ?? false);
    };

    manageMultipleUserRole() {
        this.multipleRoleUserLoading = true;
        this.passUsers = this.filterUsersByRoleId(this.selectedUsers, this.multipleUserRole?.intial_role?.id);
        if (this.passUsers != null && this.passUsers.length != 0) {
            this.passUsers[0].roles = this.multipleUserRole.selected_roles;
            this.confirmationService.confirm({
                message: `Are you sure you want to change the ${this.passUsers.length} users initial roles to the intended roles?`,
                header: 'Confirm',
                icon: 'pi pi-exclamation-triangle',
                accept: () => {
                    this.userService.manageMultipleUserRole(this.passUsers).subscribe({
                        next: (response) => {
                            this.multipleRoleUserLoading = false;
                            this.multipleUserRoleDialog = false;
                            this.passUsers = [];
                            this.messageService.add({
                                severity: 'success',
                                summary: 'Successful',
                                detail: 'Users initial roles have been successfully updated to the intended roles.',
                                life: 3000
                            });
                        },
                        error: (error: HttpErrorResponse) => {
                            this.multipleRoleUserLoading = false;
                            this.messageService.add({
                                severity: 'error',
                                summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while updating users roles!',
                                detail: ''
                            });
                        }
                    });
                }
            });
        } else {
            this.messageService.add({
                severity: 'error',
                summary: 'Please select a role that has users!',
                detail: ''
            });
            this.multipleRoleUserLoading = false;
        }
    }
}
