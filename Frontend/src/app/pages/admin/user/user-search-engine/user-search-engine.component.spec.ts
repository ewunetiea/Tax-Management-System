import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSearchEngineComponent } from './user-search-engine.component';

describe('UserSearchEngineComponent', () => {
  let component: UserSearchEngineComponent;
  let fixture: ComponentFixture<UserSearchEngineComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSearchEngineComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserSearchEngineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
