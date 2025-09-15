import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakerApproverThreeComponent } from './maker-approver-three.component';

describe('MakerApproverThreeComponent', () => {
  let component: MakerApproverThreeComponent;
  let fixture: ComponentFixture<MakerApproverThreeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MakerApproverThreeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MakerApproverThreeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
