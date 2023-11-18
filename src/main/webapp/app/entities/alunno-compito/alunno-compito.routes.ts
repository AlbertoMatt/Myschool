import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AlunnoCompitoComponent } from './list/alunno-compito.component';
import { AlunnoCompitoDetailComponent } from './detail/alunno-compito-detail.component';
import { AlunnoCompitoUpdateComponent } from './update/alunno-compito-update.component';
import AlunnoCompitoResolve from './route/alunno-compito-routing-resolve.service';

const alunnoCompitoRoute: Routes = [
  {
    path: '',
    component: AlunnoCompitoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AlunnoCompitoDetailComponent,
    resolve: {
      alunnoCompito: AlunnoCompitoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AlunnoCompitoUpdateComponent,
    resolve: {
      alunnoCompito: AlunnoCompitoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AlunnoCompitoUpdateComponent,
    resolve: {
      alunnoCompito: AlunnoCompitoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default alunnoCompitoRoute;
