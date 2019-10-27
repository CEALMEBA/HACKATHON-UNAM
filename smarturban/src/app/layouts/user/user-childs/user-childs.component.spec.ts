import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UserChildsComponent } from './user-childs.component';

describe('UserChildsComponent', () => {
  let component: UserChildsComponent;
  let fixture: ComponentFixture<UserChildsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UserChildsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UserChildsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
