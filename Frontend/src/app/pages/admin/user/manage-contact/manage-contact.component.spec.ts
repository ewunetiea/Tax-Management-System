import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManageContactComponent } from './manage-contact.component';

describe('ManageContactComponent', () => {
  let component: ManageContactComponent;
  let fixture: ComponentFixture<ManageContactComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ManageContactComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ManageContactComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
