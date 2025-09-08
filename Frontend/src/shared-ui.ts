import { NgModule } from '@angular/core';
import { CommonModule, NgIf } from '@angular/common';
import { FormsModule } from '@angular/forms';

// PrimeNG UI modules
import { TextareaModule } from 'primeng/textarea';
import { DialogModule } from 'primeng/dialog';
import { ToastModule } from 'primeng/toast';
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
import { MenubarModule } from 'primeng/menubar';
import { BadgeModule } from 'primeng/badge';
import { AvatarModule } from 'primeng/avatar';
import { StepperModule } from 'primeng/stepper';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ListboxModule } from 'primeng/listbox';
import { AccordionModule } from 'primeng/accordion';
import { PasswordModule } from 'primeng/password';
import { KnobModule } from 'primeng/knob';
import { DividerModule } from 'primeng/divider';
import { ChartModule } from 'primeng/chart';
import { TimeagoModule } from 'ngx-timeago';
import { RadioButtonModule } from 'primeng/radiobutton';
import { RouterModule } from '@angular/router';
import { RippleModule } from 'primeng/ripple';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        ToastModule,

        // PrimeNG
        TextareaModule,
        DialogModule,
        ConfirmDialogModule,
        ToastModule,
        ListboxModule,
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
        AccordionModule,
        BreadcrumbModule,
        TabsModule,
        TabViewModule,
        InputTextModule,
        IconFieldModule,
        InputIconModule,
        SelectButtonModule,
        StyleClassModule,
        MenubarModule,
        BadgeModule,
        AvatarModule,
        StepperModule,
        PasswordModule,
        KnobModule,
        DividerModule,
        TimeagoModule,
        ChartModule,
        RadioButtonModule,
        RouterModule, RippleModule, NgIf
    ],
    exports: [
        CommonModule,
        FormsModule,
        ToastModule,

        // PrimeNG
        TextareaModule,
        DialogModule,
        ConfirmDialogModule,
        ToastModule,
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
        ListboxModule,
        BreadcrumbModule,
        AccordionModule,
        TabsModule,
        TabViewModule,
        InputTextModule,
        IconFieldModule,
        InputIconModule,
        SelectButtonModule,
        StyleClassModule,
        MenubarModule,
        BadgeModule,
        AvatarModule,
        StepperModule,
        PasswordModule,
        KnobModule,
        DividerModule,
        ChartModule,
        TimeagoModule,
        RadioButtonModule,

        RouterModule, RippleModule, NgIf
    ]
})
export class SharedUiModule { }
