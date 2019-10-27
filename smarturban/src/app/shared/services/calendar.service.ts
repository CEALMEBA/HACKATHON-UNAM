import {
    Injectable
} from "@angular/core";
import {
    CdkDragDrop,
    moveItemInArray,
    transferArrayItem
} from "@angular/cdk/drag-drop";
import {
    AngularFirestore,
    AngularFirestoreCollection,
    AngularFirestoreDocument
} from "@angular/fire/firestore";

// Services
import {
    AuthService
} from "./auth.service";

// Models
import {
    User
} from "../../shared/models/user";
import {
    Routes
} from "../../shared/models/routes";
import {
    Cars
} from "../models/cars";

@Injectable({
    providedIn: "root"
})
export class DropzonesService {
    userDetail: User;
    rutas: AngularFirestoreCollection < Routes > ;
    ruta: AngularFirestoreDocument < Routes > ;

    buses: AngularFirestoreCollection < Cars > ;
    buse: AngularFirestoreDocument < Cars > ;

    routesArray = [];
    unitid = "";

    travelDate: any = [];

    constructor(
        private dropZonesService: DropzonesService,
        private db: AngularFirestore,
        private authService: AuthService
    ) {}

    moveInList(event: CdkDragDrop < string[] > ) {
        moveItemInArray(
            event.container.data,
            event.previousIndex,
            event.currentIndex
        );
    }

    addToList(event: CdkDragDrop < string[] > ) {
        console.log("addToList", {
            event
        });
        this.cloneToList(
            event.previousContainer.data,
            event.container.data,
            event.previousIndex,
            event.currentIndex
        );
    }

    cloneToList < T = any > (
        currentArray: T[],
        targetArray: T[],
        currentIndex: number,
        targetIndex: number
    ): void {
        const to = this.clamp(targetIndex, targetArray.length);

        if (currentArray.length) {
            targetArray.splice(to, 0, currentArray[currentIndex]);
        }
    }

    /** Clamps a number between zero and a maximum. */
    clamp(value: number, max: number): number {
        return Math.max(0, Math.min(max, value));
    }

    // ============================================================================
    /**  Agregar los horarios deacuerdo al dia */
    // ============================================================================

    getCarById(key: string) {
        this.buse = this.db.collection("cars").doc(key);
        //this.db.doc('products/' + key);
        return this.buse;
    }

    setArray(array: any) {
        this.routesArray = array;
    }

    getArray() {
        return this.routesArray;
    }

    setData(data: any) {
        this.unitid = data;
    }

    getData() {
        return this.unitid;
    }

    setTravelDate(data: any){
        this.travelDate = data;
    }

    getTravelDate(){
    return this.travelDate;
    }

    createTravelDate(data: Routes){
      this.userDetail = this.authService.getLoggedInUser();

      const allRoutes = this.routesArray;
      const arrayTravelDate = this.travelDate;
      const unitidd = this.unitid;

      let horaSalida = arrayTravelDate['0'];
      let horaLlegada = arrayTravelDate['1'];

      if(allRoutes[0]){

        let lunes = allRoutes[0];

        let ultimo = lunes[lunes.length - 1];
        let shortidd = this.generateUUID();

        ultimo["shortId"] = shortidd;
        ultimo["unitid"] = unitidd;
        ultimo["boardId"] = 0;
        ultimo["checkIn"] = horaLlegada;
        ultimo["checkOut"] = horaSalida;

        this.db.collection("schedulecars").add(ultimo);
      }
      else if (allRoutes[1]) {
        let martes = allRoutes[1];

        let ultimo = martes[martes.length - 1];
        let shortidd = this.generateUUID();

        ultimo["shortId"] = shortidd;
        ultimo["unitid"] = unitidd;
        ultimo["boardId"] = 1;
        ultimo["checkIn"] = horaLlegada;
        ultimo["checkOut"] = horaSalida;

        this.db.collection("schedulecars").add(ultimo);
      }
      else if (allRoutes[2]) {
        let miercoles = allRoutes[2];

        let ultimo = miercoles[miercoles.length - 1];
        let shortidd = this.generateUUID();

        ultimo["shortId"] = shortidd;
        ultimo["unitid"] = unitidd;
        ultimo["boardId"] = 2;
        ultimo["checkIn"] = horaLlegada;
        ultimo["checkOut"] = horaSalida;

        this.db.collection("schedulecars").add(ultimo);
      }
      else if (allRoutes[3]) {
        let jueves = allRoutes[1];

        let ultimo = jueves[jueves.length - 1];
        let shortidd = this.generateUUID();

        ultimo["shortId"] = shortidd;
        ultimo["unitid"] = unitidd;
        ultimo["boardId"] = 3;
        ultimo["checkIn"] = horaLlegada;
        ultimo["checkOut"] = horaSalida;

        this.db.collection("schedulecars").add(ultimo);
      }
      else if (allRoutes[4]) {
        let viernes = allRoutes[1];

        let ultimo = viernes[viernes.length - 1];
        let shortidd = this.generateUUID();

        ultimo["shortId"] = shortidd;
        ultimo["unitid"] = unitidd;
        ultimo["boardId"] = 4;
        ultimo["checkIn"] = horaLlegada;
        ultimo["checkOut"] = horaSalida;

        this.db.collection("schedulecars").add(ultimo);
      }
      else if (allRoutes[5]) {
        let sabado = allRoutes[5];

        let ultimo = sabado[sabado.length - 1];
        let shortidd = this.generateUUID();

        ultimo["shortId"] = shortidd;
        ultimo["unitid"] = unitidd;
        ultimo["boardId"] = 5;
        ultimo["checkIn"] = horaLlegada;
        ultimo["checkOut"] = horaSalida;

        this.db.collection("schedulecars").add(ultimo);
      }
      else if (allRoutes[6]) {
        let domingo = allRoutes[6];

        let ultimo = domingo[domingo.length - 1];
        let shortidd = this.generateUUID();

        ultimo["shortId"] = shortidd;
        ultimo["unitid"] = unitidd;
        ultimo["boardId"] = 6;
        ultimo["checkIn"] = horaLlegada;
        ultimo["checkOut"] = horaSalida;

        this.db.collection("schedulecars").add(ultimo);
      }


}
generateUUID() {
        let d = new Date().getTime();
        let shortId = "SCHED_KEY_" + "xxxxxxxxxxxx4xxxyxxxxxxxxxxxxxxx".replace(/[xy]/g, function (c) {
            const r = (d + Math.random() * 16) % 16 | 0;
            d = Math.floor(d / 16);
            return (c === "x" ? r : (r & 0x3) | 0x8).toString(16);
    });
  return shortId;
  }

}
