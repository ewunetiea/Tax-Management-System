import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HoSkeletonComponent } from './ho-skeleton.component';

describe('HoSkeletonComponent', () => {
  let component: HoSkeletonComponent;
  let fixture: ComponentFixture<HoSkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HoSkeletonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HoSkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
