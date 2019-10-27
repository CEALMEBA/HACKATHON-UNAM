import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/shared/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { UserService } from 'src/app/shared/services/user.service';
import { Product } from 'src/app/shared/models/product';
import { NgForm } from '@angular/forms';
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

@Component({
  selector: 'app-add-child',
  templateUrl: './add-child.component.html',
  styleUrls: ['./add-child.component.scss']
})

export class AddChildComponent implements OnInit {
  
 /*
     <agm-polygon
          [geodesic]="true"
          [strokeColor]=""
          [strokeOpacity]="0.3"
          [fillOpacity]="0.0"
          [strokeWeight]="3"
          
          [paths]="markersOnMap">
              <agm-marker
                  [label]="labelOptions"

                  *ngFor="let marker of markersOnMap; index as i"
                  [latitude]="marker.lat" 
                  [longitude]="marker.lng"
                  [label]="marker.pos"
                  [iconUrl]="marker.image_url"> 

                  <agm-info-window
                      [maxWidth]="150">
                      <div class="container">
                          <p class="font-weight-bold">
                              {{marker.name}}
                          </p>
                          <img 
                              src="{{marker.image_url}}" 
                              class="img-thumbnail"
                              *ngIf="marker.image_url.length > 5">
                      </div>
                  </agm-info-window>
              </agm-marker>
            
      </agm-polygon>
 */
 
    loggedUser: User;
    // Enable Update Button
    markersOnMap: Marker[] = [];
    lat: number = 18.8519500;
    lng: number = -97.0995700;
    constructor(private authService: AuthService,
      private userService: UserService
      ) {}
  
    ngOnInit() {
      this.loggedUser = this.authService.getLoggedInUser();
    }
    
    updateUser(updateForm: NgForm) {
      //productForm.value['productId'] = 'PROD_' + shortId.generate();
      //productForm.value['productAdded'] = moment().unix();
      /*productForm.value['ratings'] = Math.floor(Math.random() * 5 + 1);
      if (productForm.value['productImageUrl'] === undefined) {
        productForm.value['productImageUrl'] = 'http://via.placeholder.com/640x360/007bff/ffffff';
      }
  
      productForm.value['favourite'] = false;
  
      const date = productForm.value['productAdded'];
  */
  
      this.userService.updateUser(this.loggedUser.$key, updateForm.value);
  
      //this.product = new Product();
  
      //$('#exampleModalLong').modal('hide');
      // https://alligator.io/angular/angular-google-maps/
  
      toastr.success('product ' + updateForm.value['userName'] + 'is added successfully', 'Product Creation');
    }
  
  }
  