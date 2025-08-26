import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuditorHeaderComponent } from './auditor-header.component';

describe('AuditorHeaderComponent', () => {
  let component: AuditorHeaderComponent;
  let fixture: ComponentFixture<AuditorHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuditorHeaderComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuditorHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
