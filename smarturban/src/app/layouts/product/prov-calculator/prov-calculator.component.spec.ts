import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvCalculatorComponent } from './prov-calculator.component';

describe('ProvCalculatorComponent', () => {
  let component: ProvCalculatorComponent;
  let fixture: ComponentFixture<ProvCalculatorComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvCalculatorComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvCalculatorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
