import {
    Component,
    OnInit
} from '@angular/core';
import {
    NgForm
} from '@angular/forms';
import {
    BusesService
} from '../../../shared/services/buses.service';
import {
    Cars
} from '../../../shared/models/cars';

import {
    ToastrService
} from 'src/app/shared/services/toastr.service';


declare var $: any;
declare var require: any;
declare var toastr: any;
const shortId = require('shortid');
const moment = require('moment');


@Component({
    selector: 'app-all-transportes',
    templateUrl: './all-transportes.component.html',
    styleUrls: ['./all-transportes.component.scss']
})
export class AllTransportesComponent implements OnInit {

    bus: Cars = new Cars();
    constructor(
        private busService: BusesService,
        private toastrService: ToastrService
    ) {}

    ngOnInit() {}

    createBus(busForm: NgForm) {


        console.log(busForm);

        const date = busForm.value['BusAdded'];

        this.busService.createBus(busForm.value);

        this.bus = new Cars();

        toastr.success('Bus added successfully', 'Product Creation');
    }
}
