import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CompitoInClasseComponent } from './list/compito-in-classe.component';
import { CompitoInClasseDetailComponent } from './detail/compito-in-classe-detail.component';
import { CompitoInClasseUpdateComponent } from './update/compito-in-classe-update.component';
import CompitoInClasseResolve from './route/compito-in-classe-routing-resolve.service';

const compitoInClasseRoute: Routes = [
  {
    path: '',
    component: CompitoInClasseComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompitoInClasseDetailComponent,
    resolve: {
      compitoInClasse: CompitoInClasseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompitoInClasseUpdateComponent,
    resolve: {
      compitoInClasse: CompitoInClasseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompitoInClasseUpdateComponent,
    resolve: {
      compitoInClasse: CompitoInClasseResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default compitoInClasseRoute;
