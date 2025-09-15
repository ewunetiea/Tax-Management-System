import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSkeletonOneComponent } from './admin-skeleton-one.component';

describe('AdminSkeletonOneComponent', () => {
  let component: AdminSkeletonOneComponent;
  let fixture: ComponentFixture<AdminSkeletonOneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminSkeletonOneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSkeletonOneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
