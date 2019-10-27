import { Injectable } from '@angular/core';
//import { AngularFireDatabase, AngularFireList, AngularFireObject } from 'angularfire2/database';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

import { Branch } from '../models/branch';
import { AuthService } from './auth.service';
import { ToastrService } from './toastr.service';
import { User } from '../../shared/models/user';

@Injectable()
export class ChildService {
	//products: AngularFireList<Product>;
	//product: AngularFireObject<Product>;
    products: AngularFirestoreCollection<Branch>;
	product:  AngularFirestoreDocument<Branch>;
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

	getBranches() {
		//this.products = this.db.list('products');
		this.userDetail = this.authService.getLoggedInUser();
		//this.products = this.db.list('products');

		this.products  = this.db.collection('childs', 
			ref => ref.where('userId', '==', this.userDetail.$key));


		//this.products = this.db.collection('shippings');
		//return this.db.collection('products');
		return this.products;
	}

	createBranchOffice(data: Branch) {
		//this.products.push(data);
		
		var newWhId = this.db.createId();
		console.log(" creating warehouse " + newWhId);
		
		this.userDetail = this.authService.getLoggedInUser();

		data['userid'] = this.userDetail.$key;
		let myNewWarehouse :  any = new Object();
        //var productForm: NgForm = new NgForm();

        myNewWarehouse["userId"] = this.userDetail.$key;
		myNewWarehouse["sucid"] = data["childCode"];
		myNewWarehouse["whcode"] = data["name"];
		myNewWarehouse["whdescription"] = data["name"];
		
		//.collection('warehouses').doc();
		this.db.collection('warehouses').doc(newWhId).set(myNewWarehouse);
		console.log('id ' + newWhId );
		
		
	// later...
		//newWhRef.set(data);
		data['warehouseid'] = newWhId;

		this.db.collection('childs').add(data)
		.then(function(whRef)   {
//		}, (results, status) => {

			//createBranch(data, whRef.id);
			console.log("branch written with ID: ", whRef.id);
		})
		.catch(function(error) {
			console.error("Error adding document: ", error);
		});

		return data;
	}

	createProduct(data: Branch) {
		//this.products.push(data);
		return this.db.collection('childs').add(data);
	}

	getProductById(key: string) {
		this.product =   this.db.collection('shippings').doc(key);
		//this.db.doc('products/' + key);
		return this.product;
	}

	updateProduct(data: Branch) {
	//	this.products.update(data.$key, data);
	}

	deleteProduct(key: string) {
	//	this.products.remove(key);
	}

	
}


