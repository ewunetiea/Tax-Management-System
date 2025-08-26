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

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
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
  ],
  exports: [
    CommonModule,
    FormsModule,
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
  ]
})
export class SharedUiModule {}
