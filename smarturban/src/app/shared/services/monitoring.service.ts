import { Injectable } from '@angular/core';

//Se importa la base de datos
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from '@angular/fire/firestore';

import { Routes_Details } from '../models/routes-detail';
import { Carlog} from '../models/carlog';
//servicio de autenticacion
import { AuthService } from './auth.service';
//servicio de los mensajes
import { ToastrService } from './toastr.service';
import { User } from '../../shared/models/user';


@Injectable({
    providedIn: "root"
  })
  export class MonitoringService {
    userDetail: User;
  
    monitoring: AngularFirestoreCollection<Routes_Details>;
    monitor: AngularFirestoreDocument<Routes_Details>;
      
    buses: AngularFirestoreCollection<Carlog>;
    bus: AngularFirestoreDocument<Carlog>;
    constructor(
      private db: AngularFirestore,
      private authService: AuthService,
      private toastrService: ToastrService
  
    ) {
  
     }


     getBuses(id){
      console.log("nombre clave " + id);
        //autenticamos
        this.userDetail = this.authService.getLoggedInUser();
        //obtenemos los datos de la BD
        console.log("key user " + this.userDetail.$key);
        //this.routes = this.db.collection('routedetails', ref => ref.where('userid' , '==', this.userDetail.$key )/*.where('routeid' , '==', this.userDetail.$key)*/);
    
    
        this.buses = this.db.collection('carlog', 
        ref => ref.where('userid', '==', this.userDetail.$key)
        .where('routeid', '==', id));
                       //console.log("routeid"+whkey);
        return this.buses;
      }



      getRoutesDetails(id){
        //autenticamos
        this.userDetail = this.authService.getLoggedInUser();
        //obtenemos los datos de la BD
        console.log("key user " + this.userDetail.$key);
        //this.routes = this.db.collection('routedetails', ref => ref.where('userid' , '==', this.userDetail.$key )/*.where('routeid' , '==', this.userDetail.$key)*/);
    
    
        this.monitoring = this.db.collection('routedetails', 
        ref => ref.where('userid', '==', this.userDetail.$key)
        /*.where('routeid', '==', whkey)*/);
                       //console.log("routeid"+whkey);
        return this.monitoring;
      }
    }

