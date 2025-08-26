import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MenuheadersComponent } from './menuheaders.component';

describe('MenuheadersComponent', () => {
  let component: MenuheadersComponent;
  let fixture: ComponentFixture<MenuheadersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MenuheadersComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MenuheadersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
