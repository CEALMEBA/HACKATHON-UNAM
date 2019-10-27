import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RouteMonitoringComponent } from './route-monitoring.component';

describe('RouteMonitoringComponent', () => {
  let component: RouteMonitoringComponent;
  let fixture: ComponentFixture<RouteMonitoringComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RouteMonitoringComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RouteMonitoringComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
