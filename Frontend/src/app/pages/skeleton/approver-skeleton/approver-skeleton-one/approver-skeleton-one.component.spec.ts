import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApproverSkeletonOneComponent } from './approver-skeleton-one.component';

describe('ApproverSkeletonOneComponent', () => {
  let component: ApproverSkeletonOneComponent;
  let fixture: ComponentFixture<ApproverSkeletonOneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApproverSkeletonOneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApproverSkeletonOneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
