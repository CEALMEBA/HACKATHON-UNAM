import { Injectable } from '@angular/core';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

import { Routes_Details } from '../models/routes-detail';

//servicio de autenticacion
import { AuthService } from './auth.service';
//servicio de los mensajes
import { ToastrService } from './toastr.service';
import { User } from '../../shared/models/user';

@Injectable()
export class RoutesDetailsService {
  userDetail: User;

  routes: AngularFirestoreCollection<Routes_Details>;
  route : AngularFirestoreDocument<Routes_Details>;


  constructor(
    private db: AngularFirestore,
    private authService: AuthService,
    private toastrService: ToastrService

  ){ }
  
  getRoutesDetails(whkey){
    //autenticamos
    this.userDetail = this.authService.getLoggedInUser();
    //obtenemos los datos de la BD
    console.log("key user" + this.userDetail.$key )
    //this.routes = this.db.collection('routedetails', ref => ref.where('userid' , '==', this.userDetail.$key )/*.where('routeid' , '==', this.userDetail.$key)*/);


    this.routes = this.db.collection('routedetails', 
    ref => ref.where('userid', '==', this.userDetail.$key)
    .where('routeid', '==', whkey));
                   console.log("routeid"+whkey);
    return this.routes;
  }

  createPoint(whkey: string, lat:number, lng: number, data: Routes_Details){

    this.userDetail = this.authService.getLoggedInUser();

    data['userid'] = this.userDetail.$key;
    data['routeid'] = whkey;
    data['lat'] = lat;
    data['lng'] = lng;
    
    console.log( this.userDetail.$key);
    console.log( whkey);
    console.log( lat);
    console.log( lng);

    return this.db.collection('routedetails').add(data);
  }

  updatePoint(k : string, product: Routes_Details){
    console.log("key: "+k);
    var description = product["description"];
    var img = product["img"];
    var color = product["color"];
  

    this.db.doc('routedetails/' + k).ref.get().then(function(product){
        
        if(product.exists){
          console.log("Document data:", product.data());

          product.ref.update({
            description: description,
            img: img,
            color: color
          });

        }else{

          console.log("Cant find the Document!!");
        }

    }).catch(function( error ){
      console.log( "Error Getting Document:", error )
    });
  }



  deletePoint(k: string ){

    this.userDetail = this.authService.getLoggedInUser();
    console.log(k);
    this.db.collection("routedetails").doc(k).delete().then(function() {
    console.log("Document successfully deleted!");
    }).catch(function(error) {
      console.error("Error removing document: ", error);
      });

  }


}