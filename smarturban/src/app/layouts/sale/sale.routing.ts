//import { CartProductsComponent } from './cart-products/cart-products.component';
//import { FavouriteProductsComponent } from './favourite-products/favourite-products.component';
import { SaleListComponent } from './sale-list/sale-list.component';
import { ViewhtmlComponent } from './viewhtml/viewhtml.component';

import { Routes } from '@angular/router';
import { IndexComponent } from '../../index/index.component';
//import { ProductDetailComponent } from './product-detail/product-detail.component';

export const SaleRoutes: Routes = [
	{
		path: 'sales',
		children: [
			{
				path: '',
				component: IndexComponent
			},
			{
				path: 'all-sales',
				component: SaleListComponent
			},
			{
				path: 'view-html',
				component: ViewhtmlComponent
			}
				]
	}
];
