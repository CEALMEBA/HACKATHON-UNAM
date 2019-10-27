// Core Dependencies
import { NgModule, NO_ERRORS_SCHEMA, APP_INITIALIZER } from '@angular/core';

import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";

// configuration and services
import { EmployeeRoutes } from "./employee.routing";
import { EmployeeListComponent } from './employee-list/employee-list.component';
//import { IndexComponent } from '../../index/index.component';
import { SharedModule } from "../../shared/shared.module";
import { AddEmployeeComponent } from './add-employee/add-employee.component';
import { EmployeeDetailComponent } from './employee-detail/employee-detail.component';

// Components
@NgModule({
	imports: [CommonModule, RouterModule.forChild(EmployeeRoutes), SharedModule],
	declarations: [
		EmployeeListComponent,
		AddEmployeeComponent,
		EmployeeDetailComponent
	],
	exports: [/*BestProductComponent*/],
	schemas: [ NO_ERRORS_SCHEMA ]

})
export class EmployeeModule { }
