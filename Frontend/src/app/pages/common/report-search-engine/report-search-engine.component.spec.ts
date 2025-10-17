import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportSearchEngineComponent } from './report-search-engine.component';

describe('ReportSearchEngineComponent', () => {
  let component: ReportSearchEngineComponent;
  let fixture: ComponentFixture<ReportSearchEngineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReportSearchEngineComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportSearchEngineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
