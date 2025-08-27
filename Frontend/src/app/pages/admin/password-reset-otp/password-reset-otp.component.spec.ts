import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PasswordResetOtpComponent } from './password-reset-otp.component';

describe('PasswordResetOtpComponent', () => {
  let component: PasswordResetOtpComponent;
  let fixture: ComponentFixture<PasswordResetOtpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PasswordResetOtpComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PasswordResetOtpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
