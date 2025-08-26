import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingConfigurationComponent } from './setting-configuration.component';

describe('SettingConfigurationComponent', () => {
  let component: SettingConfigurationComponent;
  let fixture: ComponentFixture<SettingConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SettingConfigurationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SettingConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
