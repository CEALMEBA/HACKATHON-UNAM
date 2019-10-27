// imports con los que va interactuar

import { RoutesListComponent } from './routes-list/routes-list.component';
import { RouteMonitoringComponent } from './route-monitoring/route-monitoring.component';
/*
import { AllTransportesComponent } from './add-car/all-transportes.component';
import { CarDetailComponent } from './car-details/car-details.component';*/
import { Routes } from '@angular/router';
import { RoutesAddComponent } from './routes-add/routes-add.component';
import { RoutesDetailsComponent } from './routes-details/routes-details.component';


export const CarRoutes : Routes = [
    {
      path: 'routes',
      children: [
        {
          path: 'monitoring/:id',
          component: RouteMonitoringComponent
        },

        {
          path: 'all-routes',
          component: RoutesListComponent
        },
       
        {
          path: 'add-routes',
          component: RoutesAddComponent
        },
        
        {
  				path: 'route/:id',
  				component: RoutesDetailsComponent
  			},

                ]
    }
];
