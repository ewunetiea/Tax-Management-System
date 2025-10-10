import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTaxHoComponent } from './manage-tax-ho.component';

describe('ManageTaxHoComponent', () => {
  let component: ManageTaxHoComponent;
  let fixture: ComponentFixture<ManageTaxHoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageTaxHoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageTaxHoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
