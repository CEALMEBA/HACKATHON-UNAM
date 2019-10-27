import { Component, OnInit } from '@angular/core';
import { Sale } from '../../../shared/models/sale';
import { AuthService } from '../../../shared/services/auth.service';
import { SaleService } from '../../../shared/services/sale.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';
import { Router } from '@angular/router';
import { RestApiService } from "../../../shared/services/rest-api.service";


@Component({
  selector: 'app-sale-list',
  templateUrl: './sale-list.component.html',
  styleUrls: ['./sale-list.component.scss']
})
export class SaleListComponent implements OnInit {
    saleList: Sale[];
    saleObject: Sale;
  
    loading = false;
    brands = ['All', 'Google', 'Apple', 'Realme', 'Nokia', 'Motorolla'];
  
    selectedBrand: 'All';
  
    page = 1;

    
    constructor(
      public authService: AuthService,
      private saleService: SaleService,
      private toastrService: ToastrService,
      public restApi: RestApiService,
      public router: Router
    ) { }
  
    ngOnInit() {
      this.getAllSales();
    }
  
    getAllSales() {
      // this.spinnerService.show();
      console.log("getting sales");

      this.loading = true;
      const x = this.saleService.getSales('sal');
      x.snapshotChanges().subscribe(
        (sale) => {
          this.loading = false;
          // this.spinnerService.hide();
          this.saleList = [];
          console.log(" sales " + sale);

          sale.forEach((element) => {
            //con y = element.payload.doc.data(). ..toJSON();
            //y['$key'] = element.key;
            this.saleObject = element.payload.doc.data();
            this.saleObject.$key = element.payload.doc.id; 
            console.log("data : " + this.saleObject.$key); 
            this.saleList.push(this.saleObject as Sale);
          });
        },
        (err) => {
          this.toastrService.error('Error while fetching Sales', err);
        }
      );
    }
  
    removeProduct(key: string) {
      this.saleService.deleteProduct(key);
    }
   
    addTfd(rfc : string, key: string) {
      console.log("rfc :" + rfc);
      //this.saleService.deleteProduct(key);
      return this.restApi.getCfdi(rfc , key).subscribe((data: {}) => {
        console.log("url :" + data["message"] );
        alert("url : " + data["message"])
      })
    }
    
    getHtml(rfc, xml, xslt) {
        console.log("gettting html" + rfc, + " " + xml +  " " + xslt);
        //this.saleService.deleteProduct(key);
 
        return this.restApi.getHtml(rfc, xml, xslt)
         .subscribe((data: {}) => {
          console.log("html :" + data["message"] );
          //alert("html : " + data["message"]);
          //this.router.navigate(['/sales/view-html'])
          window.open(data["message"], "_blank");
        })
    
  
  }
}