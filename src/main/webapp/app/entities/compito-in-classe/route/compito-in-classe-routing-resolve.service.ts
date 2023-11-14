import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICompitoInClasse } from '../compito-in-classe.model';
import { CompitoInClasseService } from '../service/compito-in-classe.service';

export const compitoInClasseResolve = (route: ActivatedRouteSnapshot): Observable<null | ICompitoInClasse> => {
  const id = route.params['id'];
  if (id) {
    return inject(CompitoInClasseService)
      .find(id)
      .pipe(
        mergeMap((compitoInClasse: HttpResponse<ICompitoInClasse>) => {
          if (compitoInClasse.body) {
            return of(compitoInClasse.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default compitoInClasseResolve;
