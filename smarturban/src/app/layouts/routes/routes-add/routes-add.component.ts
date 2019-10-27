import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { RoutesService } from '../../../shared/services/routes.service';
import { Routes } from '../../../shared/models/routes';

import { ToastrService } from 'src/app/shared/services/toastr.service';


declare var $: any;
declare var require: any;
declare var toastr: any;
const shortId = require('shortid');
const moment = require('moment');

@Component({
  selector: 'app-routes-add',
  templateUrl: './routes-add.component.html',
  styleUrls: ['./routes-add.component.scss']
})
export class RoutesAddComponent implements OnInit {

  route: Routes = new Routes();
	constructor(
    private busService: RoutesService,
    private toastrService : ToastrService
    ) { }

  ngOnInit() {
  }

  createRoutes(routesForm: NgForm) {


    console.log(routesForm);

    const date = routesForm.value['BusAdded'];

    this.busService.createRoutes(routesForm.value);

    this.route = new Routes();


    toastr.success('Bus added successfully', 'Product Creation');
  }
}
