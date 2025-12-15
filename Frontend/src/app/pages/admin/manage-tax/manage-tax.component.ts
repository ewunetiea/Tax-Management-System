import { Component } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { PaginatorPayLoad } from 'app/models/admin/paginator-payload';
import { User } from 'app/models/admin/user';
import { Tax } from 'app/models/maker/tax';
import { TaxableSearchengineApproverComponent } from 'app/pages/approver/search-engine/taxable-searchengine-approver.component';
import { ManageTaxApproverService } from 'app/service/approver/manage-tax-ho-service';
import { FileDownloadService } from 'app/service/maker/file-download-service';
import { StorageService } from 'app/service/sharedService/storage.service';
import { ConfirmationService, MessageService } from 'primeng/api';
import { SharedUiModule } from 'shared-ui';

@Component({
  selector: 'app-manage-tax',
  imports: [TaxableSearchengineApproverComponent, SharedUiModule],
  templateUrl: './manage-tax.component.html',
  styleUrl: './manage-tax.component.scss'
})
export class ManageTaxComponent {
  statusRoute: string = '';
  sizes!: any[];
  selectedSize: any = 'normal';
  user: User = new User();
  paginatorPayLoad: PaginatorPayLoad = new PaginatorPayLoad();
  taxes: Tax[] = [];
  selectedTaxes: Tax[] = [];
  loading = true;
   fetching = false;

  constructor(
    private manageTaxHoService: ManageTaxApproverService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private storageService: StorageService,
    private router: Router,
    private fileDownloadService: FileDownloadService,
    private sanitizer: DomSanitizer,
  ) { }

  ngOnInit(): void {
    this.user = this.storageService.getUser();
    this.paginatorPayLoad.branch_id = this.user.branch?.id;
    this.paginatorPayLoad.user_id = this.user.id;

    this.sizes = [
      { name: 'Small', value: 'small' },
      { name: 'Normal', value: 'normal' },
      { name: 'Large', value: 'large' }
    ];


    this.setStatusRoute();
    this.router.events.subscribe(() => {
      this.setStatusRoute();
    });
  }

   setStatusRoute() {
    const currentRoute = this.router.url.toLowerCase();
    if (currentRoute.includes('approved')) this.statusRoute = 'approved';
    else if (currentRoute.includes('rejected')) this.statusRoute = 'rejected';
    else this.statusRoute = 'pending';

    // 🧹 reset when route changes
    this.taxes = [];
    this.fetching = false;
  }

   onDataGenerated(event: { data: Tax[]; fetching: boolean }): void {
    this.taxes = event.data;
    this.fetching = event.fetching;
    this.loading = false;
  }


}
