import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlunnoCompito } from '../alunno-compito.model';
import { AlunnoCompitoService } from '../service/alunno-compito.service';

export const alunnoCompitoResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlunnoCompito> => {
  const id = route.params['id'];
  if (id) {
    return inject(AlunnoCompitoService)
      .find(id)
      .pipe(
        mergeMap((alunnoCompito: HttpResponse<IAlunnoCompito>) => {
          if (alunnoCompito.body) {
            return of(alunnoCompito.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default alunnoCompitoResolve;
