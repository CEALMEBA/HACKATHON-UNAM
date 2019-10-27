// Core Dependencies
import { NgModule, NO_ERRORS_SCHEMA, APP_INITIALIZER } from '@angular/core';

import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";

// configuration and services
import { ClientRoutes } from "./client.routing";
import { ClientListComponent } from './client-list/client-list.component';
//import { IndexComponent } from '../../index/index.component';
import { SharedModule } from "../../shared/shared.module";
import { AddClientComponent } from './add-client/add-client.component';

// Components


@NgModule({
	imports: [CommonModule, RouterModule.forChild(ClientRoutes), SharedModule],
	declarations: [
		ClientListComponent,
		AddClientComponent
	],
	exports: [/*BestProductComponent*/],
	schemas: [ NO_ERRORS_SCHEMA ]

})
export class ClientModule { }
