//import { CartProductsComponent } from './cart-products/cart-products.component';
//import { FavouriteProductsComponent } from './favourite-products/favourite-products.component';
import { PurchaseListComponent } from './purchase-list/purchase-list.component';

import { Routes } from '@angular/router';
import { IndexComponent } from '../../index/index.component';
//import { ProductDetailComponent } from './product-detail/product-detail.component';

export const PurchaseRoutes: Routes = [
	{
		path: 'purchases',
		children: [
			{
				path: '',
				component: IndexComponent
			},
			{
				path: 'all-purchases',
				component: PurchaseListComponent
			}
				]
	}
];
