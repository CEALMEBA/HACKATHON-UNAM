import { ElementRef, NgZone, OnInit, ViewChild, Component} from '@angular/core';
import { User } from 'src/app/shared/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { UserService } from 'src/app/shared/services/user.service';
import { ChildService } from 'src/app/shared/services/child.service';

import { Branch } from 'src/app/shared/models/branch';

import { Routes } from '../../../shared/models/routes';
import { Routes_Details } from '../../../shared/models/routes-detail';
import { NgForm } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { MonitoringService } from '../../../shared/services/monitoring.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';

import { MapsAPILoader, AgmMap } from '@agm/core';
import { FormControl } from '@angular/forms';
//import { ConsoleReporter } from 'jasmine';
import { runOutsideAngular } from '@angular/fire';

declare var google: any;

declare var $: any;
declare var require: any;
declare var toastr: any;

class Marker {
  public pos: number;
  public lat: number;
  public lng: number;
  public name: string;
  public image_url: boolean;





  constructor(pos: number, lat: number, lng: number, name: string, image_url) {
      this.pos = pos;
      this.lat = lat;
      this.lng = lng;
      this.name = name;
      this.image_url = image_url;
  }
}

interface Location {
  // lat: number;
  // lng: number;
  // viewport?: Object;
   placename?: string;
   full_address?: string;
   placeid?: string;
   address_level_1?: string;
   address_number?: string;
   address_level_2?: string;
   address_country?: string;
   address_zip?: string;
   address_state?: string;
  // marker?: Marker;
 }

 const shortId = require('shortid');


@Component({
  selector: 'app-monitoring-list',
  templateUrl: './monitoring-list.component.html',
  styleUrls: ['./monitoring-list.component.scss']
})
export class MonitoringListComponent implements OnInit {

  geocoder: any;
  service: any;
  mymap : any;

  productList: Routes_Details[];
  productObject: Routes_Details;

  public description: string;
  public k: string;

  public lat: number;
  public lng: number;
  public mylat: number;
  public mylng: number;

  public keyroute:string;
  public searchControl: FormControl;

  public location: Location = {};
  public branchoffice= new Branch();

  @ViewChild("search")
  public searchElementRef: ElementRef;

    loggedUser: User;

    // Enable Update Button
    markersOnMap: Marker[] = [];
    myzoom = 16;
    mapType = 'roadmap';


    selectedMarker;
    markers = [
      // These are all just random coordinates from https://www.random.org/geographic-coordinates/
     /*{ lat: 18.846881252671754  , lng: -97.10393805802568, alpha: 1 }*/
      /*{ lat: 18.081362059871488 , lng: -96.16088978305055, alpha: 1 },*/
      /*{ lat: 48.75606, lng: -118.859, alpha: 1 },
      { lat: 5.19334, lng: -67.03352, alpha: 1 },
      { lat: 12.09407, lng: 26.31618, alpha: 1 },
      { lat: 47.92393, lng: 78.58339, alpha: 1 }*/
    ];

  
    
  
    //addMarker($event: any) { 
    mapReady($event: any) { 
    
    // here $event will be of type google.maps.Map 

      // and you can put your logic here to get lat lng for marker. I have just put a sample code. You can refactor it the way you want.
      //alert(" gps" +  $event.coords.lat + ", " + $event.coords.lng)
      //  (mapClick)="addMarker($event.coords.lat, $event.coords.lng)"
      this.mymap = $event;
      this.getLatLong('ChIJN1t_tDeuEmsRUsoyG83frY4', $event, null);
    }

    addMarkerl(lat, lng) {

        // here $event will be of type google.maps.Map
          // and you can put your logic here to get lat lng for marker. I have just put a sample code. You can refactor it the way you want.
          //alert(" gps" +  $event.coords.lat + ", " + $event.coords.lng)
          //  (mapClick)="addMarker($event.coords.lat, $event.coords.lng)"
         // this.mymap = $event;

        this.mylat = lat;
        this.mylng = lng;


        this.findAddressByCoordinates();
        }

    max(coordType: 'lat' | 'lng'): number {
      return Math.max(...this.markers.map(marker => marker[coordType]));
    }

    min(coordType: 'lat' | 'lng'): number {
      return Math.min(...this.markers.map(marker => marker[coordType]));
    }

    selectMarker(event) {
    /*  this.selectedMarker = {
        lat: event.latitude,
        lng: event.longitude
      };*/
      this.lat= event.latitude;
      this.lng = event.longitude;

      //alert(this.lat +" , " + this.lng);
      for (let datos of this.productList){
        if(datos.lat == this.lat){
          this.k=datos.$key;
           this.description = datos.description;
      console.log("descripcion: " + datos.description);
      console.log(this.lat);
          }
      }

     /*
      this.productList.forEach(function(element) {
        console.log("latitud: " + element.lat);
        console.log("longitud: " + element.lng);

      });
      */


    }

  private sub: any;
  product: Routes;

  routes: Routes = new Routes();
  loading = false;
  constructor(
    private authService: AuthService,
    private userService: UserService,
    private childService: ChildService,
    public mapsApiLoader: MapsAPILoader,
    private ngZone: NgZone,

    private route: ActivatedRoute,
    private monitoringService: MonitoringService,
		private toastrService: ToastrService

  ) {
    this.product = new Routes();
  }


  ngOnInit() {
    this.sub = this.route.params.subscribe((params) => {
      const id = params['id']; // (+) converts string 'id' to a number
      this.keyroute=id;

      this.getAllRoutesDetails(id);
    });


    this.loggedUser = this.authService.getLoggedInUser();
      this.branchoffice.name = "oxxo";



      this.searchControl = new FormControl();
      this.setCurrentPosition();

      this.mapsApiLoader = this.mapsApiLoader;

      this.mapsApiLoader.load().then(() => {

        this.geocoder = new google.maps.Geocoder();

       /* let autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement, {
          types: ["address"]
        });

        this.service = new google.maps.places.PlacesService();

        autocomplete.addListener("place_changed", () => {
          this.ngZone.run(() => {
            //get the place result
            let place = autocomplete.getPlace();

            //verify result
            if (place.geometry === undefined || place.geometry === null) {
              return;
            }


            //set latitude, longitude and zoom
            this.lat = place.geometry.location.lat();
            this.lng = place.geometry.location.lng();
            this.myzoom = 16;
          });
        });*/
      });
  }

  //----------------------------------MAPA--------------------------------------------------------------------

getAllRoutesDetails(whKey){
  console.log("latitud1 " + whKey);

 this.loading = true;
  const x = this.monitoringService.getRoutesDetails(whKey);
  x.snapshotChanges().subscribe(
    (product) => {
      this.loading = false;
      this.productList = [];

      console.log("product" + product);

      product.forEach((element) => {

        this.productObject = element.payload.doc.data();
        this.productObject.$key = element.payload.doc.id;
        console.log("ruta : " + this.productObject.$key);
        this.productList.push(this.productObject as Routes_Details);
      });
    },
    (err) => {
      this.toastrService.error('Error while fetching Car List', err);
    }
 );

}
/*
addPoint(product: NgForm){



    console.log("latitud2"+this.mylat);

    this.routesdetailsService.createPoint(this.keyroute, this.mylat, this.mylng,  product.value);

    this.routes = new Routes();

    toastr.success('Bus added successfully', 'Product Creation');

}
updatePoint(product: NgForm){


  console.log("Updating Routes" + this.k + " - " +  product);

  this.routesdetailsService.updatePoint(this.k, product.value);

  this.toastrService.success('Bus was updated successfully', 'Bus Updated');

}
*/



//------------------------------------------------------------------------------------------------------









  addBranch(branchForm: NgForm) {


    branchForm.value['childCode'] = 'suc_' + shortId.generate();
    // branchForm.value['userid'] = this.loggedUser.$key;
    branchForm.value['warehouseid'] = 'xxx';
    branchForm.value['description'] = '-';

    branchForm.value['lat'] = this.mylat;
    branchForm.value['lng'] = this.mylng;
    /*productForm.value['ratings'] = Math.floor(Math.random() * 5 + 1);
    if (productForm.value['productImageUrl'] === undefined) {
      productForm.value['productImageUrl'] = 'http://via.placeholder.com/640x360/007bff/ffffff';
    }

    productForm.value['favourite'] = false;

    const date = productForm.value['productAdded'];
*/
    console.log(" form " + branchForm.value['name']);

    this.childService.createBranchOffice(branchForm.value);

    //this.product = new Product();

    //$('#exampleModalLong').modal('hide');
    // https://alligator.io/angular/angular-google-maps/

    toastr.success('branch office ' + branchForm.value['name'] + 'is added successfully', 'Branch office Creation');
  }


  findPlaceById() {

    //var service = new google.maps.places.PlacesService(map);
    var request = {
      placeId: this.location.placeid,
      fields: ['name', 'formatted_address', 'place_id', 'geometry']
    };

    /*
    this.service.getDetails(request, function(place, status) {
      if (status === google.maps.places.PlacesServiceStatus.OK) {

        alert( " place:" + place.name + " - " +
         place.formatted_address);


      }
    });
*/
  }


  findAddressByCoordinates() {
    this.geocoder.geocode({
      'location': {
        lat: this.mylat,
        lng: this.mylng
      }
    }, (results, status) => {
     // alert("results:" + results);
      this.decomposeAddressComponents(results);

    /*  alert("dir : " + this.location.placeid + " : " +
      this.location.address_level_1 + " " +
      this.location.address_number
      + ' - ' + this.location.address_level_2
      + ' - ' + this.location.address_state
      + ' - ' + this.location.address_country
      + ' - ' + this.location.address_zip
      );
*/
     alert("gps " + this.mylat + "-" + this.mylng + "-" + this.location.placeid);

      this.getLatLong(this.location.placeid, this.mymap, null);

    });
  }


  decomposeAddressComponents(addressArray) {
    if (addressArray.length == 0) { return false; }

    let address = addressArray[0].address_components;
    this.location.address_level_1 = '';
    this.location.placeid = addressArray[0].place_id;

    for (let element of address) {
      if (element.length == 0 && !element['types']) { continue; }

      if (element['types'].indexOf('route') > -1) {
        this.location.address_level_1 = '' + element['long_name'];
        continue;
      }
      if (element['types'].indexOf('street_number') > -1) {
        this.location.address_number = ' ' + element['long_name'];
        continue;
      }

      if (element['types'].indexOf('locality') > -1) {
        this.location.address_level_2 = element['long_name'];
        continue;
      }
      if (element['types'].indexOf('administrative_area_level_1') > -1) {
        this.location.address_state = element['long_name'];
        continue;
      }
      if (element['types'].indexOf('country') > -1) {
        this.location.address_country = element['long_name'];
        continue;
      }
      if (element['types'].indexOf('postal_code') > -1) {
        this.location.address_zip = element['long_name'];
        continue;
      }
    }
    this.branchoffice.address = this.location.address_level_1 + " " +
    this.location.address_number
    + ' - ' + this.location.address_level_2
    + ' - ' + this.location.address_state
    + ' - ' + this.location.address_country
    + ' - ' + this.location.address_zip;

  }


  getLatLong(placeid: string, map: any, fn) {
    let placeService = new google.maps.places.PlacesService(map);
    placeService.getDetails({
      placeId: placeid
//        }, function (result, status) {
      }, (results, status) => {

     // this.location.name = result.name;
     //  alert(" ok name : " +
     //  result.name );
       this.decomposePlace(results);
       // console.log(result.geometry.location.lat());
       // console.log(result.geometry.location.lng());
      });
  }


  decomposePlace(placeArray) {
    this.branchoffice.name = placeArray.name;
  }

  private setCurrentPosition() {
    if ("geolocation" in navigator) {
      navigator.geolocation.getCurrentPosition((position) => {
        this.lat = position.coords.latitude;
        this.lng = position.coords.longitude;
        this.myzoom = 16;
      });
    }
  }


  onMouseOver(infoWindow, $event: MouseEvent) {
    infoWindow.open();
}

onMouseOut(infoWindow, $event: MouseEvent) {
    infoWindow.close();
}

}
