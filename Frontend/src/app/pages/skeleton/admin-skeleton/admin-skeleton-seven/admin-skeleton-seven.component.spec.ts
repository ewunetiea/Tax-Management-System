import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSkeletonSevenComponent } from './admin-skeleton-seven.component';

describe('AdminSkeletonSevenComponent', () => {
  let component: AdminSkeletonSevenComponent;
  let fixture: ComponentFixture<AdminSkeletonSevenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSkeletonSevenComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSkeletonSevenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
