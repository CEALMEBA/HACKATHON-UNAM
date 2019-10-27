import { Component, OnInit } from '@angular/core';
import { Warehouse } from '../../../shared/models/warehouse';
import { AuthService } from '../../../shared/services/auth.service';
import { WarehouseService } from '../../../shared/services/warehouse.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';

@Component({
  selector: 'app-warehouses-list',
  templateUrl: './warehouses-list.component.html',
  styleUrls: ['./warehouses-list.component.scss']
})
export class WarehousesListComponent implements OnInit {
    productList: Warehouse[];
    productObject: Warehouse;
  
    loading = false;
    brands = ['All', 'Google', 'Apple', 'Realme', 'Nokia', 'Motorolla'];
  
    selectedBrand: 'All';
  
    page = 1;
    constructor(
      public authService: AuthService,
      private productService: WarehouseService,
      private toastrService: ToastrService
    ) { }
  
    ngOnInit() {
      this.getAllWarehouses();
    }
  
    getAllWarehouses() {
      // this.spinnerService.show();
      this.loading = true;
      const x = this.productService.getWarehouses();
      x.snapshotChanges().subscribe(
        (product) => {
          this.loading = false;
          // this.spinnerService.hide();
          this.productList = [];
          product.forEach((element) => {
            //con y = element.payload.doc.data(). ..toJSON();
            //y['$key'] = element.key;
            this.productObject = element.payload.doc.data();
            this.productObject.$key = element.payload.doc.id; 
            console.log("data : " + this.productObject.$key); 
            this.productList.push(this.productObject as Warehouse);
          });
        },
        (err) => {
          this.toastrService.error('Error while fetching Products', err);
        }
      );
    }
  
    removeProduct(key: string) {
      this.productService.deleteProduct(key);
    }
  
    addFavourite(product: Warehouse) {
     // this.productService.addFavouriteProduct(product);
    }
  
    addToCart(product: Warehouse) {
     // this.productService.addToCart(product);
    }
  }
  