import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageRegionComponent } from './manage-region.component';

describe('ManageRegionComponent', () => {
  let component: ManageRegionComponent;
  let fixture: ComponentFixture<ManageRegionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageRegionComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageRegionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
