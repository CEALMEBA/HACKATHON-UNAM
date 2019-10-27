import { Component, OnInit } from '@angular/core';
import { Product } from '../../../shared/models/product';
import { Warehouse } from '../../../shared/models/warehouse';

import { AuthService } from '../../../shared/services/auth.service';
import { ProductService } from '../../../shared/services/product.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';
import { WarehouseService } from '../../../shared/services/warehouse.service';
import { MarketingService } from '../../../shared/services/marketing.service';

@Component({
	selector: 'app-product-list',
	templateUrl: './product-list.component.html',
	styleUrls: ['./product-list.component.scss']
})
export class ProductListComponent implements OnInit {
	productList: Product[];
	productObject: Product;
	warehouseList: Warehouse[];
	warehouseObject: Warehouse;

	loading = false;
	brands = ['All', 'Google', 'Apple', 'Realme', 'Nokia', 'Motorolla'];

	selectedWh: 'All';
	selectedSuc: string = '';

	page = 1;
	constructor(
		public authService: AuthService,
		private productService: ProductService,
		private warehouseService: WarehouseService,
		private marketingService: MarketingService,
		private toastrService: ToastrService
	) { }

	ngOnInit() {
		this.getAllProducts("");
		this.getAllWarehouses();

	}

	  
	selectWarehouseData()
	{
	  
	alert(this.selectedWh);
	this.getAllProducts(this.selectedWh);

	
	let myWh = this.warehouseList.find(x => x.$key === this.selectedWh);
	this.selectedSuc = myWh.sucid;

	alert(this.selectedSuc);

	/*
	this.trailer1Brand = myTrailer1.brand;
	this.trailer1Type = myTrailer1.trailertype;
	this.trailer1Model = myTrailer1.model;
	this.trailer1Year = myTrailer1.year;
	this.trailer1Plate = myTrailer1.plate;
	*/
	}

	
	getAllProducts(whKey) {
		// this.spinnerService.show();
		this.loading = true;
		const x = this.productService.getProducts(whKey);
		x.snapshotChanges().subscribe(
			(product) => {
				this.loading = false;
				// this.spinnerService.hide();
				this.productList = [];
				product.forEach((element) => {
					//con y = element.payload.doc.data(). ..toJSON();
					//y['$key'] = element.key;
					this.productObject = element.payload.doc.data();
					this.productObject.$key = element.payload.doc.id; 
					console.log("data : " + this.productObject.$key); 
					this.productList.push(this.productObject as Product);
				});
			},
			(err) => {
				this.toastrService.error('Error while fetching Products', err);
			}
		);
	}

	getAllWarehouses() {
		// this.spinnerService.show();
		this.loading = true;
		const x = this.warehouseService.getWarehouses();
		x.snapshotChanges().subscribe(
			(product) => {
				this.loading = false;
				// this.spinnerService.hide();
				this.warehouseList = [];
				product.forEach((element) => {
					//con y = element.payload.doc.data(). ..toJSON();
					//y['$key'] = element.key;
					this.warehouseObject = element.payload.doc.data();
					this.warehouseObject.$key = element.payload.doc.id; 
					console.log("warehouses : " + this.warehouseObject.$key); 
					this.warehouseList.push(this.warehouseObject as Warehouse);
				});
			},
			(err) => {
				this.toastrService.error('Error while fetching Warehouses', err);
			}
		);
	}

	removeProduct(key: string) {
		this.productService.deleteProduct(key);
	}

	addFavourite(product: Product) {
		this.productService.addFavouriteProduct(product);
	}

	addToProv(product: Product) {
		this.productService.addFavouriteProduct(product);
	}

	addToCart(product: Product) {
		this.productService.addToCart(product);
	}

	addMarketing(product: Product) {
		let myNewproduct :  any = new Object();

		myNewproduct["sucid"] = this.selectedSuc;

	    myNewproduct["key$"] = product.$key;
        myNewproduct["description"] = product.productDescription;
        myNewproduct["productid"] = product.productId;
        myNewproduct["price"] = product.productPrice;
        myNewproduct["quantity"] = "1";
        myNewproduct["status"] = 1;

		console.log("url " + product.productImageUrl);
        if (product.productImageUrl === undefined) {
          myNewproduct["imageurl"] = 'http://via.placeholder.com/640x360/007bff/ffffff';
        }
        else
        {
           myNewproduct["imageurl"] = product.productImageUrl;
        }
  
     
		this.marketingService.createProduct(myNewproduct);
	}

}
