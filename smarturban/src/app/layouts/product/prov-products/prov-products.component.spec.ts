import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProvProductsComponent } from './prov-products.component';

describe('ProvProductsComponent', () => {
  let component: ProvProductsComponent;
  let fixture: ComponentFixture<ProvProductsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProvProductsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProvProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
