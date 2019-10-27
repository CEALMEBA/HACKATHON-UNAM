// imports con los que va interactuar

import { MonitoringListComponent } from './monitoring-list/monitoring-list.component';
/*
import { AllTransportesComponent } from './add-car/all-transportes.component';
import { CarDetailComponent } from './car-details/car-details.component';*/
import { Routes } from '@angular/router';


export const MonitoringRoutes : Routes = [
    {
      path: 'monitoring',
      children: [

        {
          path: 'all-routes-buses',
          component: MonitoringListComponent
        },
      ]
    }
];