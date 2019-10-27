import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/shared/models/user';
import { AuthService } from 'src/app/shared/services/auth.service';
import { UserService } from 'src/app/shared/services/user.service';
import { Product } from 'src/app/shared/models/product';
import { NgForm } from '@angular/forms';

declare var toastr: any;

@Component({
	selector: 'app-user-account',
	templateUrl: './user-account.component.html',
	styleUrls: [ './user-account.component.scss' ]
})
export class UserAccountComponent implements OnInit {
	loggedUser: User;
	// Enable Update Button

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

		toastr.success('product ' + updateForm.value['userName'] + 'is added successfully', 'Product Creation');
	}

}
