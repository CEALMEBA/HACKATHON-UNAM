import { Component, OnInit, Input, OnChanges, SimpleChange, SimpleChanges } from '@angular/core';
import { Product } from '../../../shared/models/product';
import { MovInventario } from '../../../shared/models/movinv';

import { ShippingService } from '../../../shared/services/shipping.service';
import { UserDetail, User } from '../../../shared/models/user';
import { AuthService } from '../../../shared/services/auth.service';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Billing } from '../../../shared/models/billing';
import { ClientCompany } from '../../../shared/models/clientcompany';
import { ClientCompanyService } from '../../../shared/services/clientcompany.service';
import { ProductService } from '../../../shared/services/product.service';


@Component({
  selector: 'app-prov-calculator',
  templateUrl: './prov-calculator.component.html',
  styleUrls: ['./prov-calculator.component.scss']
})
export class ProvCalculatorComponent implements OnInit {
 
    @Input() products: Product[];
  
    userDetails: User;
    clientCompany: ClientCompany;
    movInventario : MovInventario;

    userDetail: UserDetail;
  
    totalValue = 0;
    totalCantidad = 0;
    constructor(
      private shippingService: ShippingService,
      private authService: AuthService,
      private productService: ProductService,
      
    ) {
  
      
    }
  
    ngOnChanges(changes: SimpleChanges) {
      const dataChanges: SimpleChange = changes.products;
  
      const products: Product[] = dataChanges.currentValue;
      this.totalValue = 0;
      this.totalCantidad = 0;
      products.forEach((product) => {
        this.totalCantidad += product.productQuatity;
        this.totalValue += product.productPrice * product.productQuatity;
      });
    }
  
    ngOnInit() {}

    saveMov()
    {
      this.movInventario = this.productService.getLocalMovInv();

      alert(" mov: " + this.movInventario.idMov);
      

      if (this.movInventario.idMov == "Compras")
      {
        this.saveShopping();    
      }

      if (this.movInventario.idMov == "Transpaso")
      {
        this.saveTrans();    
      }
    }
  
    saveShopping()
    {
  
      
      this.clientCompany = this.productService.getLocalClient();
      this.movInventario = this.productService.getLocalMovInv();

      
      this.userDetail = new UserDetail();
      this.userDetails = this.authService.getLoggedInUser();
      alert("compras..." + this.userDetails.userName);
  
      //var data : any;
      var data : any = {};
      var emisor : any = {};
      var receptor : any = {};

      data['type'] = this.movInventario.type;
      data['idmov'] = this.movInventario.idMov;
  
      data['whsource'] = '';
      data['whtarget'] = this.movInventario.whTarget;
  
      data['xml'] = "";
      data['template'] = this.userDetails.template;
      data['shippingDate'] = Date.now();
  
          data['moneda'] =  "MXN",
          data['tipodecomprobante'] = "I",
          data['formapago'] =  "99", 
          data['metodopago'] = "PPD", 
          data['condicionesdepago'] =  "Una sola exhibicion",
          data['lugarexpedicion'] =  this.userDetails.postalcode;
  
      emisor['rfc'] = this.userDetails.rfc;
      emisor['regimenfiscal'] = this.userDetails.regimenfiscal;
      emisor['name'] = this.userDetails.userName;
      emisor['address'] = this.userDetails.address;
      emisor['city'] = this.userDetails.city;
      emisor['col'] = this.userDetails.col;
      emisor['state'] = this.userDetails.state;
      emisor['country'] = this.userDetails.country;
      emisor['postalcode'] = this.userDetails.postalcode;
      emisor['cuentabanco'] = this.userDetails.cuentabanco;
  
      data['emisor'] = emisor;
  
      receptor['rfc'] = this.clientCompany.rfc;
      receptor['name'] = this.clientCompany.name;
      receptor['address'] = this.clientCompany.address;
      receptor['col'] = this.clientCompany.col;		
      receptor['city'] = this.clientCompany.city;
      receptor['state'] = this.clientCompany.state;
      receptor['country'] = this.clientCompany.country;
      receptor['postalcode'] = this.clientCompany.postalcode;
      receptor['usocfdi'] = this.clientCompany.usocfdi;
      receptor['cuentabanco'] = this.clientCompany.cuentabanco;
  
      data['serie'] = this.userDetails.serie
      data['folio'] = this.userDetails.folio
   
      data['receptor'] = receptor;
  
  
      data['emailId'] = this.userDetails.emailId;
      data['userId'] = this.userDetails.$key;
      
      
      const products = [];
  
      let totalPrice = 0;
      let subtotal = 0;
  
      let totalTransIva = 0;
      let totalTransIsr = 0;
      let totalTransIeps = 0;
  
      let totalRetIva = 0;
      let totalRetIsr = 0;
  
      let totalRet = 0;
      
    
      this.products.forEach((product) => {
        //delete product['$key'];
        delete product['favourite'];
        delete product['productAdded'];
        delete product['productBarCode'];
        delete product['productCategory'];
        delete product['productImageUrl'];
        delete product['productSeller'];
        delete product['ratings'];
  
        let importe = product.productPrice * product.productQuatity;
  
  
        let base_trans_ieps = product.productPrice / ( 1 + (product.productTransIeps / 100)); //* product.productQuatity;
        let transIeps = base_trans_ieps * ( product.productTransIeps / 100);
        transIeps = product.productQuatity * transIeps;
  
        let base_trans_iva = importe - transIeps;
        let base_iva = base_trans_iva / ( 1 + (product.productTransIva / 100)); //* product.productQuatity;
        let transIva = base_iva * ( product.productTransIva / 100);
  
  //			let transIsr = importe * ( product.productTransIsr / 100);
        totalTransIva += transIva;
  //			totalTransIsr += transIsr;
        totalTransIeps += transIeps;
  
        let retIva = importe * ( product.productRetIva / 100);
        let retIsr = importe * ( product.productRetIsr / 100);
        totalRetIva += retIva;
        totalRetIsr += retIsr;
  
        product['base_trans_ieps'] = base_trans_ieps;
        product['base_trans_iva'] = base_trans_iva;
  
        product['importe'] = importe;
  
        product['transIva'] = transIva;
  //			product['transIsr'] = transIsr;
        product['transIeps'] = transIeps;
        
        product['retIva'] = retIva;
        product['retIsr'] = retIsr;
  
  
        totalPrice += product.productPrice;
        subtotal +=base_trans_iva;
        products.push(product);
      });
  
      data['products'] = products;
      data['totalPrice'] = totalPrice;
      data['subtotal'] = subtotal;
  
      data['totalTransIva'] = totalTransIva;
    //	data['totalTransIsr'] = totalTransIsr;
      data['totalTransIeps'] = totalTransIeps;
  
      data['totalRetIva'] = totalRetIva;
      data['totalRetIsr'] = totalRetIsr;
  
      
      data['totaltrans'] = totalTransIva + totalTransIsr + totalTransIeps;
      data['totalret'] = totalRetIva + totalRetIsr
      
  
      //alert(" name " + data["firstName"]);
      this.shippingService.createshippings(data);
  
    }


    saveTrans()
    {
  
      
      this.clientCompany = this.productService.getLocalClient();
      this.movInventario = this.productService.getLocalMovInv();

      
      this.userDetail = new UserDetail();
      this.userDetails = this.authService.getLoggedInUser();
      alert("transpaso..." + this.userDetails.userName);
  
      //var data : any;
      var data : any = {};
      var emisor : any = {};
      var receptor : any = {};

      data['type'] = this.movInventario.type;
      data['idmov'] = this.movInventario.idMov;
  
      data['whsource'] = this.movInventario.whSource;
      data['whtarget'] = this.movInventario.whTarget;
  
      data['xml'] = "";
      data['template'] = this.userDetails.template;
      data['shippingDate'] = Date.now();
  
          data['moneda'] =  "MXN",
          data['tipodecomprobante'] = "I",
          data['formapago'] =  "99", 
          data['metodopago'] = "PPD", 
          data['condicionesdepago'] =  "Una sola exhibicion",
          data['lugarexpedicion'] =  this.userDetails.postalcode;
  
      emisor['rfc'] = this.userDetails.rfc;
      emisor['regimenfiscal'] = this.userDetails.regimenfiscal;
      emisor['name'] = this.userDetails.userName;
      emisor['address'] = this.userDetails.address;
      emisor['city'] = this.userDetails.city;
      emisor['col'] = this.userDetails.col;
      emisor['state'] = this.userDetails.state;
      emisor['country'] = this.userDetails.country;
      emisor['postalcode'] = this.userDetails.postalcode;
      emisor['cuentabanco'] = this.userDetails.cuentabanco;
  
      data['emisor'] = emisor;
  
      receptor['rfc'] = this.clientCompany.rfc;
      receptor['name'] = this.clientCompany.name;
      receptor['address'] = this.clientCompany.address;
      receptor['col'] = this.clientCompany.col;		
      receptor['city'] = this.clientCompany.city;
      receptor['state'] = this.clientCompany.state;
      receptor['country'] = this.clientCompany.country;
      receptor['postalcode'] = this.clientCompany.postalcode;
      receptor['usocfdi'] = this.clientCompany.usocfdi;
      receptor['cuentabanco'] = this.clientCompany.cuentabanco;
  
      data['serie'] = this.userDetails.serie
      data['folio'] = this.userDetails.folio
   
      data['receptor'] = receptor;
  
  
      data['emailId'] = this.userDetails.emailId;
      data['userId'] = this.userDetails.$key;
      
      
      const products = [];
  
      let totalPrice = 0;
      let subtotal = 0;
  
      let totalTransIva = 0;
      let totalTransIsr = 0;
      let totalTransIeps = 0;
  
      let totalRetIva = 0;
      let totalRetIsr = 0;
  
      let totalRet = 0;
      
    
      this.products.forEach((product) => {
        //delete product['$key'];
        delete product['favourite'];
        delete product['productAdded'];
        delete product['productBarCode'];
        delete product['productCategory'];
        delete product['productImageUrl'];
        delete product['productSeller'];
        delete product['ratings'];
  
        let importe = product.productPrice * product.productQuatity;
  
  
        let base_trans_ieps = product.productPrice / ( 1 + (product.productTransIeps / 100)); //* product.productQuatity;
        let transIeps = base_trans_ieps * ( product.productTransIeps / 100);
        transIeps = product.productQuatity * transIeps;
  
        let base_trans_iva = importe - transIeps;
        let base_iva = base_trans_iva / ( 1 + (product.productTransIva / 100)); //* product.productQuatity;
        let transIva = base_iva * ( product.productTransIva / 100);
  
  //			let transIsr = importe * ( product.productTransIsr / 100);
        totalTransIva += transIva;
  //			totalTransIsr += transIsr;
        totalTransIeps += transIeps;
  
        let retIva = importe * ( product.productRetIva / 100);
        let retIsr = importe * ( product.productRetIsr / 100);
        totalRetIva += retIva;
        totalRetIsr += retIsr;
  
        product['base_trans_ieps'] = base_trans_ieps;
        product['base_trans_iva'] = base_trans_iva;
  
        product['importe'] = importe;
  
        product['transIva'] = transIva;
  //			product['transIsr'] = transIsr;
        product['transIeps'] = transIeps;
        
        product['retIva'] = retIva;
        product['retIsr'] = retIsr;
  
  
        totalPrice += product.productPrice;
        subtotal +=base_trans_iva;
        products.push(product);
      });
  
      data['products'] = products;
      data['totalPrice'] = totalPrice;
      data['subtotal'] = subtotal;
  
      data['totalTransIva'] = totalTransIva;
    //	data['totalTransIsr'] = totalTransIsr;
      data['totalTransIeps'] = totalTransIeps;
  
      data['totalRetIva'] = totalRetIva;
      data['totalRetIsr'] = totalRetIsr;
  
      
      data['totaltrans'] = totalTransIva + totalTransIsr + totalTransIeps;
      data['totalret'] = totalRetIva + totalRetIsr
      
  
      //alert(" name " + data["firstName"]);
      data['type'] = "ent";
      data['idmov'] = "ent-transpaso";
      this.shippingService.createshippings(data);

      data['type'] = "sal";
      data['idmov'] = "sal-transpaso";
      this.shippingService.createshippings(data);
 
      
    }


  }
  
