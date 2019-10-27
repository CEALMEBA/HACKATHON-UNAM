import { Injectable } from '@angular/core';
//import { AngularFireDatabase, AngularFireList, AngularFireObject } from 'angularfire2/database';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

import { Warehouse } from '../models/warehouse';
import { AuthService } from './auth.service';
import { ToastrService } from './toastr.service';
import { User } from '../../shared/models/user';

@Injectable()
export class WarehouseService {
	//products: AngularFireList<Product>;
	//product: AngularFireObject<Product>;
    products: AngularFirestoreCollection<Warehouse>;
	product:  AngularFirestoreDocument<Warehouse>;
	userDetail : User;
	// favouriteProducts
	//favouriteProducts: AngularFireList<FavouriteProduct>;
	//cartProducts: AngularFireList<FavouriteProduct>;
	

	// NavbarCounts
	navbarCartCount = 0;
	navbarFavProdCount = 0;

	constructor(
		private db: AngularFirestore,
		private authService: AuthService,
		private toastrService: ToastrService
	) {
	}

	getWarehouses() {
		//this.products = this.db.list('products');
		this.userDetail = this.authService.getLoggedInUser();
		//this.products = this.db.list('products');

		this.products  = this.db.collection('warehouses', 
			ref => ref.where('userId', '==', this.userDetail.$key));


		//this.products = this.db.collection('shippings');
		//return this.db.collection('products');
		return this.products;
	}

	createProduct(data: Warehouse) {
		//this.products.push(data);
		return this.db.collection('childs').add(data);
	}

	getProductById(key: string) {
		this.product =   this.db.collection('shippings').doc(key);
		//this.db.doc('products/' + key);
		return this.product;
	}

	updateProduct(data: Warehouse) {
	//	this.products.update(data.$key, data);
	}

	deleteProduct(key: string) {
	//	this.products.remove(key);
	}

	
}


