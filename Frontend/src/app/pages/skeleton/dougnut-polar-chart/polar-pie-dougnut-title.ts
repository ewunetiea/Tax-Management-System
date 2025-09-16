import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Skeleton } from "primeng/skeleton";

@Component({
    standalone: true,
    selector: 'app-pie-dougnut-polar-description-skeleton',
    imports: [CommonModule, Skeleton],
    template: `<div>
                 <p-skeleton width="6rem" height="1.5rem" shape="text"></p-skeleton>

            </div>`
})
export class PieDougnutPolarSkeletonDescription { }

