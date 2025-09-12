import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUserSecurityComponent } from './add-user-security.component';

describe('AddUserSecurityComponent', () => {
  let component: AddUserSecurityComponent;
  let fixture: ComponentFixture<AddUserSecurityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddUserSecurityComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddUserSecurityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
