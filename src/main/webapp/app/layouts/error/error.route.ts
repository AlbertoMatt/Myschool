import { Routes } from '@angular/router';

import ErrorComponent from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    title: 'Pagina di errore!',
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      errorMessage: 'Non si dispongono i privilegi sufficienti per accedere a qusta pagina.',
    },
    title: 'Pagina di errore!',
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      errorMessage: 'La pagina non esiste.',
    },
    title: 'Pagina di errore!',
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
