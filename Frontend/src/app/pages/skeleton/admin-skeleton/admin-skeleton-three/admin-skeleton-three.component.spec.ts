import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSkeletonThreeComponent } from './admin-skeleton-three.component';

describe('AdminSkeletonThreeComponent', () => {
  let component: AdminSkeletonThreeComponent;
  let fixture: ComponentFixture<AdminSkeletonThreeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSkeletonThreeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSkeletonThreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
