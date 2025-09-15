import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakerApproverTwoComponent } from './maker-approver-two.component';

describe('MakerApproverTwoComponent', () => {
  let component: MakerApproverTwoComponent;
  let fixture: ComponentFixture<MakerApproverTwoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MakerApproverTwoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MakerApproverTwoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
