import { Component, OnInit } from '@angular/core';
import { ClientCompany } from '../../../shared/models/clientcompany';
import { AuthService } from '../../../shared/services/auth.service';
import { ProviderService } from '../../../shared/services/provider.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';

@Component({
  selector: 'app-provider-list',
  templateUrl: './provider-list.component.html',
  styleUrls: ['./provider-list.component.scss']
})
export class ProviderListComponent implements OnInit {
  
  productList: ClientCompany[];
        productObject: ClientCompany;
      
        loading = false;
        brands = ['All', 'Google', 'Apple', 'Realme', 'Nokia', 'Motorolla'];
      
        selectedBrand: 'All';
      
        page = 1;
        constructor(
          public authService: AuthService,
          private productService: ProviderService,
          private toastrService: ToastrService
        ) { }
      
        ngOnInit() {
          this.getAllProducts();
        }
      
        getAllProducts() {
          // this.spinnerService.show();
          console.log("getting products");
    
          this.loading = true;
          const x = this.productService.getProviders();
          x.snapshotChanges().subscribe(
            (product) => {
              this.loading = false;
              // this.spinnerService.hide();
              this.productList = [];
              console.log(" products" + product);
    
              product.forEach((element) => {
                //con y = element.payload.doc.data(). ..toJSON();
                //y['$key'] = element.key;
                this.productObject = element.payload.doc.data();
                this.productObject.$key = element.payload.doc.id; 
                console.log("data : " + this.productObject.$key); 
                this.productList.push(this.productObject as ClientCompany);
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
      
        addFavourite(product: ClientCompany) {
         // this.productService.addFavouriteProduct(product);
        }
      
        addToCart(product: ClientCompany) {
         // this.productService.addToCart(product);
        }
      }
      