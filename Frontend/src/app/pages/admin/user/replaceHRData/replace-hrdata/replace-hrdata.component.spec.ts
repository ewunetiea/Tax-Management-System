import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReplaceHRDataComponent } from './replace-hrdata.component';

describe('ReplaceHRDataComponent', () => {
  let component: ReplaceHRDataComponent;
  let fixture: ComponentFixture<ReplaceHRDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReplaceHRDataComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReplaceHRDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
