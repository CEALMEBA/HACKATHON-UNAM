import {
    async,
    ComponentFixture,
    TestBed
} from '@angular/core/testing';

import {
    AllTransportesComponent
} from './all-transportes.component';

describe('AllTransportesComponent', () => {
    let component: AllTransportesComponent;
    let fixture: ComponentFixture < AllTransportesComponent > ;

    beforeEach(async (() => {
        TestBed.configureTestingModule({
                declarations: [AllTransportesComponent]
            })
            .compileComponents();
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AllTransportesComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
