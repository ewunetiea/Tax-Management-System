import { Component, OnInit, OnDestroy } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter, Subject, takeUntil } from 'rxjs';
import { Tax } from 'app/models/maker/tax';
import { User } from 'app/models/admin/user';
import { PaginatorPayLoad } from 'app/models/admin/paginator-payload';
import { SharedUiModule } from 'shared-ui';
import { SearchEngineComponent } from '../search-engine/search-engine.component';

@Component({
  selector: 'app-manage-tax',
  standalone: true,
  imports: [SearchEngineComponent, SharedUiModule],
  templateUrl: './manage-tax.component.html',
  styleUrls: ['./manage-tax.component.scss']
})
export class ManageTaxComponent implements OnInit, OnDestroy {
  statusRoute: string = '';
  taxes: Tax[] = [];
  fetching = false;
  loading = true;
  sizes!: any[];
  selectedSize: any = 'normal';
  user: User = new User();
  paginatorPayLoad: PaginatorPayLoad = new PaginatorPayLoad();
  private destroy$ = new Subject<void>();
  constructor(private router: Router) {}

  ngOnInit(): void {
    this.sizes = [
      { name: 'Small', value: 'small' },
      { name: 'Normal', value: 'normal' },
      { name: 'Large', value: 'large' }
    ];

    this.setStatusRoute();
  }

 setStatusRoute() {
    const currentRoute = this.router.url.toLowerCase();
    if (currentRoute.includes('general')) this.statusRoute = 'general';
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

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
