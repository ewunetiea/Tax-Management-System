import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoleFunctionalitiesComponent } from './role-functionalities.component';

describe('RoleFunctionalitiesComponent', () => {
  let component: RoleFunctionalitiesComponent;
  let fixture: ComponentFixture<RoleFunctionalitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RoleFunctionalitiesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoleFunctionalitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
