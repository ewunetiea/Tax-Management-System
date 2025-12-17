import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { SharedUiModule } from '../../../../shared-ui';
import { ManageTaxService } from '../../../service/reviewer/manage_tax_reviewer-service';
import { StorageService } from '../../../service/sharedService/storage.service';
import { Tax } from '../../../models/maker/tax';
import { User } from '../../../models/admin/user';
import { ManageTaxApproverService } from '../../../service/approver/manage-tax-ho-service';
import { Observable } from 'rxjs';
import { ToastModule } from 'primeng/toast';
import { InputSanitizer } from 'app/SQLi-XSS-Prevention/InputSanitizer';
import { xssSqlValidator } from 'app/SQLi-XSS-Prevention/xssSqlValidator';

@Component({
  selector: 'app-reject-checker-approver',
  standalone: true,
  imports: [SharedUiModule, ToastModule],
  templateUrl: './reject-checker-approver.component.html',
  styleUrls: ['./reject-checker-approver.component.scss']
})
export class RejectCheckerApproverComponent implements OnInit {
  user: User = new User();
  tax: Tax = new Tax();
  form!: FormGroup;
  submitted = false;
  isApprover = false;
  tagFilter: RegExp = InputSanitizer.attackRegex;

  @Input() routeControl = "";
  @Input() passedRejectedTax: any[] = [];
  @Output() rejectedTax: EventEmitter<any> = new EventEmitter();

  constructor(
    private fb: FormBuilder,
    private manageTaxService: ManageTaxService,
    private manageTaxHoService: ManageTaxApproverService,
    private storageService: StorageService,
    private messageService: MessageService,
  ) { }

  ngOnInit(): void {
    this.user = this.storageService.getUser();
    const roleNames: string[] = (this.user?.roles as unknown as string[]) ?? [];
    this.isApprover = roleNames.includes('ROLE_APPROVER');

    // ✅ Initialize form
    this.form = this.fb.group({
      reference_number: [{ value: '', disabled: true }],
      checker_rejected_reason: [''],
      approver_rejected_reason: [''],
    });

    // ✅ Load data if passed
    if (this.passedRejectedTax?.length) {
      this.rejectTax(this.passedRejectedTax);
    }
    this.setRoleValidators();
  }

  setRoleValidators() {
  if (this.isApprover) {
    // Approver required
    this.form.get('approver_rejected_reason')?.setValidators([Validators.required, Validators.minLength(3), Validators.maxLength(250), xssSqlValidator]);

    // Checker not required
    this.form.get('checker_rejected_reason')?.clearValidators();
    this.form.get('checker_rejected_reason')?.setValue('');
  } else {
    // Checker required
    this.form.get('checker_rejected_reason')?.setValidators([Validators.required, Validators.minLength(3), Validators.maxLength(250), xssSqlValidator]);

    // Approver not required
    this.form.get('approver_rejected_reason')?.clearValidators();
    this.form.get('approver_rejected_reason')?.setValue('');
  }

  // Update form after changes
  this.form.get('approver_rejected_reason')?.updateValueAndValidity();
  this.form.get('checker_rejected_reason')?.updateValueAndValidity();
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

    if (this.routeControl != "sent") {
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
    }
    const roleNames: string[] = (this.user?.roles as unknown as string[]) ?? [];
    this.tax = {
      ...this.tax,
      ...this.form.getRawValue(),
      user_id: this.user.id
    };


    let request$: Observable<any> | null = null;

    if (roleNames.includes('ROLE_APPROVER')) {
      request$ = this.manageTaxHoService.rejectApproverTax(this.tax);

    } else if (roleNames.includes('ROLE_REVIEWER')) {
      request$ = this.routeControl === 'sent'
        ? this.manageTaxService.backToWaitingState(this.tax)
        : this.manageTaxService.rejectReviewerTax(this.tax);
    }
    if (request$ !== null) {


      // ✅ Execute API call
      if (request$ !== null) {
        request$.subscribe({
          next: (res: any) => {


            this.submitted = false;

            let message = '';

            if (res?.status === 'backtowait') {
              message = `${this.tax.reference_number} has been moved back to waiting state`;
            }

            else if (res?.status === 'alreadyapproved') {
              message = `${this.tax.reference_number} has already been approved, you can not back`;

            } else {
              // For rejectReviewerTax or rejectApproverTax responses
              message = `${this.tax.reference_number} successfully rejected`;
            }

            this.messageService.add({
              severity: 'success',
              detail: 'Message Details',
              summary: message,
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

    }

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
