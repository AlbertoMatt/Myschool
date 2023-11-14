import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'classe',
        data: { pageTitle: 'Classes' },
        loadChildren: () => import('./classe/classe.routes'),
      },
      {
        path: 'alunno',
        data: { pageTitle: 'Alunnos' },
        loadChildren: () => import('./alunno/alunno.routes'),
      },
      {
        path: 'compito-in-classe',
        data: { pageTitle: 'CompitoInClasses' },
        loadChildren: () => import('./compito-in-classe/compito-in-classe.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
