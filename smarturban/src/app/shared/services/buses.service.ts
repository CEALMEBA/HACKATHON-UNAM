import { Injectable } from "@angular/core";
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from "@angular/fire/firestore";
import { Cars } from "../models/cars";
import { AuthService } from "./auth.service";
import { ToastrService } from "./toastr.service";
import { User } from "../../shared/models/user";
import { Routes } from "../models/routes";
import {Routes_Details} from "../models/routes-detail";




@Injectable({
  providedIn: "root"
})
export class BusesService {
  userDetail: User;

  routes: AngularFirestoreCollection<Routes>;
  route: AngularFirestoreDocument<Routes>;

  buses: AngularFirestoreCollection<Cars>;
  buse: AngularFirestoreDocument<Cars>;

  constructor(
    private db: AngularFirestore,
    private authService: AuthService,
    private toastrService: ToastrService

  ) {

   }

    getBuses() {


      this.userDetail = this.authService.getLoggedInUser();
      this.buses = this.db.collection("cars",
      ref => ref.where("userid", "==", this.userDetail.$key));
      return this.buses;
    }

    createBus(data: Cars) {

      this.userDetail = this.authService.getLoggedInUser();

      data["userid"] = this.userDetail.$key;

      console.log( this.userDetail.$key);

      return this.db.collection("cars").add(data);
    }

    deleteCar(mykey: string ) {

      this.userDetail = this.authService.getLoggedInUser();
      console.log(mykey);
      this.db.collection("cars").doc(mykey).delete().then(function() {
      console.log("Document successfully deleted!");
      }).catch(function(error) {
        console.error("Error removing document: ", error);
        });

    }

    getCarsById(key: string) {
      this.buse =  this.db.collection("cars").doc(key);
      return this.buse;
      }


    updateBus(mykey: string, product: Cars) {

      let description = product["description"];
      let driverId = product["driverid"];
      let placas = product["lplate"];

      this.db.doc("cars/" + mykey).ref.get().then(function(product) {

          if (product.exists) {
            console.log("Document data:", product.data());

            product.ref.update({

              description: description,
              driverid: driverId,
              lplate: placas
            });

          } else {

            console.log("Cant find the Document!!");
          }

      }).catch(function( error ) {
        console.log( "Error Getting Document:", error );
      });
    }

  getAllRoutes(id: string) {
    this.userDetail = this.authService.getLoggedInUser();
    console.log("route id" + this.userDetail.$key );
    this.routes = this.db.collection("routes",
      ref => ref.where("userid", "==", this.userDetail.$key)
        /*.where("routeid", "==", whkey)*/);
    console.log("userid" + id);
    return this.routes;
  }

  }
