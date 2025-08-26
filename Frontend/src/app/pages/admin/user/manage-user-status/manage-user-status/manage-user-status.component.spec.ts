import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageUserStatusComponent } from './manage-user-status.component';

describe('ManageUserStatusComponent', () => {
  let component: ManageUserStatusComponent;
  let fixture: ComponentFixture<ManageUserStatusComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageUserStatusComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageUserStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
