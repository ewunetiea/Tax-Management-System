import { Component } from '@angular/core';
import { Contact } from '../../../../models/admin/contact';
import { Feedback } from '../../../../models/admin/feedback';
import { ConfirmationService, MenuItem, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { HttpErrorResponse } from '@angular/common/http';
import { SharedUiModule } from '../../../../../shared-ui';
import { StorageService } from '../../../../service/sharedService/storage.service';
import { ContactService } from '../../../../service/admin/contact-service';

@Component({
    selector: 'app-manage-contact',
    imports: [SharedUiModule],
    
    templateUrl: './manage-contact.component.html',
    styleUrl: './manage-contact.component.scss'
})
export class ManageContactComponent {
    public config = {
        toolbar: {
            items: ['heading', '|', 'bold', 'italic', '|', 'bulletedList', 'numberedList', '|', 'insertTable', 'tableColumn', 'tableRow', 'mergeTableCells', '|', 'undo', 'redo']
        },
        language: 'en',
        table: {
            contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells']
        }
    };

    isLoggedIn = false;
    message = '';
    submitted = false;
    contact: Contact = new Contact();
    selectedContact: Contact = new Contact();
    admin: boolean = false;
    approver: boolean = false;
    maker: boolean = false;
    ho: boolean = false;
    allContact: Contact[] = [];
    selectedContacts: Contact[] = [];
    passContacts: Contact[] = [];
    feedbacks: Feedback[] = [];
    selectedFeedbacks: Feedback[] = [];
    passFeedbacks: Feedback[] = [];
    respondedFeedbacks: Feedback[] = [];
    selectedRespondedFeedbacks: Feedback[] = [];
    feedback = new Feedback();
    contactDialog = false;
    feedbackDialog = false;
    contact_loading = false;
    contact_form_loading = false;
    feedback_loading = false;
    feedback_form_loading = false;
    sizes!: any[];
    selectedSize: any = 'normal';
    breadcrumbText: string = 'Manage Contacts';
    items: MenuItem[] | undefined;
    home: MenuItem | undefined;

    constructor(
        private storageService: StorageService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService,
        private contactService: ContactService
    ) {}

    ngOnInit(): void {
        this.home = { icon: 'pi pi-home', routerLink: '/' };
        this.items = [{ label: this.breadcrumbText }];
        this.sizes = [
            { name: 'Small', value: 'small' },
            { name: 'Normal', value: 'normal' },
            { name: 'Large', value: 'large' }
        ];
        this.getContacts();
        this.getFeedbacks();
    }

    getContacts() {
        this.contactService.getContacts().subscribe(
            (data) => {
                this.allContact = data;
                this.contact_loading = false;
            },
            (error) => {
                this.contact_loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching contacts!',

                    detail: ''
                });
            }
        );
    }

    getFeedbacks() {
        this.contactService.getFeedbacks().subscribe(
            async (response: any) => {
                this.feedbacks = response;
                this.respondedFeedbacks = this.feedbacks.filter((val) => val.response != null);

                this.feedbacks = this.feedbacks.filter((val) => val.response == null);

                this.feedback_loading = false;
            },
            (error: HttpErrorResponse) => {
                this.feedback_loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while fetching feedbacks!',

                    detail: ''
                });
            }
        );
    }

    addContact(): void {
        this.contact_form_loading = true;
        this.selectedContact.registered_by = this.storageService.getUser().id;
        this.contactService.saveContact(this.selectedContact).subscribe({
            next: (res) => {
                this.contact_form_loading = false;
                if (this.selectedContact.id) {
                    this.allContact[this.findIndexContactById(this.selectedContact.id)] = this.selectedContact;

                    this.messageService.add({
                        severity: 'success',
                        summary: ` Contact successfully updated`,
                        detail: '',
                        life: 3000
                    });
                } else {
                    this.messageService.add({
                        severity: 'success',
                        summary: ` Contact successfully created`,
                        detail: '',
                        life: 3000
                    });

                    this.getContacts();
                }
                this.selectedContact = new Contact();
                this.contactDialog = false;
            },
            error: (error: HttpErrorResponse) => {
                this.contact_form_loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while adding contact!',

                    detail: ''
                });
            }
        });
    }

    openNewContact() {
        this.selectedContact = new Contact();
        this.contactDialog = true;
    }

    editContact(contact: Contact) {
        this.selectedContact = { ...contact };
        this.contactDialog = true;
    }

    responseFeedback(feedback: Feedback) {
        this.feedback = { ...feedback };
        this.feedbackDialog = true;
    }

    clear(table: Table) {
        table.clear();
    }

    addResponse() {
        this.passFeedbacks = [];
        this.passFeedbacks.push(this.feedback);
        this.contactService.respondFeedbacks(this.passFeedbacks).subscribe({
            next: (response) => {
                this.getFeedbacks();
                this.feedback = new Feedback();
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Response Added',
                    life: 3000
                });
                this.feedback_loading = false;
                this.feedbackDialog = false;
            },
            error: (error: HttpErrorResponse) => {
                this.feedback_loading = false;
                this.messageService.add({
                    severity: 'error',
                    summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while adding response!',
                    detail: ''
                });
            }
        });
    }

    findIndexById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.feedbacks.length; i++) {
            if (this.feedbacks[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    findIndexContactById(id: number): number {
        let index = -1;
        for (let i = 0; i < this.allContact.length; i++) {
            if (this.allContact[i].id === id) {
                index = i;
                break;
            }
        }
        return index;
    }

    deleteContact(contact: Contact) {
        this.passContacts.push(contact);
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete selected contact?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.contactService.deleteContacts(this.passContacts).subscribe({
                    next: (response) => {
                        this.allContact = this.allContact.filter((val) => val.id !== contact.id);
                        this.contact = new Contact();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Contact deleted',
                            life: 3000
                        });
                        this.contact_loading = false;
                    },
                    error: (error: HttpErrorResponse) => {
                        this.contact_loading = false;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deleting contact!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    deleteSelectedContacts() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete selected contacts?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.contactService.deleteContacts(this.selectedContacts).subscribe({
                    next: (response) => {
                        this.allContact = this.allContact.filter((val) => !this.selectedContacts.includes(val));
                        this.selectedContacts = [];
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Contacts Deleted',
                            life: 3000
                        });
                        this.contact_loading = false;
                    },
                    error: (error: HttpErrorResponse) => {
                        this.contact_loading = false;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deleting contacts!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    closeFeedback(feedback: Feedback, state: String) {
        this.passFeedbacks = [];
        this.passFeedbacks.push(feedback);
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete selected feedback?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.contactService.closeFeedbacks(this.passFeedbacks).subscribe({
                    next: (response) => {
                        if (state.includes('pending')) {
                            this.feedbacks = this.feedbacks.filter((val) => val.id !== feedback.id);
                        } else {
                            this.respondedFeedbacks = this.respondedFeedbacks.filter((val) => val.id !== feedback.id);
                        }

                        this.feedback = new Feedback();
                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Feedback deleted',
                            life: 3000
                        });
                        this.feedback_loading = false;
                    },
                    error: (error: HttpErrorResponse) => {
                        this.feedback_loading = false;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deleting feedback!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }

    closeSelectedFeedbacks(state: String) {
        if (state.includes('pending')) {
            this.passFeedbacks = this.selectedFeedbacks;
        } else {
            this.passFeedbacks = this.selectedRespondedFeedbacks;
        }
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete selected feedbacks?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
                this.contactService.closeFeedbacks(this.passFeedbacks).subscribe({
                    next: (response) => {
                        if (state.includes('pending')) {
                            this.feedbacks = this.feedbacks.filter((val) => !this.selectedFeedbacks.includes(val));
                            this.selectedFeedbacks = [];
                        } else {
                            this.respondedFeedbacks = this.respondedFeedbacks.filter((val) => !this.selectedRespondedFeedbacks.includes(val));
                            this.selectedRespondedFeedbacks = [];
                        }

                        this.messageService.add({
                            severity: 'success',
                            summary: 'Successful',
                            detail: 'Feedbacks Deleted',
                            life: 3000
                        });
                        this.feedback_loading = false;
                    },
                    error: (error: HttpErrorResponse) => {
                        this.feedback_loading = false;
                        this.messageService.add({
                            severity: 'error',
                            summary: error.status == 401 ? 'You are not permitted to perform this action!' : 'Something went wrong while deleting feedbacks!',
                            detail: ''
                        });
                    }
                });
            }
        });
    }
}
