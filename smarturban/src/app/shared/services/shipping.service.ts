/*import {
  AngularFireList,
  AngularFireObject,
  AngularFireDatabase
} from "angularfire2/database";
*/
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

import { Billing } from "./../models/billing";
import { Injectable } from "@angular/core";
import { Product } from "./../models/product";
import { ProductService } from './product.service';
//import { Observable } from 'rxjs/Observable';
import { Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';
import { User } from '../../shared/models/user';
import { NgForm } from '@angular/forms';
//import { NgForm } from '@angular/forms';

//import 'rxjs/add/operator/take';
//import 'rxjs/add/operator/take';
//import { take } from 'rxjs/operators';

@Injectable({
  providedIn: "root"
})

export class ShippingService {

  shippings: AngularFirestoreCollection<Billing>;
  shipping: AngularFirestoreDocument<Billing>;
	product:  AngularFirestoreDocument<Product>;
  userDetail : User;
  myproducts: AngularFirestoreCollection<Product>;
	//product:  AngularFirestoreDocument<Product>;

 // shortId = require('shortid');
//  moment = require('moment');

  constructor(private db: AngularFirestore,
    private productService : ProductService,
    private authService : AuthService

    ) {
    this.getshippings();
  }

  createshippings(data: Billing) {
    //this.shippings.push(data);
  var type = data["type"];
  // var collectionName = ''
   //if (type == 'ent') collectionName = 'shippings'

   console.log("tipo: " + type);
   this.db.collection('shippings').add(data);
   var products = data["products"]


   this.userDetail = this.authService.getLoggedInUser();


   products.forEach((product) => {
    console.log ("product update " +   product.$key +  " - " +  product.productQuatity);
    var cantidad = 0;


    console.log ("userid =  " + this.userDetail.$key);
    var whtarget = data["whtarget"];


  this.myproducts  = this.db.collection('products',
    ref => ref.where('userId', '==', this.userDetail.$key)
    .where('productId', '==', product.productId)
    .where('warehouseid', '==', whtarget)

    )

  this.myproducts.get().subscribe(
			(myproduct) => {

    /*  });

    var myn = 0;

    this.myproducts.snapshotChanges().subscribe(
			(myproduct) => {*/
        console.log("n = " + myproduct.size);
       // myn = myproduct.size;
      if (myproduct.size > 0 )
      {
        this.db.doc('products/' + product.$key).ref
                .get()
                .then(function(doc) {
                  if (doc.exists) {
                    console.log("Document data:", doc.data());

                    console.log("Document data:", doc.data().productQuatity);
                    if (type == 'ent')
                      cantidad = doc.data().productQuatity + product.productQuatity;
                    if (type == 'sal')
                      cantidad = doc.data().productQuatity - product.productQuatity;


                      console.log("Document -- data:" + cantidad);

                    //this.db.collection('products').doc(product.$key)
                    doc.ref.update({productQuatity: cantidad});


                  } else {
                    console.log("No such document!");
                  }
                }).catch(function(error) {
                  console.log("Error getting document:", error);
                });
      }
      else
      {
//====================================================================
        let myNewproduct :  any = new Object();
        //var productForm: NgForm = new NgForm();

        myNewproduct["userId"] = product.userId;

	    	myNewproduct["productId"] = product.productId;
		  //  myNewproduct["productAdded"] = 1558374578; //product.productAdded;
        myNewproduct["warehouseid"] = whtarget;
        myNewproduct["productName"] = product.productName;

        myNewproduct["productCategory"] = "--";
        myNewproduct["productPrice"] = product.productPrice;
        myNewproduct["productDescription"] = product.productDescription;
       // myNewproduct["productImageUrl"] = product.productImageUrl;
       console.log("url " + product.productImageUrl);
       if (product.productImageUrl === undefined) {
          myNewproduct["productImageUrl"] = 'http://via.placeholder.com/640x360/007bff/ffffff';
       }
       else
       {
          myNewproduct["productImageUrl"] = product.productImageUrl;
       }

      //  myNewproduct["productAdded"] = product.productAdded;
        myNewproduct["productQuatity"] = product.productQuatity;
      //  myNewproduct["ratings"] = product.ratings;
      //  myNewproduct["favourite"] = product.favourite;
      //  myNewproduct["productSeller"] = product.productSeller;
        myNewproduct["productBarCode"]  = "010101"; //product.productBarCode; // "86101600"; // sat
        myNewproduct["productClaveProdServ"]  = product.productClaveProdServ; // "86101600"; // sat
        myNewproduct["productClaveUnidad"] = product.productClaveUnidad; // "E48";  // sat
        myNewproduct["productUnidad"] = product.productUnidad; // "Unidad de servicio"; //sat
        myNewproduct["productTransIva"] = product.productTransIva; // "Unidad de servicio"; //sat
        myNewproduct["productTransIsr"] = product.productTransIsr; // "Unidad de servicio"; //sat
        myNewproduct["productTransIeps"] = product.productTransIeps; // "Unidad de servicio"; //sat
        myNewproduct["productRetIva"] = product.productRetIva; // "Unidad de servicio"; //sat
        myNewproduct["productRetIsr"] = product.productRetIsr; // "Unidad de servicio"; //sat

        console.log("new product 0 ...");
        this.productService.createProduct(myNewproduct);
        console.log("new product 1 ...");
      }
		},
		(err) => {
			//	this.toastrService.error('Error while fetching Products', err);
			});
  });


}

  getshippings() {
   // this.shippings = this.db.list("shippings");
   // return this.shippings;
  }

  getshippingById(key: string) {
   // this.shipping = this.db.object("products/" + key);
   // return this.shipping;
  }

  updateshipping(data: Billing) {
    //this.shippings.update(data.$key, data);
  }

  deleteshipping(key: string) {
    //this.shippings.remove(key);
  }
}
