import { Component, OnInit } from "@angular/core";
import {
  CdkDragDrop,
  moveItemInArray,
  transferArrayItem,
  copyArrayItem
} from "@angular/cdk/drag-drop";

import { Cars } from "../../../shared/models/cars";

import { BusesService } from "../../../shared/services/buses.service";
import { UserDetail, User } from "../../../shared/models/user";
import { AuthService } from "../../../shared/services/auth.service";
import { DropzonesService } from "../../../shared/services/calendar.service";
import { Routes } from "../../../shared/models/routes";
import { ToastrService } from "src/app/shared/services/toastr.service";
import { ActivatedRoute } from "@angular/router";
import { RoutesService } from "../../../shared/services/routes.service";
import { Routes_Details } from "../../../shared/models/routes-detail";
import { NgForm } from "@angular/forms";

@Component({
  selector: "app-calendar",
  templateUrl: "./calendar.component.html",
  styleUrls: ["./calendar.component.scss"]
})
export class CalendarComponent implements OnInit {

    travelDate: any = [];


  cars: Cars;

  unitid = "";

  lunes: any = [];
  martes: any = [];
  miercoles: any = [];
  jueves: any = [];
  viernes: any = [];
  sabado: any = [];
  domingo: any = [];

  rutas: any = [
    this.lunes,
    this.martes,
    this.miercoles,
    this.jueves,
    this.viernes,
    this.sabado,
    this.domingo
  ];

  userDetail: UserDetail;
  userDetails: User;

  private sub: any;

  product2: Routes;
  routes: Routes = new Routes();
  routeList: Routes[];
  routeObject: Routes;

  loading = false;

  constructor(
    private route: ActivatedRoute,
    private busesService: BusesService,
    private toastrService: ToastrService,
    private authService: AuthService,
    private dropZones: DropzonesService
  ) {}

  ngOnInit() {
    this.sub = this.route.params.subscribe(params => {
      const id = params["id"];

      this.getAllRoutesDetails(id);
      this.sendArray(this.rutas);
      this.getCarDetail(id);
      this.sendData(this.unitid);
      this.sendTravelDate(this.travelDate);
    });
  }

  drop(event: CdkDragDrop<string[]>) {
    // if drop event is from an item that was already on canvas
    if (event.container === event.previousContainer) {
      // sort it based on where it was dropped
      this.dropZones.moveInList(event);
    } else {
      copyArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
    }
  }

  onSelect(event) {
    console.log(event);
  }

  getAllRoutesDetails(id) {
    console.log("getting routes");

    this.loading = true;
    const x = this.busesService.getAllRoutes(id);
    x.snapshotChanges().subscribe(
      product2 => {
        this.loading = false;
        // this.spinnerService.hide();
        this.routeList = [];

        console.log("routesdetails" + product2);

        // console.log("description" + product);

        product2.forEach(element => {
          this.routeObject = element.payload.doc.data();
          this.routeObject.$key = element.payload.doc.id;
          console.log("data : " + this.routeObject.$key);
          console.log("data : " + this.routeObject.source);
          console.log("data : " + this.routeObject.target);
          this.routeList.push(this.routeObject as Routes);
        });
      },
      err => {
        this.toastrService.error("Error while fetching route List", err);
      }
    );
  }

  getCarDetail(id: string) {
    const x = this.dropZones.getCarById(id);
    console.log("unitid: " + id);
    x.snapshotChanges().subscribe(
      (data) => {
        console.log("product" + data);
        this.unitid = data.payload.data().unitid;

        console.log("id de la unidad: " + this.unitid);

        this.sendData(this.unitid);

        // this.product = y;
      },
      error => {
        this.toastrService.error("Error while fetching data Detail", error);
      }
    );
  }

  sendArray(array: any) {
    this.dropZones.setArray(array);
  }

  sendData(data: any) {
    this.dropZones.setData(data);
  }

  sendTravelDate(data: any){
    this.dropZones.setTravelDate(data);
  }

  // saveSchedule() {
  //   this.userDetail = new UserDetail();
  //   this.userDetails = this.authService.getLoggedInUser();
  //   this.dropZones.createSchedule();
  // }

    saveTravelDate(travelForm: NgForm){

      console.log(travelForm)

      console.log("Ruta:", this.travelDate);
      this.dropZones.createTravelDate(travelForm.value);
    }
}
