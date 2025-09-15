import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecentActivitySkeletonComponent } from './recent-activity-skeleton.component';

describe('RecentActivitySkeletonComponent', () => {
  let component: RecentActivitySkeletonComponent;
  let fixture: ComponentFixture<RecentActivitySkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RecentActivitySkeletonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RecentActivitySkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
