import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Skeleton } from 'primeng/skeleton';

@Component({
    standalone: true,
    selector: 'app-bar-and-line-chart-skeleton',
    imports: [CommonModule, Skeleton],
    template: ` <!-- title placeholder -->
        <p-skeleton width="6rem" height="1.5rem" styleClass="mb-4"></p-skeleton>

        <!-- legends  -->
        <div class="flex justify-center gap-4 mt-3 mb-2">
            <p-skeleton width="6rem" height="1rem"></p-skeleton>
            <p-skeleton width="6rem" height="1rem"></p-skeleton>
        </div>
        
        <!-- big rectangle to simulate the chart area -->
        <p-skeleton width="100%" styleClass="!h-96"></p-skeleton>
        <!-- x- axis in month  -->

        <div class="flex justify-between mt-2">
            <p-skeleton width="2rem" height="1rem"></p-skeleton>
            <p-skeleton width="2rem" height="1rem"></p-skeleton>
            <p-skeleton width="2rem" height="1rem"></p-skeleton>
            <p-skeleton width="2rem" height="1rem"></p-skeleton>
            <p-skeleton width="2rem" height="1rem"></p-skeleton>
            <p-skeleton width="2rem" height="1rem"></p-skeleton>
        </div>`
})
export class BarAndLineChartSkeleton {}
