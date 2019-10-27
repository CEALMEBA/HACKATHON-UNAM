//import { CartProductsComponent } from './cart-products/cart-products.component';
//import { FavouriteProductsComponent } from './favourite-products/favourite-products.component';
import { ChildsListComponent } from './childs-list/childs-list.component';

import { Routes } from '@angular/router';
import { IndexComponent } from '../../index/index.component';
//import { ProductDetailComponent } from './product-detail/product-detail.component';
import { AddChildComponent } from './add-child/add-child.component';

export const BranchRoutes: Routes = [
	{
		path: 'branches',
		children: [
			{
				path: 'addbranch',
				component: AddChildComponent
			},
			{
				path: 'all-branches',
				component: ChildsListComponent
			}
				]
	}
];
