import { Cars } from "../../../shared/models/cars";
import { Component, OnInit } from "@angular/core";
import { NgForm } from "@angular/forms";
import { ActivatedRoute } from "@angular/router";
import { BusesService } from "../../../shared/services/buses.service";
import { ToastrService } from "src/app/shared/services/toastr.service";


import {Routes} from "../../../shared/models/routes";
import { RoutesService } from "../../../shared/services/routes.service";
import {Routes_Details} from "../../../shared/models/routes-detail";


declare var $: any;
declare var require: any;
declare var toastr: any;

@Component({
  selector: "app-car-details",
  templateUrl: "./car-details.component.html",
  styleUrls: ["./car-details.component.scss"]
})
export class CarDetailComponent implements OnInit {

  private sub: any;

  product: Cars;
  bus: Cars = new Cars();

  product2: Routes;
  routes: Routes = new Routes();
  routeList: Routes[];
  routeObject: Routes;

  loading = false;

constructor(

    private route: ActivatedRoute,
    private busesService: BusesService,
    private toastrService: ToastrService

  ) {
    this.product = new Cars();
    this.product2 = new Routes();
  }

  ngOnInit() {
    this.sub = this.route.params.subscribe((params) => {
      const id = params["id"]; // (+) converts string 'id' to a number
      this.getCarDetail(id);

      /*this.getAllTasks();*/
      this.getAllRoutesDetails(id);
    });

  }

  getCarDetail(id: string) {

    const x = this.busesService.getCarsById(id);
    console.log("id " + id);
    x.snapshotChanges().subscribe(
      (product) => {
        console.log("product" + product);
        // this.spinnerService.hide();
        // const y = product.payload.data() as Product;
        this.product = product.payload.data();

        this.product["$key"] = id;
        // this.product = y;
      },
      (error) => {
        this.toastrService.error("Error while fetching Product Detail", error);
      }
    );
  }

  updateBus(mykey: string, product: NgForm){
    console.log("Updating Bus" + mykey + " - " +  product);

    this.busesService.updateBus(mykey, product.value);

    this.toastrService.success("Bus was updated successfully", "Bus Updated");
  }
  getAllRoutesDetails(id) {
    console.log("getting routes");

    this.loading = true;
    const x = this.busesService.getAllRoutes(id);
    x.snapshotChanges().subscribe(
      (product2) => {
        this.loading = false;
        // this.spinnerService.hide();
        this.routeList = [];

        console.log("routesdetails" + product2);

        // console.log("description" + product);

        product2.forEach((element) => {
          this.routeObject = element.payload.doc.data();
          this.routeObject.$key = element.payload.doc.id;
          console.log("data : " + this.routeObject.$key);
          console.log("data : " + this.routeObject.source);
          console.log("data : " + this.routeObject.target);
          this.routeList.push(this.routeObject as Routes);
        });
      },
      (err) => {
        this.toastrService.error('Error while fetching route List', err);
      }
    );

  }
}
