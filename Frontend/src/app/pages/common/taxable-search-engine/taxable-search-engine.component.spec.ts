import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaxableSearchEngineComponent } from './taxable-search-engine.component';

describe('TaxableSearchEngineComponent', () => {
  let component: TaxableSearchEngineComponent;
  let fixture: ComponentFixture<TaxableSearchEngineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaxableSearchEngineComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TaxableSearchEngineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
