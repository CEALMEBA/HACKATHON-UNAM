import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-transportes',
  templateUrl: './transportes.component.html',
  styleUrls: ['./transportes.component.scss']
})
export class TransportesComponent implements OnInit {

  constructor() { }

  ngOnInit() {
      this.getAllBuses();

  }
  getAllBuses(){
    console.log("getting Buses");
  };
}
