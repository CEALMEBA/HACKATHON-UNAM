import { UserComponent } from './user.component';
import { UserAccountComponent } from './user-account/user-account.component';
import { Routes, RouterModule } from '@angular/router';
import { AuthGuard } from 'src/app/shared/services/auth_gaurd';
import { UserChildsComponent } from './user-childs/user-childs.component';
import { UserCertificatesComponent } from './user-certificates/user-certificates.component';

export const UserRoutes: Routes = [
	{
		/*
		path: 'clients',
		children: [
			{
				path: '',
				component: IndexComponent
			},
			{
				path: 'all-clients',
				component: ClientListComponent
			}		]
		*/
		path: 'users',
		//component: UserComponent,
		canActivate: [ AuthGuard ],
		children: [
			{
				path: '',
				component: UserAccountComponent
				//outlet: 'profileOutlet'
			},
			{
				path: 'all-users',
				component: UserChildsComponent
			},
			{
				path: 'all-certificates',
				component: UserCertificatesComponent
			}
		]
	}
];
