import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakerApproverOneComponent } from './maker-approver-one.component';

describe('MakerApproverOneComponent', () => {
  let component: MakerApproverOneComponent;
  let fixture: ComponentFixture<MakerApproverOneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MakerApproverOneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MakerApproverOneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
