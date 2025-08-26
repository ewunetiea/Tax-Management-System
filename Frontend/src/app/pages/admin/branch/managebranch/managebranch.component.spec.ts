import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagebranchComponent } from './managebranch.component';

describe('ManagebranchComponent', () => {
  let component: ManagebranchComponent;
  let fixture: ComponentFixture<ManagebranchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManagebranchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManagebranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
