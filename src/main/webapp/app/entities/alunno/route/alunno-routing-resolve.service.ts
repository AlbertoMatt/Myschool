import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAlunno } from '../alunno.model';
import { AlunnoService } from '../service/alunno.service';

export const alunnoResolve = (route: ActivatedRouteSnapshot): Observable<null | IAlunno> => {
  const id = route.params['id'];
  if (id) {
    return inject(AlunnoService)
      .find(id)
      .pipe(
        mergeMap((alunno: HttpResponse<IAlunno>) => {
          if (alunno.body) {
            return of(alunno.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default alunnoResolve;
