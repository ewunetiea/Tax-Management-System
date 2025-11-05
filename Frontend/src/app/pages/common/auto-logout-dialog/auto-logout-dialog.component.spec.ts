import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AutoLogoutDialogComponent } from './auto-logout-dialog.component';

describe('AutoLogoutDialogComponent', () => {
  let component: AutoLogoutDialogComponent;
  let fixture: ComponentFixture<AutoLogoutDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AutoLogoutDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AutoLogoutDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
