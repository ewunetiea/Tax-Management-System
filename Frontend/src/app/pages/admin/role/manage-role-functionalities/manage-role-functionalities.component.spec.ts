import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageRoleFunctionalitiesComponent } from './manage-role-functionalities.component';

describe('ManageRoleFunctionalitiesComponent', () => {
  let component: ManageRoleFunctionalitiesComponent;
  let fixture: ComponentFixture<ManageRoleFunctionalitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageRoleFunctionalitiesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageRoleFunctionalitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
