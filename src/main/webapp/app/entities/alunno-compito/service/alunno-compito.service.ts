import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlunnoCompito, NewAlunnoCompito } from '../alunno-compito.model';

export type PartialUpdateAlunnoCompito = Partial<IAlunnoCompito> & Pick<IAlunnoCompito, 'id'>;

export type EntityResponseType = HttpResponse<IAlunnoCompito>;
export type EntityArrayResponseType = HttpResponse<IAlunnoCompito[]>;

@Injectable({ providedIn: 'root' })
export class AlunnoCompitoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alunno-compitos');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(alunnoCompito: NewAlunnoCompito): Observable<EntityResponseType> {
    return this.http.post<IAlunnoCompito>(this.resourceUrl, alunnoCompito, { observe: 'response' });
  }

  update(alunnoCompito: IAlunnoCompito): Observable<EntityResponseType> {
    return this.http.put<IAlunnoCompito>(`${this.resourceUrl}/${this.getAlunnoCompitoIdentifier(alunnoCompito)}`, alunnoCompito, {
      observe: 'response',
    });
  }

  partialUpdate(alunnoCompito: PartialUpdateAlunnoCompito): Observable<EntityResponseType> {
    return this.http.patch<IAlunnoCompito>(`${this.resourceUrl}/${this.getAlunnoCompitoIdentifier(alunnoCompito)}`, alunnoCompito, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAlunnoCompito>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAlunnoCompito[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlunnoCompitoIdentifier(alunnoCompito: Pick<IAlunnoCompito, 'id'>): number {
    return alunnoCompito.id;
  }

  compareAlunnoCompito(o1: Pick<IAlunnoCompito, 'id'> | null, o2: Pick<IAlunnoCompito, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlunnoCompitoIdentifier(o1) === this.getAlunnoCompitoIdentifier(o2) : o1 === o2;
  }

  addAlunnoCompitoToCollectionIfMissing<Type extends Pick<IAlunnoCompito, 'id'>>(
    alunnoCompitoCollection: Type[],
    ...alunnoCompitosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alunnoCompitos: Type[] = alunnoCompitosToCheck.filter(isPresent);
    if (alunnoCompitos.length > 0) {
      const alunnoCompitoCollectionIdentifiers = alunnoCompitoCollection.map(
        alunnoCompitoItem => this.getAlunnoCompitoIdentifier(alunnoCompitoItem)!,
      );
      const alunnoCompitosToAdd = alunnoCompitos.filter(alunnoCompitoItem => {
        const alunnoCompitoIdentifier = this.getAlunnoCompitoIdentifier(alunnoCompitoItem);
        if (alunnoCompitoCollectionIdentifiers.includes(alunnoCompitoIdentifier)) {
          return false;
        }
        alunnoCompitoCollectionIdentifiers.push(alunnoCompitoIdentifier);
        return true;
      });
      return [...alunnoCompitosToAdd, ...alunnoCompitoCollection];
    }
    return alunnoCompitoCollection;
  }
}
