import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Skeleton } from "primeng/skeleton";

@Component({
    standalone: true,
    selector: 'app-card-skeleton',
    imports: [CommonModule, Skeleton],
template: `<div class="card mb-0">
            <div class="flex justify-between mb-4">
                <div>
                    <!-- Title skeleton -->
                    <p-skeleton width="6rem" height="2rem" styleClass="mb-2"></p-skeleton>

                    <!-- Number skeleton -->
                    <p-skeleton width="4rem" height="1.5rem"></p-skeleton>
                </div>

                <!-- Icon skeleton -->
                <p-skeleton shape="circle" size="3rem"></p-skeleton>
            </div>

            <!-- Footer skeleton -->
            <p-skeleton width="10rem" height="1rem" styleClass="inline-block"></p-skeleton>
        </div>`
})
export class CardSkeleton {}

