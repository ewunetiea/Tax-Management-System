import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageTaxCategoryComponent } from './manage-tax-category.component';

describe('ManageTaxCategoryComponent', () => {
  let component: ManageTaxCategoryComponent;
  let fixture: ComponentFixture<ManageTaxCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageTaxCategoryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageTaxCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
