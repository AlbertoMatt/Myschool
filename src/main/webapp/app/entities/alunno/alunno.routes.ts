import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AlunnoComponent } from './list/alunno.component';
import { AlunnoDetailComponent } from './detail/alunno-detail.component';
import { AlunnoUpdateComponent } from './update/alunno-update.component';
import AlunnoResolve from './route/alunno-routing-resolve.service';

const alunnoRoute: Routes = [
  {
    path: '',
    component: AlunnoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AlunnoDetailComponent,
    resolve: {
      alunno: AlunnoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AlunnoUpdateComponent,
    resolve: {
      alunno: AlunnoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AlunnoUpdateComponent,
    resolve: {
      alunno: AlunnoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alunnoRoute;
