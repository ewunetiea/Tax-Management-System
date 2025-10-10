import { Component } from '@angular/core';

@Component({
  standalone: true,
  selector: 'app-footer',
  template: `
    <footer class="layout-footer flex flex-col md:flex-row items-center justify-between gap-2 text-center md:text-left p-4 border-t bg-gray-50 text-gray-700">
      <div class="flex items-center gap-2">
        <i class="pi pi-bank text-primary text-lg"></i>
        <span class="font-semibold">Tax Management System</span>
      </div>

      <div>
        <span>© {{ currentYear }} Awash Bank. All Rights Reserved!</span>
      </div>
    </footer>
  `,
})
export class AppFooter {
  currentYear: number = new Date().getFullYear();
}
