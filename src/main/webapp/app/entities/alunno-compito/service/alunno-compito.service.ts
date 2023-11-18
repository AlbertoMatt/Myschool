import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlunnoCompito, NewAlunnoCompito } from '../alunno-compito.model';

export type PartialUpdateAlunnoCompito = Partial<IAlunnoCompito> & Pick<IAlunnoCompito, 'id'>;

type RestOf<T extends IAlunnoCompito | NewAlunnoCompito> = Omit<T, 'dataRestituizione'> & {
  dataRestituizione?: string | null;
};

export type RestAlunnoCompito = RestOf<IAlunnoCompito>;

export type NewRestAlunnoCompito = RestOf<NewAlunnoCompito>;

export type PartialUpdateRestAlunnoCompito = RestOf<PartialUpdateAlunnoCompito>;

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
    const copy = this.convertDateFromClient(alunnoCompito);
    return this.http
      .post<RestAlunnoCompito>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alunnoCompito: IAlunnoCompito): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alunnoCompito);
    return this.http
      .put<RestAlunnoCompito>(`${this.resourceUrl}/${this.getAlunnoCompitoIdentifier(alunnoCompito)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alunnoCompito: PartialUpdateAlunnoCompito): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alunnoCompito);
    return this.http
      .patch<RestAlunnoCompito>(`${this.resourceUrl}/${this.getAlunnoCompitoIdentifier(alunnoCompito)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlunnoCompito>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlunnoCompito[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
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

  protected convertDateFromClient<T extends IAlunnoCompito | NewAlunnoCompito | PartialUpdateAlunnoCompito>(alunnoCompito: T): RestOf<T> {
    return {
      ...alunnoCompito,
      dataRestituizione: alunnoCompito.dataRestituizione?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAlunnoCompito: RestAlunnoCompito): IAlunnoCompito {
    return {
      ...restAlunnoCompito,
      dataRestituizione: restAlunnoCompito.dataRestituizione ? dayjs(restAlunnoCompito.dataRestituizione) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlunnoCompito>): HttpResponse<IAlunnoCompito> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlunnoCompito[]>): HttpResponse<IAlunnoCompito[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
