import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanAllowedAmountSkeletonComponent } from './loan-allowed-amount-skeleton.component';

describe('LoanAllowedAmountSkeletonComponent', () => {
  let component: LoanAllowedAmountSkeletonComponent;
  let fixture: ComponentFixture<LoanAllowedAmountSkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoanAllowedAmountSkeletonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoanAllowedAmountSkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
