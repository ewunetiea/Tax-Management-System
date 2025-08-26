import { Component } from '@angular/core';

@Component({
    standalone: true,
    selector: 'app-footer',
    template: `<div class="layout-footer">
        Tax Management System
        <a href="https://primeng.org" target="_blank" rel="noopener noreferrer" class="text-primary font-bold hover:underline">© Copyright {{currentYear}} Awash Bank. All Rights Reserved</a>
    </div>`
})
export class AppFooter {
     currentYear: number = new Date().getFullYear();
}
