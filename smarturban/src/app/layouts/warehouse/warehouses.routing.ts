//import { CartProductsComponent } from './cart-products/cart-products.component';
//import { FavouriteProductsComponent } from './favourite-products/favourite-products.component';
import { WarehousesListComponent } from './warehouses-list/warehouses-list.component';

import { Routes } from '@angular/router';
import { IndexComponent } from '../../index/index.component';
//import { ProductDetailComponent } from './product-detail/product-detail.component';

export const WarehouseRoutes: Routes = [
	{
		path: 'warehouses',
		children: [
			/*{
				path: '',
				component: IndexComponent
			},*/
			{
				path: 'all-warehouses',
				component: WarehousesListComponent
			} 
				]
	}
];
