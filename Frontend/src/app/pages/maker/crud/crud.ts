import { Component, OnInit, ViewChild } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table, TableModule } from 'primeng/table';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { RippleModule } from 'primeng/ripple';
import { ToastModule } from 'primeng/toast';
import { ToolbarModule } from 'primeng/toolbar';
import { RatingModule } from 'primeng/rating';
import { InputTextModule } from 'primeng/inputtext';
import { TextareaModule } from 'primeng/textarea';
import { SelectModule } from 'primeng/select';
import { RadioButtonModule } from 'primeng/radiobutton';
import { InputNumberModule } from 'primeng/inputnumber';
import { DialogModule } from 'primeng/dialog';
import { TagModule } from 'primeng/tag';
import { InputIconModule } from 'primeng/inputicon';
import { IconFieldModule } from 'primeng/iconfield';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { HttpErrorResponse } from '@angular/common/http';
import { FileUpload } from 'primeng/fileupload';
import { Product, ProductService } from '../../../service/product.service';

interface Column {
    field: string;
    header: string;
    customExportHeader?: string;
}

interface ExportColumn {
    title: string;
    dataKey: string;
}

@Component({
    selector: 'app-crud',
    standalone: true,
    templateUrl: './crud.component.html',
    providers: [MessageService, ProductService, ConfirmationService],
    imports: [
        CommonModule,
        TableModule,
        FormsModule,
        ButtonModule,
        RippleModule,
        ToastModule,
        ToolbarModule,
        RatingModule,
        InputTextModule,
        TextareaModule,
        SelectModule,
        RadioButtonModule,
        InputNumberModule,
        DialogModule,
        TagModule,
        InputIconModule,
        IconFieldModule,
        ConfirmDialogModule,

        FileUpload,
    ]
})
export class Crud implements OnInit {
    productDialog: boolean = false;
    products: Product[] = [];
    product!: Product;
    selectedProducts!: Product[] | null;
    submitted: boolean = false;
    statuses!: any[];
    @ViewChild('dt') dt!: Table;
    exportColumns!: ExportColumn[];
    cols!: Column[];
    uploadedFiles: any[] = [];

    constructor(
        private productService: ProductService,
        private messageService: MessageService,
        private confirmationService: ConfirmationService
    ) { }

    ngOnInit() {
        this.loadProducts();
    }
    loadProducts() {
        this.productService.getProductsFromDb().subscribe(
            (response) => {
                if (Array.isArray(response)) {
                    this.products = response.map(product => ({
                        ...product,
                        imageSrc: 'data:image/png;base64,' + product.image
                    }));
                }
            },
            (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: 'error',
                    summary:
                        error.status === 401
                            ? 'You are not permitted to perform this action!'
                            : 'Something went wrong while fetching products!',
                    detail: '',
                });
            }
        );
    }



    exportCSV() {
        this.dt.exportCSV();
    }

    onGlobalFilter(table: Table, event: Event) {
        table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
    }

    openNew() {
        this.product = {};
        this.submitted = false;
        this.productDialog = true;
    }

    editProduct(product: Product) {
        this.product = { ...product };
        this.productDialog = true;
    }

    deleteSelectedProducts() {
        this.confirmationService.confirm({
            message: 'Are you sure you want to delete the selected products?',
            header: 'Confirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {



                this.products = this.products.filter(
                    (val) => !this.selectedProducts?.includes(val)
                );
                this.selectedProducts = null;
                this.messageService.add({
                    severity: 'success',
                    summary: 'Successful',
                    detail: 'Products Deleted',
                    life: 3000
                });
            }
        });
    }


    hideDialog() {
        this.productDialog = false;
        this.submitted = false;
    }

  deleteProduct(product: Product) {
    this.confirmationService.confirm({
        message: `Are you sure you want to delete ${product.name}?`,
        header: 'Confirm',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
            this.productService.deleteProduct(product.id!).subscribe({
                next: () => {
                    this.products = this.products.filter(p => p.id !== product.id);
                    this.messageService.add({
                        severity: 'success',
                        summary: 'Successful',
                        detail: 'Product Deleted',
                        life: 3000
                    });
                    this.product = {};
                },
                error: (err: HttpErrorResponse) => {
                    this.messageService.add({
                        severity: 'error',
                        summary: err.status === 401
                            ? 'You are not permitted to perform this action!'
                            : 'Something went wrong while deleting the product!',
                        detail: '',
                    });
                }
            });
        }
    });
}


    findIndexById(id: any): number {
        return this.products.findIndex((p) => p.id === id);
    }


    createId(): string {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        return Array.from({ length: 5 }, () => chars.charAt(Math.floor(Math.random() * chars.length))).join('');
    }

    getSeverity(status: string) {
        switch (status) {
            case 'INSTOCK': return 'success';
            case 'LOWSTOCK': return 'warn';
            case 'OUTOFSTOCK': return 'danger';
            default: return 'info';
        }
    }



    // saveProduct() {

    //     this.submitted = true;
    //     const _products = this.products;

    //     this.productService.createProduct(this.product).subscribe({
    //         next: (productResponse) => {
    //             if (this.product.id) {

    //                 _products[this.findIndexById(this.product.id)] = productResponse;
    //                 this.products = [..._products];
    //                 this.messageService.add({
    //                     severity: "success",
    //                     summary: "Successful",
    //                     detail: "Audit Updated",
    //                     life: 3000,
    //                 });
    //             } else {

    //                 this.products = [..._products, productResponse];

    //                 this.messageService.add({
    //                     severity: "success",
    //                     summary: "Successful",
    //                     detail: "Audit Created",
    //                     life: 3000,
    //                 });



    //                 this.productDialog = false;
    //                 this.product = new Product();
    //             }



    //         },
    //         error: (error: HttpErrorResponse) => {
    //             this.messageService.add({
    //                 severity: "error",
    //                 summary: "Error",
    //                 detail:
    //                     error.status == 401
    //                         ? "You are not permitted to perform this action!"
    //                         : "Something went wrong while either creating or updating finding!",
    //                 life: 3000,
    //             });
    //         },
    //     });

    // }


    saveProduct() {
        this.submitted = true;
        const _products = this.products;

        this.productService.createProduct(this.product).subscribe({
            next: (productResponse) => {

                // Add imageSrc field
                const updatedProduct = {
                    ...productResponse,
                    imageSrc: 'data:image/png;base64,' + productResponse.image
                };

                if (this.product.id) {
                    // Update existing product
                    _products[this.findIndexById(this.product.id)] = updatedProduct;
                    this.products = [..._products];
                    this.messageService.add({
                        severity: "success",
                        summary: "Successful",
                        detail: "Audit Updated",
                        life: 3000,
                    });
                    this.productDialog = false;

                } else {
                    // Add new product
                    this.products = [..._products, updatedProduct];
                    this.messageService.add({
                        severity: "success",
                        summary: "Successful",
                        detail: "Audit Created",
                        life: 3000,
                    });
                    this.productDialog = false;
this.product = {} as Product;  // empty product
                }
            },
            error: (error: HttpErrorResponse) => {
                this.messageService.add({
                    severity: "error",
                    summary: "Error",
                    detail:
                        error.status == 401
                            ? "You are not permitted to perform this action!"
                            : "Something went wrong while either creating or updating finding!",
                    life: 3000,
                });
            },
        });
    }


    onFileSelect(event: any): void {
        const file: File = event.files[0];

        const reader = new FileReader();
        reader.onload = () => {
            const arrayBuffer = reader.result as ArrayBuffer;
            const byteArray = new Uint8Array(arrayBuffer);
            this.product.image = Array.from(byteArray) as any; // 👈 Convert to number[]

            this.messageService.add({
                severity: 'success',
                summary: 'Upload Successful',
                detail: 'Image uploaded as byte array!'
            });


        };
        reader.readAsArrayBuffer(file);
    }





}
