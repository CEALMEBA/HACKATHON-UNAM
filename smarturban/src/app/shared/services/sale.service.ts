import { Injectable } from '@angular/core';
//import { AngularFireDatabase, AngularFireList, AngularFireObject } from 'angularfire2/database';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

import { Sale } from '../models/sale';
import { AuthService } from './auth.service';
import { ToastrService } from './toastr.service';
import { User } from '../../shared/models/user';

@Injectable()
export class SaleService {
	//products: AngularFireList<Product>;
	//product: AngularFireObject<Product>;
    products: AngularFirestoreCollection<Sale>;
	product:  AngularFirestoreDocument<Sale>;
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

	getSales(type) {
		//this.products = this.db.list('products');
		this.userDetail = this.authService.getLoggedInUser();
		//this.products = this.db.list('products');

		this.products  = this.db.collection('shippings', 
			ref => ref.where('userId', '==', this.userDetail.$key)
			.where('type', '==', type));


		//this.products = this.db.collection('shippings');
		//return this.db.collection('products');
		return this.products;
	}

	createProduct(data: Sale) {
		//this.products.push(data);
		return this.db.collection('shippings').add(data);
	}

	getProductById(key: string) {
		this.product =   this.db.collection('shippings').doc(key);
		//this.db.doc('products/' + key);
		return this.product;
	}

	updateProduct(data: Sale) {
	//	this.products.update(data.$key, data);
	}

	deleteProduct(key: string) {
	//	this.products.remove(key);
	}

	
}


