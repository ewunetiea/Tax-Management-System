import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RadSkeletonComponent } from './rad-skeleton.component';

describe('RadSkeletonComponent', () => {
  let component: RadSkeletonComponent;
  let fixture: ComponentFixture<RadSkeletonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RadSkeletonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RadSkeletonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
