import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Skeleton } from "primeng/skeleton";

@Component({
    standalone: true,
    selector: 'app-pie-dougnut-polar-skeleton',
    imports: [CommonModule, Skeleton],
template: `<div class="flex justify-center gap-3 mb-1" >
               <!-- <app-pie-dougnut-polar-skeleton></app-pie-dougnut-polar-skeleton> -->
                <p-skeleton width="1rem" height="1rem" shape="circle"></p-skeleton>
                <p-skeleton width="1rem" height="1rem" shape="circle"></p-skeleton>
                <p-skeleton width="1rem" height="1rem" shape="circle"></p-skeleton>
            </div>`
})
export class PieDougnutPolarSkeleton {}

