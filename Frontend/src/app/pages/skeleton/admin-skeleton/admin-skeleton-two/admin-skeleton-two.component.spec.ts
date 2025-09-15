import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSkeletonTwoComponent } from './admin-skeleton-two.component';

describe('AdminSkeletonTwoComponent', () => {
  let component: AdminSkeletonTwoComponent;
  let fixture: ComponentFixture<AdminSkeletonTwoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminSkeletonTwoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSkeletonTwoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
