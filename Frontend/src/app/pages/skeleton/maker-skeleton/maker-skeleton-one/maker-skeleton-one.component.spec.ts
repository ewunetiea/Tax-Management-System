import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MakerSkeletonOneComponent } from './maker-skeleton-one.component';

describe('MakerSkeletonOneComponent', () => {
  let component: MakerSkeletonOneComponent;
  let fixture: ComponentFixture<MakerSkeletonOneComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MakerSkeletonOneComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MakerSkeletonOneComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
