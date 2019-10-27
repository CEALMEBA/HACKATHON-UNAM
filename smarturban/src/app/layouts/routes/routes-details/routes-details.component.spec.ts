import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RoutesDetailsComponent } from './routes-details.component';

describe('RoutesDetailsComponent', () => {
  let component: RoutesDetailsComponent;
  let fixture: ComponentFixture<RoutesDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RoutesDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RoutesDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
