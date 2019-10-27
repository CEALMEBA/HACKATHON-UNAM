import { Injectable } from '@angular/core';
//import { AngularFireDatabase, AngularFireList, AngularFireObject } from 'angularfire2/database';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

import { Product } from '../models/product';
import { AuthService } from './auth.service';
import { ToastrService } from './toastr.service';
import { ClientCompany } from '../models/clientcompany';
import { User } from '../../shared/models/user';
import { MovInventario } from '../models/movinv';
	
@Injectable()
export class ProductService {
	//products: AngularFireList<Product>;
	//product: AngularFireObject<Product>;
	userDetail: User;

    products: AngularFirestoreCollection<Product>;
	product:  AngularFirestoreDocument<Product>;

	// favouriteProducts
	//favouriteProducts: AngularFireList<FavouriteProduct>;
	//cartProducts: AngularFireList<FavouriteProduct>;
	favouriteProducts: AngularFirestoreCollection<FavouriteProduct>;
	cartProducts:      AngularFirestoreDocument<FavouriteProduct>;


	// NavbarCounts
	navbarCartCount = 0;
	navbarProviderCount = 0;

	navbarFavProdCount = 0;

	constructor(
		private db: AngularFirestore,
		private authService: AuthService,
		private toastrService: ToastrService
	) {

		this.calculateLocalFavProdCounts();
		this.calculateLocalCartProdCounts();
		this.calculateLocalProviderProdCounts();
	}

	getProducts(whkey) {
		//this.products = this.db.list('products');
		this.userDetail = this.authService.getLoggedInUser();

		this.products  = this.db.collection('products', 
			ref => ref.where('userId', '==', this.userDetail.$key)
			.where('warehouseid', '==', whkey));
		

		//var myproducts = productRef.where('userId', '=', );
		//return this.db.collection('products');
		return this.products;
	}

	createProduct(data: Product) {
		//this.products.push(data);
		this.userDetail = this.authService.getLoggedInUser();

		data['userId'] = this.userDetail.$key;
		return this.db.collection('products').add(data);
	}

	getProductById(key: string) {
		this.product =   this.db.collection('products').doc(key);
		//this.db.doc('products/' + key);
		return this.product;
	}


	updateProduct(data: Product) {
	//	this.products.update(data.$key, data);
	}

	deleteProduct(key: string) {
	//	this.products.remove(key);
	}

	/*
   ----------  Favourite Product Function  ----------
  */

	// Get Favourite Product based on userId
	getUsersFavouriteProduct() {
		const user = this.authService.getLoggedInUser();
	//	this.favouriteProducts = this.db.list('favouriteProducts', (ref) =>
//			ref.orderByChild('userId').equalTo(user.$key)
//		);
		return this.favouriteProducts;
	}

	// Adding New product to favourite if logged else to localStorage
	addFavouriteProduct(data: Product): void {
	
	/*	let a: Product[];
		a = JSON.parse(localStorage.getItem('avf_item')) || [];
		a.push(data);
		this.toastrService.wait('Adding Product', 'Adding Product as Favourite');
		setTimeout(() => {
			localStorage.setItem('avf_item', JSON.stringify(a));
			this.calculateLocalFavProdCounts();
		}, 1500);
		*/
		this.userDetail = this.authService.getLoggedInUser();

		var myshoppincar = this.userDetail.$key + "_provider";
		console.log ('provider shoppingcar ' + myshoppincar)
		let a: Product[];

		a = JSON.parse(localStorage.getItem(myshoppincar)) || [];
		if (a.length==0)
		{
			console.log("primer producto");
			//			
			data["productQuatity"] = 1;
			a.push(data);
		}
		else
		{
			var ProductFound = false;

				for (let i = 0; i < a.length; i++) {

					if (a[i].productId == data["productId"]) {

						ProductFound = true;
						console.log("producto ya existe");
						var cantidad = a[i].productQuatity;
						cantidad = cantidad + 1;
						a[i].productQuatity = cantidad;
						console.log("cantidad " + a[i].productQuatity);

		//				a.push(data);
				
					}
					
				}

			if (!ProductFound) {
				
						console.log("producto nuevo");
						data["productQuatity"] = 1;
						a.push(data);

			}
		}
		this.toastrService.wait('Adding Product to Provider Cart', 'Product Adding to the cart');
		setTimeout(() => {
			localStorage.setItem(myshoppincar, JSON.stringify(a));
			this.calculateLocalProviderProdCounts();
		}, 500);
	

	}

	// Fetching unsigned users favourite proucts
	getLocalFavouriteProducts(): Product[] {
		const products: Product[] = JSON.parse(localStorage.getItem('avf_item')) || [];

		return products;
	}

	// Removing Favourite Product from Database
	removeFavourite(key: string) {
//		this.favouriteProducts.remove(key);
	}

	// Removing Favourite Product from localStorage
	removeLocalFavourite(product: Product) {
		const products: Product[] = JSON.parse(localStorage.getItem('avf_item'));

		for (let i = 0; i < products.length; i++) {
			if (products[i].productId === product.productId) {
				products.splice(i, 1);
				break;
			}
		}
		// ReAdding the products after remove
		localStorage.setItem('avf_item', JSON.stringify(products));

		this.calculateLocalFavProdCounts();
	}

	// Returning Local Products Count
	calculateLocalFavProdCounts() {
		this.navbarFavProdCount = this.getLocalFavouriteProducts().length;
	}

	/*
   ----------  Cart Product Function  ----------
  */

    	// Adding new Client to cart db if logged in else localStorage
	addToMovInv(data: MovInventario): void {
		let a: MovInventario;
		console.log("mov 0 " + data);
		//a = JSON.parse(localStorage.getItem('avct_movinv')) || {};

		a = data;
		console.log("mov 1 " + a);

		this.toastrService.wait('Adding Mov Inventario Cart', 'Client Adding to the cart');
		setTimeout(() => {
			localStorage.setItem('avct_movinv', JSON.stringify(a));
			//this.calculateLocalCartProdCounts();
		}, 500);
	}

	// Fetching Locat Client
	getLocalMovInv(): MovInventario {
		const mymovInventario: MovInventario = JSON.parse(localStorage.getItem('avct_movinv')) || {};

		return mymovInventario;
	}

  	// Adding new Client to cart db if logged in else localStorage
	addToClient(data: ClientCompany): void {
		let a: ClientCompany;

		a = JSON.parse(localStorage.getItem('avct_client')) || {};

		a = data;
		this.toastrService.wait('Adding Client to Cart', 'Client Adding to the cart');
		setTimeout(() => {
			localStorage.setItem('avct_client', JSON.stringify(a));
			//this.calculateLocalCartProdCounts();
		}, 500);
	}

	// Fetching Locat Client
	getLocalClient(): ClientCompany {
		const clientCompany: ClientCompany = JSON.parse(localStorage.getItem('avct_client')) || {};

		return clientCompany;
	}
	// Adding new Product to cart db if logged in else localStorage
	addToCart(data: Product): void {
		this.userDetail = this.authService.getLoggedInUser();

		var myshoppincar = this.userDetail.$key + "_sales";
		console.log ('shoppingcar ' + myshoppincar)
		let a: Product[];

		a = JSON.parse(localStorage.getItem(myshoppincar)) || [];
		if (a.length==0)
		{
			console.log("primer producto");
			//			
			data["productQuatity"] = 1;
			a.push(data);
		}
		else
		{
			var ProductFound = false;

				for (let i = 0; i < a.length; i++) {

					if (a[i].productId == data["productId"]) {

						ProductFound = true;
						console.log("producto ya existe");
						var cantidad = a[i].productQuatity;
						cantidad = cantidad + 1;
						a[i].productQuatity = cantidad;
						console.log("cantidad " + a[i].productQuatity);

		//				a.push(data);
				
					}
					
				}

			if (!ProductFound) {
				
						console.log("producto nuevo");
						data["productQuatity"] = 1;
						a.push(data);

			}
		}
		this.toastrService.wait('Adding Product to Cart', 'Product Adding to the cart');
		setTimeout(() => {
			localStorage.setItem(myshoppincar, JSON.stringify(a));
			this.calculateLocalCartProdCounts();
		}, 500);
	}

	// Removing cart from local
	removeLocalCartProduct(product: Product) {
		this.userDetail = this.authService.getLoggedInUser();

		var myshoppincar = this.userDetail.$key + "_sales";
		console.log ('shoppingcar ' + myshoppincar)

		const products: Product[] = JSON.parse(localStorage.getItem(myshoppincar));

		for (let i = 0; i < products.length; i++) {
			if (products[i].productId === product.productId) {
				products.splice(i, 1);
				break;
			}
		}
		// ReAdding the products after remove
		localStorage.setItem(myshoppincar, JSON.stringify(products));

		this.calculateLocalCartProdCounts();
	}

	// Fetching Locat CartsProducts
	getLocalCartProducts(): Product[] {
		this.userDetail = this.authService.getLoggedInUser();

		var myshoppincar = this.userDetail.$key + "_sales";
		console.log ('count shoppingcar ' + myshoppincar)
		
		const products: Product[] = JSON.parse(localStorage.getItem(myshoppincar)) || [];

		return products;
	}

	removeLocalProviderProduct(product: Product) {
		this.userDetail = this.authService.getLoggedInUser();

		var myshoppincar = this.userDetail.$key + "_provider";
		console.log ('count provider shoppingcar ' + myshoppincar)
	
		const products: Product[] = JSON.parse(localStorage.getItem(myshoppincar));

		for (let i = 0; i < products.length; i++) {
			if (products[i].productId === product.productId) {
				products.splice(i, 1);
				break;
			}
		}
		// ReAdding the products after remove
		localStorage.setItem(myshoppincar, JSON.stringify(products));

		this.calculateLocalProviderProdCounts();
	}

	

	getLocalProviderProducts(): Product[] {
		this.userDetail = this.authService.getLoggedInUser();

		var myshoppincar = this.userDetail.$key + "_provider";
		console.log ('count provider shoppingcar ' + myshoppincar)
		
		const products: Product[] = JSON.parse(localStorage.getItem(myshoppincar)) || [];

		return products;
	}
	// returning LocalCarts Product Count
	calculateLocalCartProdCounts() {
		this.navbarCartCount = this.getLocalCartProducts().length;
	}

	calculateLocalProviderProdCounts() {
		this.navbarProviderCount = this.getLocalProviderProducts().length;
	}
}

export class FavouriteProduct {
	product: Product;
	productId: string;
	userId: string;
}
