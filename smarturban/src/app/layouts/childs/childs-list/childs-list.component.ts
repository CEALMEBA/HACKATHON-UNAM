import { Component, OnInit } from '@angular/core';
import { Branch } from '../../../shared/models/branch';
import { AuthService } from '../../../shared/services/auth.service';
import { ChildService } from '../../../shared/services/child.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';

@Component({
  selector: 'app-childs-list',
  templateUrl: './childs-list.component.html',
  styleUrls: ['./childs-list.component.scss']
})
export class ChildsListComponent implements OnInit {
    productList: Branch[];
    productObject: Branch;
  
    loading = false;
    brands = ['All', 'Google', 'Apple', 'Realme', 'Nokia', 'Motorolla'];
  
    selectedBrand: 'All';
  
    page = 1;
    constructor(
      public authService: AuthService,
      private productService: ChildService,
      private toastrService: ToastrService
    ) { }
  
    ngOnInit() {
      this.getAllProducts();
    }
  
    getAllProducts() {
      // this.spinnerService.show();
      this.loading = true;
      const x = this.productService.getBranches();
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
            this.productList.push(this.productObject as Branch);
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
  
    addFavourite(product: Branch) {
     // this.productService.addFavouriteProduct(product);
    }
  
    addToCart(product: Branch) {
     // this.productService.addToCart(product);
    }
  }
  