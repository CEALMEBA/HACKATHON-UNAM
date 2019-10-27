import { NgModule, NO_ERRORS_SCHEMA, APP_INITIALIZER } from '@angular/core';

import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";

// configuration and services
import { MonitoringRoutes } from './monitoring.routing';
import { SharedModule } from "../../shared/shared.module";
import { MonitoringComponent } from './monitoring.component';

//components
//import { RoutesAddComponent } from './routes-add/routes-add.component';
//import { RoutesDetailsComponent } from './routes-details/routes-details.component';


import { AgmCoreModule } from '@agm/core';
import { MonitoringListComponent } from './monitoring-list/monitoring-list.component';

/*
import { AllTransportesComponent } from './add-car/all-transportes.component';
import { RoutesListComponent } from './car-list/lista-camiones.component';
import { CarDetailComponent } from './car-details/car-details.component';
*/


@NgModule({
  imports: [ CommonModule, RouterModule.forChild(MonitoringRoutes), SharedModule,
    AgmCoreModule.forRoot({
      //apiKey: 'AIzaSyD9xXq1L6UtsTBi8miLM0FJU2erOkwW_0I'
      apiKey: 'AIzaSyCISYKY_t-jlJGxYMe3nBOMTqwRrhKPxMk',
        libraries: ["places"]
       })
  ],
  declarations: [
    MonitoringListComponent
              ],
  providers: [

  ],
  schemas:[ NO_ERRORS_SCHEMA ]
})
export class MonitoringModule { }