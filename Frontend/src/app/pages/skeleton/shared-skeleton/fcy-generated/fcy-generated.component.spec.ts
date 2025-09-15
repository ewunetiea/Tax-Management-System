import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FcyGeneratedComponent } from './fcy-generated.component';

describe('FcyGeneratedComponent', () => {
  let component: FcyGeneratedComponent;
  let fixture: ComponentFixture<FcyGeneratedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FcyGeneratedComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FcyGeneratedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
