import { Injectable } from '@angular/core';

//Se importa la base de datos
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

//importamos el modelo
import { Routes } from '../models/routes';
//servicio de autenticacion
import { AuthService } from './auth.service';
//servicio de los mensajes
import { ToastrService } from './toastr.service';
import { User } from '../../shared/models/user';


@Injectable()
export class RoutesService {
  userDetail: User;

  routes: AngularFirestoreCollection<Routes>;
  route : AngularFirestoreDocument<Routes>;

  constructor(
    private db: AngularFirestore,
    private authService: AuthService,
    private toastrService: ToastrService

  ){ }

   getRoutes(){

    //autenticamos
    this.userDetail = this.authService.getLoggedInUser();
    //obtenemos los datos de la BD
    this.routes = this.db.collection('routes',
    ref => ref.where('userid' , '==', this.userDetail.$key));

    return this.routes;
  }

  createRoutes(data: Routes){

    this.userDetail = this.authService.getLoggedInUser();

    data['userid'] = this.userDetail.$key;

    console.log( this.userDetail.$key)

    return this.db.collection('routes').add(data);
  }

  deleteRoutes(mykey: string ){

    this.userDetail = this.authService.getLoggedInUser();
    console.log(mykey);
    this.db.collection("routes").doc(mykey).delete().then(function() {
    console.log("Document successfully deleted!");
    }).catch(function(error) {
      console.error("Error removing document: ", error);
      });

  }

  getRoutesById(key: string) {
    this.route =   this.db.collection('routes').doc(key);
    //this.db.doc('products/' + key);
    return this.route;
  }


  updateRoutes(mykey : string, product: Routes){
    console.log("ahi "+mykey);
    var source = product["source"];
    var target = product["target"];
    var duration = product['duration'];
    var status = product['status'];

    this.db.doc('routes/' + mykey).ref.get().then(function(product){

        if(product.exists){
          console.log("Document data:", product.data());

          product.ref.update({
            source: source,
            target: target,
            duration: duration,
            status: status
          });

        }else{

          console.log("Cant find the Document!!");
        }

    }).catch(function( error ){
      console.log( "Error Getting Document:", error )
    });
  }
}