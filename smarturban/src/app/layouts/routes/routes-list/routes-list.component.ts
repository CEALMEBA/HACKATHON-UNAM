import { Component, OnInit } from '@angular/core';


//servicios y modelos
import { Routes } from '../../../shared/models/routes';
import { AuthService } from '../../../shared/services/auth.service';
import { RoutesService } from '../../../shared/services/routes.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';






@Component({
  selector: 'app-routes-list',
  templateUrl: './routes-list.component.html',
  styleUrls: ['./routes-list.component.scss']
})
export class RoutesListComponent implements OnInit {
  
  productList: Routes[];
  productObject: Routes;

  loading = false;


  constructor(

    public authService : AuthService,
    public routesService : RoutesService,
    private toastrService : ToastrService

  ) { }

  ngOnInit() {

    this.getAllRoutes(  );


  }


  getAllRoutes(  ){
    console.log("getting routes");

   this.loading = true;
    const x = this.routesService.getRoutes();
    x.snapshotChanges().subscribe(
      (product) => {
        this.loading = false;
        // this.spinnerService.hide();
        this.productList = [];
        console.log("products" + product);

        product.forEach((element) => {
          //con y = element.payload.doc.data(). ..toJSON();
          //y['$key'] = element.key;
          this.productObject = element.payload.doc.data();
          this.productObject.$key = element.payload.doc.id;
          console.log("data : " + this.productObject.$key);
          this.productList.push(this.productObject as Routes);
        });
      },
      (err) => {
        this.toastrService.error('Error while fetching Car List', err);
      }
   );

  }

  deleteRoutes($key){
      console.log($key);

    this.routesService.deleteRoutes($key);

    this.toastrService.success('Routes was deleted successfully', 'Routes deleted');
  }

}
