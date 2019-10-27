import { NgModule, NO_ERRORS_SCHEMA, APP_INITIALIZER } from '@angular/core';

import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";

// configuration and services
import { TransporteRoutes } from './transportes.routing';
import { SharedModule } from "../../shared/shared.module";

//components
import { AllTransportesComponent } from './add-car/all-transportes.component';
import { ListaCamionesComponent } from './car-list/lista-camiones.component';
import { CarDetailComponent } from './car-details/car-details.component';
import { CalendarComponent } from './calendar/calendar.component';



@NgModule({
  imports: [ CommonModule, RouterModule.forChild(TransporteRoutes), SharedModule],
  declarations: [AllTransportesComponent,
                 ListaCamionesComponent, CarDetailComponent, CalendarComponent
               ],
  providers: [],
  schemas:[ NO_ERRORS_SCHEMA ]
})
export class TransportesModule { }
