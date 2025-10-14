import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SharedUiModule } from '../../../../shared-ui';
import { ManageTaxService } from '../../../service/reviewer/manage_tax_reviewer-service';
import { StorageService } from '../../../service/sharedService/storage.service';
import { Tax } from '../../../models/maker/tax';
import { User } from '../../../models/admin/user';
import { ManageTaxApproverService } from '../../../service/approver/manage-tax-ho-service';

@Component({
  selector: 'app-reject-checker-approver',
  providers: [MessageService, ConfirmationService],
  imports: [SharedUiModule],
  templateUrl: './reject-checker-approver.component.html',
  styleUrls: ['./reject-checker-approver.component.scss']
})
export class RejectCheckerApproverComponent implements OnInit {
  user: User = new User();
  tax: Tax = new Tax();
  form!: FormGroup;
  submitted = false;

  @Input() passedRejectedTax: any[] = [];
  @Output() rejectedTax: EventEmitter<any> = new EventEmitter();

  constructor(
    private fb: FormBuilder,
    private manageTaxService: ManageTaxService,
    private manageTaxHoService: ManageTaxApproverService,
    private storageService: StorageService,
    private messageService: MessageService,
  ) {}

  ngOnInit(): void {
    this.user = this.storageService.getUser();

    // ✅ Initialize form
    this.form = this.fb.group({
      reference_number: [{ value: '', disabled: true }], 
      checker_rejected_reason: ['', [Validators.required, Validators.minLength(3)]]
    });

    // ✅ Load data if passed
    if (this.passedRejectedTax?.length) {
      this.rejectTax(this.passedRejectedTax);
    }
  }

  // ✅ Populate form with passed data
  rejectTax(passedData: any[]): void {
    this.tax = passedData[0];
    this.form.patchValue({
      reference_number: this.tax.reference_number || '',
      checker_rejected_reason: this.tax.checker_rejected_reason?.trim() || ''
    });
  }

  onSubmit(): void {
  this.submitted = true;

  // ✅ Stop if form is invalid
  if (this.form.invalid) {
    this.messageService.add({
      severity: 'warn',
      summary: 'Validation Error',
      detail: 'Please fill all required fields.',
      life: 3000
    });
    this.submitted = false;
    return;
  }

  // ✅ Safely extract role name
  const roleNames: string[] = (this.user?.roles as unknown as string[]) ?? [];

  
  

  // ✅ Merge form data into tax object
  this.tax = {
    ...this.tax,
    ...this.form.getRawValue(),
    user_id: this.user.id
  };

  // ✅ Choose correct service based on role
  const request$ = roleNames.includes('ROLE_APPROVER')
    ? this.manageTaxHoService.rejectApproverTax(this.tax)
    : this.manageTaxService.rejectReviewerTax(this.tax);

  // ✅ Execute API call
  request$.subscribe({
    next: () => {
      this.submitted = false;
      this.messageService.add({
        severity: 'success',
        summary: `${this.tax.reference_number} successfully rejected`,
        detail: '',
        life: 3000
      });

      // Emit updated data before reset
      this.emitData([this.tax]);

      // Reset form and tax object
      this.form.reset();
      this.tax = new Tax();
    },
    error: () => {
      this.submitted = false;
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'Please try again later.',
        life: 3000
      });
    }
  });
}



  // ✅ Emit data to parent
  emitData(data: any[]): void {
    this.rejectedTax.emit(data);
  }

  // ✅ Reset form manually
  onReset(): void {
    this.submitted = false;
    this.form.reset();
  }

  // ✅ Shortcut for form controls
  get f(): { [key: string]: AbstractControl } {
    return this.form.controls;
  }
}
