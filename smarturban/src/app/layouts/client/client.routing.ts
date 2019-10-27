//import { CartProductsComponent } from './cart-products/cart-products.component';
//import { FavouriteProductsComponent } from './favourite-products/favourite-products.component';
import { ClientListComponent } from './client-list/client-list.component';
import { Routes } from '@angular/router';
import { IndexComponent } from '../../index/index.component';
//import { ProductDetailComponent } from './product-detail/product-detail.component';
import { AddClientComponent } from './add-client/add-client.component';

/*import { AllTransportesComponent } from '../transportes/all-transportes/all-transportes.component';*/

export const ClientRoutes: Routes = [
	{
		path: 'clients',
		children: [
			{
				path: '',
				component: IndexComponent
			},
			{
				path: 'all-clients',
				component: ClientListComponent
			},
			{
				path: 'addclient',
				component: AddClientComponent
			}

			]
	}
];
