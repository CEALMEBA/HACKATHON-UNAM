// imports con los que va interactuar

import { ListaCamionesComponent } from './car-list/lista-camiones.component';
import { AllTransportesComponent } from './add-car/all-transportes.component';
import { CarDetailComponent } from './car-details/car-details.component';
import { CalendarComponent } from './calendar/calendar.component';
import { Routes } from '@angular/router';



export const TransporteRoutes : Routes = [
    {
      path: 'buses',
      children: [

        {
          path: 'all-buses',
          component: ListaCamionesComponent
        },
        {
          path: 'add-bus',
          component: AllTransportesComponent
        },
        {
  				path: 'cars/:id',
  				component: CarDetailComponent
        },
        {
  				path: 'calendar/:id',
  				component: CalendarComponent
  			},

                ]
    }
];
