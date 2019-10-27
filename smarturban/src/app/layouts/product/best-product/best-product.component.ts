import { TranslateService } from 'src/app/shared/services/translate.service';
import { Component, OnInit } from '@angular/core';
import { Product } from 'src/app/shared/models/product';
import { ProductService } from 'src/app/shared/services/product.service';
import { ToastrService } from 'src/app/shared/services/toastr.service';

@Component({
	selector: 'app-best-product',
	templateUrl: './best-product.component.html',
	styleUrls: [ './best-product.component.scss' ]
})
export class BestProductComponent implements OnInit {
	bestProducts: Product[] = [];
	productObject: Product;

	options: any;
	loading = false;
	constructor(
		private productService: ProductService,
		private toasterService: ToastrService,
		public translate: TranslateService
	) {}

	ngOnInit() {
		this.options = {
			dots: false,
			responsive: {
				'0': { items: 1, margin: 5 },
				'430': { items: 2, margin: 5 },
				'550': { items: 3, margin: 5 },
				'670': { items: 4, margin: 5 }
			},
			autoplay: true,
			loop: true,
			autoplayTimeout: 3000,
			lazyLoad: true
		};
		this.getAllProducts();
	}

	getAllProducts() {
		this.loading = true;
		const x = this.productService.getProducts("");
		
		x.snapshotChanges().subscribe(
			(product) => {
				this.loading = false;
				this.bestProducts = [];
				for (let i = 0; i < 5; i++) {
					//product[i].payload.doc.data
					//const y = product[i].payload.doc.data() as Product;
					//y['$key'] = product[i].payload.doc.data().$key;
					//this.productObject = product[i].payload.doc.data();
					//this.productObject.$key = product[i].payload.doc.id; 
					//console.log("data : " + this.productObject.$key); 
					
					//this.bestProducts.push(this.productObject as Product);
					this.productObject = product[i].payload.doc.data();
					this.productObject.$key = product[i].payload.doc.id; 
					console.log("best data : " + this.productObject.$key); 
					this.bestProducts.push(this.productObject as Product);
				}
				// product.forEach(element => {
				//   const y = element.payload.toJSON();
				//   y["$key"] = element.key;
				//   this.bestProducts.push(y as Product);
				// });
			},
			(error) => {
				this.toasterService.error('Error while fetching Products', error);
			}
		);
	}
}
