import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// PrimeNG UI modules
import { TextareaModule } from 'primeng/textarea';
import { DialogModule } from 'primeng/dialog';
import { CardModule } from 'primeng/card';
import { TooltipModule } from 'primeng/tooltip';
import { TableModule } from 'primeng/table';
import { MultiSelectModule } from 'primeng/multiselect';
import { ButtonModule } from 'primeng/button';
import { TagModule } from 'primeng/tag';
import { CheckboxModule } from 'primeng/checkbox';
import { DropdownModule } from 'primeng/dropdown';
import { PaginatorModule } from 'primeng/paginator';
import { ToolbarModule } from 'primeng/toolbar';
import { BreadcrumbModule } from 'primeng/breadcrumb';
import { TabsModule } from 'primeng/tabs';
import { TabViewModule } from 'primeng/tabview';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { SelectButtonModule } from 'primeng/selectbutton';
import { StyleClassModule } from 'primeng/styleclass';
import { Menubar } from 'primeng/menubar';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { StepperModule } from 'primeng/stepper';
import { ChartModule } from 'primeng/chart';
import { ToastModule } from 'primeng/toast';
import { TimeagoModule } from 'ngx-timeago';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ToastModule,

    // PrimeNG
    TextareaModule,
    DialogModule,
    CardModule,
    TooltipModule,
    TableModule,
    MultiSelectModule,
    ButtonModule,
    TagModule,
    CheckboxModule,
    DropdownModule,
    PaginatorModule,
    ToolbarModule,
    BreadcrumbModule,
    TabsModule,
    TabViewModule,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    SelectButtonModule,
    StyleClassModule,
    Menubar,
    BadgeModule,
    AvatarModule,
    StepperModule,
    TimeagoModule,
    ChartModule
  ],
  exports: [
    CommonModule,
    FormsModule,
ToastModule,
    // PrimeNG
    TextareaModule,
    DialogModule,
    CardModule,
    TooltipModule,
    TableModule,
    MultiSelectModule,
    ButtonModule,
    TagModule,
    CheckboxModule,
    DropdownModule,
    PaginatorModule,
    ToolbarModule,
    BreadcrumbModule,
    TabsModule,
    TabViewModule,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    SelectButtonModule,
    StyleClassModule,
    Menubar,
    BadgeModule,
    AvatarModule,
    StepperModule,
    ChartModule,
    TimeagoModule
  ]
})
export class SharedUiModule {}
