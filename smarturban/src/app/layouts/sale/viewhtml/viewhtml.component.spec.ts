import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewhtmlComponent } from './viewhtml.component';

describe('ViewhtmlComponent', () => {
  let component: ViewhtmlComponent;
  let fixture: ComponentFixture<ViewhtmlComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ViewhtmlComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ViewhtmlComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
