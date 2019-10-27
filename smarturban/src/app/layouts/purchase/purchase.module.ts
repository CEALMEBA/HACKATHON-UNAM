import { PurchaseListComponent } from './purchase-list/purchase-list.component';
// Core Dependencies
//import { NgModule } from "@angular/core";
import { NgModule, NO_ERRORS_SCHEMA, APP_INITIALIZER } from '@angular/core';

import { CommonModule } from "@angular/common";
import { RouterModule } from "@angular/router";
import { SharedModule } from "../../shared/shared.module";
import { HttpClientModule } from '@angular/common/http';

// configuration and services
import { PurchaseRoutes } from "./purchase.routing";
//import { IndexComponent } from '../../index/index.component';

// Components
/*
import { CheckoutModule } from "./checkout/checkout.module";

import { ProductComponent } from "./product.component";
import { BestProductComponent } from "./best-product/best-product.component";
import { ProductListComponent } from "./product-list/product-list.component";
import { AddProductComponent } from "./add-product/add-product.component";
import { ProductDetailComponent } from "./product-detail/product-detail.component";
import { SharedModule } from "../../shared/shared.module";
import { FavouriteProductsComponent } from "./favourite-products/favourite-products.component";
import { CartProductsComponent } from "./cart-products/cart-products.component";
import { CartCalculatorComponent } from "./cart-calculator/cart-calculator.component";
*/

@NgModule({
	imports: [HttpClientModule, CommonModule, RouterModule.forChild(PurchaseRoutes), SharedModule],
	declarations: [
		PurchaseListComponent
		
	
	],
  exports: [/*BestProductComponent*/],
  schemas: [ NO_ERRORS_SCHEMA ]

})
export class PurchaseModule { }
