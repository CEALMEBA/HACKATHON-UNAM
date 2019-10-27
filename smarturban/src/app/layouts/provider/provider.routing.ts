//import { CartProductsComponent } from './cart-products/cart-products.component';
//import { FavouriteProductsComponent } from './favourite-products/favourite-products.component';
import { ProviderListComponent } from './provider-list/provider-list.component';

import { Routes } from '@angular/router';
import { IndexComponent } from '../../index/index.component';
//import { ProductDetailComponent } from './product-detail/product-detail.component';

export const ProviderRoutes: Routes = [
	{
		path: 'providers',
		children: [
			{
				path: '',
				component: IndexComponent
			},
			{
				path: 'all-providers',
				component: ProviderListComponent
			}
				]
	}
];
