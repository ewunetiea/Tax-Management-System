import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakerSkeletonTwoComponent } from './maker-skeleton-two.component';

describe('MakerSkeletonTwoComponent', () => {
  let component: MakerSkeletonTwoComponent;
  let fixture: ComponentFixture<MakerSkeletonTwoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MakerSkeletonTwoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MakerSkeletonTwoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
