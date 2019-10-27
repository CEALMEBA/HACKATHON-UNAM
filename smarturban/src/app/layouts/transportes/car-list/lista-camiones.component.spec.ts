import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaCamionesComponent } from './lista-camiones.component';

describe('ListaCamionesComponent', () => {
  let component: ListaCamionesComponent;
  let fixture: ComponentFixture<ListaCamionesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListaCamionesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListaCamionesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
