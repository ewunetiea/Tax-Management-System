import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditFunctionalitiesComponent } from './create-edit-functionalities.component';

describe('CreateEditFunctionalitiesComponent', () => {
  let component: CreateEditFunctionalitiesComponent;
  let fixture: ComponentFixture<CreateEditFunctionalitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateEditFunctionalitiesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditFunctionalitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
