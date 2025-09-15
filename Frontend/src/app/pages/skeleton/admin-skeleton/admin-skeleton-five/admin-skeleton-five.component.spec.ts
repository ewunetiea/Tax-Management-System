import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSkeletonFiveComponent } from './admin-skeleton-five.component';

describe('AdminSkeletonFiveComponent', () => {
  let component: AdminSkeletonFiveComponent;
  let fixture: ComponentFixture<AdminSkeletonFiveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminSkeletonFiveComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSkeletonFiveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
