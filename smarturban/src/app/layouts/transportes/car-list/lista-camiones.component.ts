import { Component, OnInit } from '@angular/core';


//servicios y modelos
import { Cars } from "../../../shared/models/cars";
import { AuthService } from "../../../shared/services/auth.service";
import { BusesService } from "../../../shared/services/buses.service";
import { ToastrService } from "src/app/shared/services/toastr.service";



// components


@Component({
  selector: "app-lista-camiones",
  templateUrl: "./lista-camiones.component.html",
  styleUrls: ["./lista-camiones.component.scss"]
})
export class ListaCamionesComponent implements OnInit {

  productList: Cars[];
  productObject: Cars;

  loading = false;


  constructor(

    public authService: AuthService,
    public busesService: BusesService,
    private toastrService: ToastrService

  ) { }

  ngOnInit() {

    this.getAllProducts(  );


  }
      getAllProducts(  ) {
        console.log("getting Cars");

       this.loading = true;
        const x = this.busesService.getBuses();
        x.snapshotChanges().subscribe(
          (product) => {
            this.loading = false;
            // this.spinnerService.hide();
            this.productList = [];
            console.log("products" + product);

            product.forEach((element) => {
              // con y = element.payload.doc.data(). ..toJSON();
              // y['$key'] = element.key;
              this.productObject = element.payload.doc.data();
              this.productObject.$key = element.payload.doc.id;
              console.log("data : " + this.productObject.$key);
              this.productList.push(this.productObject as Cars);
            });
          },
          (err) => {
            this.toastrService.error("Error while fetching Car List", err);
          }
       );

      }

      deleteBus($key){
          console.log($key);

        this.busesService.deleteCar($key);

        this.toastrService.success("Bus was deleted successfully", "Car deleted");
      }


}
