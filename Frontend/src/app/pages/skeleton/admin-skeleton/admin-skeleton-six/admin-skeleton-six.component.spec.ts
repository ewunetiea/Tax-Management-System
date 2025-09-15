import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminSkeletonSixComponent } from './admin-skeleton-six.component';

describe('AdminSkeletonSixComponent', () => {
  let component: AdminSkeletonSixComponent;
  let fixture: ComponentFixture<AdminSkeletonSixComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminSkeletonSixComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminSkeletonSixComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
