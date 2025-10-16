import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectCheckerApproverComponent } from './reject-checker-approver.component';

describe('RejectCheckerApproverComponent', () => {
  let component: RejectCheckerApproverComponent;
  let fixture: ComponentFixture<RejectCheckerApproverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RejectCheckerApproverComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RejectCheckerApproverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
