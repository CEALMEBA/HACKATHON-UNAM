import { Injectable } from '@angular/core';
//import { AngularFireDatabase, AngularFireList, AngularFireObject } from 'angularfire2/database';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

import { ClientCompany } from '../models/clientcompany';
import { AuthService } from './auth.service';
import { ToastrService } from './toastr.service';
import { User } from '../../shared/models/user';

@Injectable()
export class ClientCompanyService {
	//products: AngularFireList<Product>;
	//product: AngularFireObject<Product>;
	userDetail: User;

    clientcompanys: AngularFirestoreCollection<ClientCompany>;
	clientcompany:  AngularFirestoreDocument<ClientCompany>;

	// favouriteProducts
	//favouriteProducts: AngularFireList<FavouriteProduct>;
	//cartProducts: AngularFireList<FavouriteProduct>;
	//favouriteProducts: AngularFirestoreCollection<FavouriteProduct>;
	//cartProducts:      AngularFirestoreDocument<FavouriteProduct>;

	constructor(
		private db: AngularFirestore,
		private authService: AuthService,
		private toastrService: ToastrService
	) {
	//	this.calculateLocalFavProdCounts();
	//	this.calculateLocalCartProdCounts();
	}

	getClientCompanys() {
		//this.products = this.db.list('products');
		this.userDetail = this.authService.getLoggedInUser();
		//this.products = this.db.list('products');

		this.clientcompanys  = this.db.collection('clientcompanys', 
			ref => ref.where('userId', '==', this.userDetail.$key));

		//this.clientcompanys = this.db.collection('clientcompanys');
		//return this.db.collection('products');
		return this.clientcompanys;
	}

	createClientCompany(data: ClientCompany) {
		//this.products.push(data);
		this.userDetail = this.authService.getLoggedInUser();

		data['userId'] = this.userDetail.$key;
		//return this.db.collection('providers').add(data);
		return this.db.collection('clientcompanys').add(data);
	}

	getProductById(key: string) {
		this.clientcompany =   this.db.collection('clientcompanys').doc(key);
		//this.db.doc('products/' + key);
		return this.clientcompany;
	}

	updateProduct(data: ClientCompany) {
	//	this.products.update(data.$key, data);
	}

	deleteProduct(key: string) {
	//	this.products.remove(key);
	}


}


