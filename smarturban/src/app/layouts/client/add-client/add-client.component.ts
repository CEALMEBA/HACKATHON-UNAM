
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
//import { ProductService } from 'src/app/shared/services/product.service';
//import { Product } from 'src/app/shared/models/product';

import { ClientCompany } from '../../../shared/models/clientcompany';
import { ClientCompanyService } from '../../../shared/services/clientcompany.service';


declare var $: any;
declare var require: any;
declare var toastr: any;
const shortId = require('shortid');
const moment = require('moment');

@Component({
  selector: 'app-add-client',
  templateUrl: './add-client.component.html',
  styleUrls: ['./add-client.component.scss']
})
export class AddClientComponent implements OnInit {

	client: ClientCompany = new ClientCompany();
	constructor(private clientService: ClientCompanyService) {}

	ngOnInit() {}

	createClient(clientForm: NgForm) {
		clientForm.value['clientId'] = 'CLI_' + shortId.generate();
		clientForm.value['clientAdded'] = moment().unix();
		if (clientForm.value['clientImageUrl'] === undefined) {
			clientForm.value['clientImageUrl'] = 'http://via.placeholder.com/640x360/007bff/ffffff';
		}

		const date = clientForm.value['clientAdded'];

		this.clientService.createClientCompany(clientForm.value);

		this.client = new ClientCompany();

	
		toastr.success('client ' + clientForm.value['clientName'] + 'is added successfully', 'Product Creation');
	}
}

