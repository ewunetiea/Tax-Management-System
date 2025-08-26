import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSkeletonFourComponent } from './admin-skeleton-four.component';

describe('AdminSkeletonFourComponent', () => {
  let component: AdminSkeletonFourComponent;
  let fixture: ComponentFixture<AdminSkeletonFourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSkeletonFourComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSkeletonFourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
