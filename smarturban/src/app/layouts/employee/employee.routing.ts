//import { CartProductsComponent } from './cart-products/cart-products.component';
//import { FavouriteProductsComponent } from './favourite-products/favourite-products.component';
import { EmployeeListComponent } from './employee-list/employee-list.component';
import { Routes } from '@angular/router';
import { IndexComponent } from '../../index/index.component';
import { EmployeeDetailComponent } from './employee-detail/employee-detail.component';
import { AddEmployeeComponent } from './add-employee/add-employee.component';

export const EmployeeRoutes: Routes = [
	{
		path: 'employees',
		children: [
			{
				path: '',
				component: IndexComponent
			},
			{
				path: 'all-employees',
				component: EmployeeListComponent
			},
			{
				path: 'employee/:id',
				component: EmployeeDetailComponent
			},
			{
				path: 'add-employee',
				component: AddEmployeeComponent
			}
				]
	}
];
