import { Component, OnInit } from '@angular/core';
  import { Product } from '../../../shared/models/product';
  import { ProductService } from '../../../shared/services/product.service';
  
  import { ClientCompany } from '../../../shared/models/clientcompany';
  import { ClientCompanyService } from '../../../shared/services/clientcompany.service';
    
  import { Provider } from '../../../shared/models/provider';
  import { ProviderService } from '../../../shared/services/provider.service';

  import { Warehouse } from '../../../shared/models/warehouse';
  import { WarehouseService } from '../../../shared/services/warehouse.service';

  import { ToastrService } from 'src/app/shared/services/toastr.service';
  import { MovInventario } from '../../../shared/models/movinv';
  
@Component({
  selector: 'app-prov-products',
  templateUrl: './prov-products.component.html',
  styleUrls: ['./prov-products.component.scss']
})
export class ProvProductsComponent implements OnInit {
    
  cartProducts: Product[];

    clientCompanyList: ClientCompany[];
    clientCompanyObject: ClientCompany;
    myMovInv : MovInventario;

    selectedClient : String;

    providerList: Provider[];
    providerObject: Provider;
    selectedProvider : String;

    warehouseList: Warehouse[];
    warehouseObject: Warehouse;

    selectedWarehouseSource = '';
    myWarehouseTarget = '';

    loading = false;
    showDataNotFound = true;

    movTypes = ['Compras', 'Transpaso', 'Dev Compra',  'Dev Venta', 'Merma', 'Caducidad'];

  	selectedMov: '';

    // Not Found Message
    messageTitle = 'No Products Found in Cart';
    messageDescription = 'Please, Add Products to Cart';

    isCompras: Boolean = false;
  	isDevolucionCompra: Boolean = false;
  	isDevolucionVenta: Boolean = false;
    isTranspaso: Boolean = false;

    mycompras = 'Compras';
    mytranspaso = 'Transpaso';
    mydevolucionCompra = 'Dev Compra';
    mydevolucionVenta = 'Dev Venta';

    showProv: Boolean = false;
  	showCli: Boolean = false;
  	showAlmacenSource: Boolean = false;
  	showAlmacenTarget: Boolean = false;

    constructor(private productService: ProductService,
      private clientCompanyService: ClientCompanyService,
      private providerService: ProviderService,
      private warehouseService: WarehouseService,

      private toastrService: ToastrService
      ) {}
  
    ngOnInit() {
      this.getCartProduct();
      this.getAllClients();
      this.getAllProviders();
      this.getAllWarehouses();
    }
  

  isOk(movtype): boolean {
    // *ngIf="authService.isAdmin()"
    if (this.selectedMov == movtype) {
        return true;
    }
  }

    getAllWarehouses() {
      // this.spinnerService.show();
      this.loading = true;
      const x = this.warehouseService.getWarehouses();
      x.snapshotChanges().subscribe(
        (product) => {
          this.loading = false;
          // this.spinnerService.hide();
          this.warehouseList = [];
          product.forEach((element) => {
            //con y = element.payload.doc.data(). ..toJSON();
            //y['$key'] = element.key;
            this.warehouseObject = element.payload.doc.data();
            this.warehouseObject.$key = element.payload.doc.id; 
            console.log("warehouse : " + this.warehouseObject.$key); 
            this.warehouseList.push(this.warehouseObject as Warehouse);
          });
        },
        (err) => {
          this.toastrService.error('Error while fetching Warehouses', err);
        }
      );
    }
  
   
    selectMov()
    {
      alert(this.selectedMov);
      
      this.showProv = false;
      this.showCli = false;
      this.showAlmacenSource = false;
      this.showAlmacenTarget = false;

      this.myMovInv = new MovInventario();

      if (this.selectedMov == this.mycompras) 
      {  
          this.isCompras = true;
          this.myMovInv.idMov = this.mycompras;
          this.myMovInv.type = "ent";
          this.myMovInv.whSource ="";
          this.myMovInv.whTarget ="";
      }
      else
       { this.isCompras = false;}
      
      if (this.selectedMov == this.mytranspaso) 
      {  
           this.isTranspaso = true;
           this.myMovInv.idMov = this.mytranspaso;
           this.myMovInv.type = "sal";
           this.myMovInv.whSource ="";
           this.myMovInv.whTarget ="";
      }
      else
      { this.isTranspaso = false;}
 

      this.selectedMov == this.mytranspaso ? 
          this.isTranspaso = true: this.isTranspaso = false;

      this.selectedMov == this.mydevolucionCompra ? 
          this.isDevolucionCompra = true: this.isDevolucionCompra = false;
    
      this.selectedMov == this.mydevolucionVenta ? 
          this.isDevolucionVenta = true: this.isDevolucionVenta = false;
      
      if ((this.isCompras) || (this.isDevolucionCompra))
      {
        this.showProv = true;
        this.showAlmacenTarget = true;
      }

      if (this.isDevolucionVenta)
        this.showCli = true;
      
      if (this.isTranspaso)
      {
        this.showAlmacenSource = true;
        this.showAlmacenTarget = true;
      } 

      

      alert("test : " +  this.myMovInv.idMov);
      this.productService.addToMovInv(this.myMovInv);
     
    }

    removeCartProduct(product: Product) {
      this.productService.removeLocalProviderProduct(product);
  
      // Recalling
      this.getCartProduct();
    }
  
    getCartProduct() {
      this.cartProducts = this.productService.getLocalProviderProducts();
    }

    getAllProviders() {
      //       

      console.log("getting providers");

      this.loading = true;
      const x = this.providerService.getProviders();
      x.snapshotChanges().subscribe(
        (providers) => {
          this.loading = false;
          // this.spinnerService.hide();
          this.providerList = [];
          console.log(" providers" + providers);

          providers.forEach((element) => {
            //con y = element.payload.doc.data(). ..toJSON();
            //y['$key'] = element.key;
            this.providerObject = element.payload.doc.data();
            this.providerObject.$key = element.payload.doc.id; 
            console.log("data : " + this.providerObject.$key); 
            this.providerList.push(this.providerObject as Provider);
          });
        },
        (err) => {
          this.toastrService.error('Error while fetching Providers', err);
        }
      );
    }

    selectProviderData()
    {
      
    //alert(this.selectedClient)
    let myProvider = this.providerList.find(x => x.$key === this.selectedProvider);
   // alert(myProvider.name);
    this.productService.addToClient(myProvider);
    /*
    this.truckNumber = "096754231";
   this.truckBrand = myTruck.brand;
    this.truckModel = myTruck.model;
    this.truckYear = myTruck.year;
    this.truckPlate = myTruck.plate;
    */
    }


    getAllClients() {
          //       
  
          console.log("getting products");
    
          this.loading = true;
          const x = this.clientCompanyService.getClientCompanys();
          x.snapshotChanges().subscribe(
            (clients) => {
              this.loading = false;
              // this.spinnerService.hide();
              this.clientCompanyList = [];
              console.log(" clients" + clients);
    
              clients.forEach((element) => {
                //con y = element.payload.doc.data(). ..toJSON();
                //y['$key'] = element.key;
                this.clientCompanyObject = element.payload.doc.data();
                this.clientCompanyObject.$key = element.payload.doc.id; 
                console.log("data : " + this.clientCompanyObject.$key); 
                this.clientCompanyList.push(this.clientCompanyObject as ClientCompany);
              });
            },
            (err) => {
              this.toastrService.error('Error while fetching Products', err);
            }
          );
        }
     
      selectClientData()
    {
      
    //alert(this.selectedClient);
    let myClient = this.clientCompanyList.find(x => x.$key === this.selectedClient);
    //alert(myClient.name);
    this.productService.addToClient(myClient);
    /*
    this.truckNumber = "096754231";
   this.truckBrand = myTruck.brand;
    this.truckModel = myTruck.model;
    this.truckYear = myTruck.year;
    this.truckPlate = myTruck.plate;
    */
    }
 
    selectWarehouseSource()
    {
      
      alert(this.selectedMov);

    alert("almacen source : " +  this.selectedWarehouseSource);

    this.myMovInv = new MovInventario();
    this.myMovInv.type = "sal";         
    this.myMovInv.idMov = this.selectedMov;                                                                                                                                                                                                                                                                                                           
    this.myMovInv.whSource = this.selectedWarehouseSource ;
    this.myMovInv.whTarget = this.myWarehouseTarget;
    
    this.productService.addToMovInv(this.myMovInv);


    }
 

    selectWhTarget()
    {


    alert("almacen target : " + this.selectedMov + " - " + this.myWarehouseTarget);
    this.myMovInv = new MovInventario();
         
    this.myMovInv.idMov = this.mytranspaso;
    this.myMovInv.type = "ent";
    this.myMovInv.idMov = this.selectedMov;                                                                                                                                                                                                                                                                                                           

    this.myMovInv.whSource = this.selectedWarehouseSource ;
    this.myMovInv.whTarget = this.myWarehouseTarget;
 
    this.productService.addToMovInv(this.myMovInv);
    console.log("type " + this.myMovInv.idMov);
    console.log("source " + this.myMovInv.whSource);
    console.log("target " + this.myMovInv.whTarget);
    

    }
 
  }
  