import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakerApproverSkeletonComponent } from './maker-approver-skeleton.component';

describe('MakerApproverSkeletonComponent', () => {
  let component: MakerApproverSkeletonComponent;
  let fixture: ComponentFixture<MakerApproverSkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MakerApproverSkeletonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MakerApproverSkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
