import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageJobPositionComponent } from './manage-job-position.component';

describe('ManageJobPositionComponent', () => {
  let component: ManageJobPositionComponent;
  let fixture: ComponentFixture<ManageJobPositionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageJobPositionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageJobPositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
