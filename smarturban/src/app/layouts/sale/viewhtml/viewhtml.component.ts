import { Product } from '../../../shared/models/product';
import { ProductService } from '../../../shared/services/product.service';
import { Component, OnInit, ViewChild } from '@angular/core';
//import { Http } from '@angular/http';
//import { map } from 'rxjs/operators';
import { HttpClient } from '@angular/common/http';

import * as jspdf from 'jspdf';
import html2canvas from 'html2canvas';
declare var $: any;
  

@Component({
  selector: 'app-viewhtml',
  templateUrl: './viewhtml.component.html',
  styleUrls: ['./viewhtml.component.scss']
})
export class ViewhtmlComponent implements OnInit {
  
  
    products: Product[];
    date: number;
    totalPrice = 0;
    tax = 6.4;
    private viewContent:any;

    private myTemplate: any = "";
    constructor(private productService: ProductService, http: HttpClient) {
      /* Hiding Billing Tab Element */
     /* document.getElementById('productsTab').style.display = 'none';
      document.getElementById('shippingTab').style.display = 'none';
      document.getElementById('billingTab').style.display = 'none';
      document.getElementById('resultTab').style.display = 'block';
  */
     //http.get('https://storage.googleapis.com/kubeet-cfdi.appspot.com/KUB150805PH7/cfdi_view.html').map((html:any) => this.myTemplate = html);
/*
     http.get('https://storage.googleapis.com/kubeet-cfdi.appspot.com/KUB150805PH7/cfdi_view.html',
      {responseType: 'text'}).subscribe(data => {
    	//this.viewFile = 'default.html';
    	this.viewContent = data;
	    });
    */

     this.products = productService.getLocalCartProducts();
  
      this.products.forEach((product) => {
        this.totalPrice += product.productPrice;
      });
  
      this.date = Date.now();
    }
  
    ngOnInit() {

      
     }
  
    downloadReceipt() {
      const data = document.getElementById('receipt');
      // console.log(data);
  
      html2canvas(data).then((canvas) => {
        // Few necessary setting options
        const imgWidth = 208;
        const pageHeight = 295;
        const imgHeight = canvas.height * imgWidth / canvas.width;
        const heightLeft = imgHeight;
  
        const contentDataURL = canvas.toDataURL('image/png');
        const pdf = new jspdf('p', 'mm', 'a4'); // A4 size page of PDF
        const position = 0;
        pdf.addImage(contentDataURL, 'PNG', 0, position, imgWidth, imgHeight);
        pdf.save('pop_cfdi.pdf'); // Generated PDF
      });
    }
  }
  