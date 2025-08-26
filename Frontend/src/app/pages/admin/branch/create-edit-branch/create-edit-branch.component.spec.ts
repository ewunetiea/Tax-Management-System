import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditBranchComponent } from './create-edit-branch.component';

describe('CreateEditBranchComponent', () => {
  let component: CreateEditBranchComponent;
  let fixture: ComponentFixture<CreateEditBranchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CreateEditBranchComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditBranchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
