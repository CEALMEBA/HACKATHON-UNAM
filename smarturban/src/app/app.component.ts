import { Component, OnInit } from "@angular/core";
import { UserService } from "./shared/services/user.service";
import { fadeAnimation } from "./shared/animations/fadeIntRoute";
//import { NgxSoapService, Client, ISoapMethodResponse } from 'ngx-soap';

declare var $: any;

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"],
  animations: [fadeAnimation]
})
export class AppComponent implements OnInit {
  title = "app";
  
  //client: Client;
  //intA = 2;
  //intB = 3;
  message: string;
  //xmlResponse: string;

  constructor(private userService: UserService
    /*,
    private soap: NgxSoapService*/) {

      //this.soap.createClient('assets/calculator.wsdl')
      /*
      this.soap.createClient('assets/SIFEIService.wsdl')
        .then(client => {
        console.log('Client', client);
        this.client = client;
        console.log('soap client ok...');
        this.sum();
      })
      .catch(err => console.log('Error soap', err));
*/
    }

    /*
  sum() {
    const body = {
      intA: this.intA,
      intB: this.intB
    };

    this.client.call('getCFDI', body).subscribe(res => {
      this.xmlResponse = res.responseBody;
      //this.message = res.result.AddResult;
      //this.loading = false;
      console.log(this.xmlResponse);
    }, err => console.log(err));
  }
  */
  ngOnInit() {
   // this.sum();
    $(document).ready(function() {
      $(".banner").owlCarousel({
        autoHeight: true,
        center: true,
        nav: true,
        items: 1,
        margin: 30,
        loop: true,
        autoplay: true,
        autoplayTimeout: 3000,
        autoplayHoverPause: true
      });
    });

    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(this.setGeoLocation.bind(this));
    }
  }

  setGeoLocation(position: any) {
    this.userService.setLocation(
      position["coords"].latitude,
      position["coords"].longitude
    );
  }
}
