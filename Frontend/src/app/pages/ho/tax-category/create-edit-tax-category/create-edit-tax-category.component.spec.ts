import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditTaxCategoryComponent } from './create-edit-tax-category.component';

describe('CreateEditTaxCategoryComponent', () => {
  let component: CreateEditTaxCategoryComponent;
  let fixture: ComponentFixture<CreateEditTaxCategoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateEditTaxCategoryComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditTaxCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
