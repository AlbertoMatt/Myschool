import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompitoInClasse, NewCompitoInClasse } from '../compito-in-classe.model';

export type PartialUpdateCompitoInClasse = Partial<ICompitoInClasse> & Pick<ICompitoInClasse, 'id'>;

type RestOf<T extends ICompitoInClasse | NewCompitoInClasse> = Omit<T, 'data' | 'dataRestituizione'> & {
  data?: string | null;
  dataRestituizione?: string | null;
};

export type RestCompitoInClasse = RestOf<ICompitoInClasse>;

export type NewRestCompitoInClasse = RestOf<NewCompitoInClasse>;

export type PartialUpdateRestCompitoInClasse = RestOf<PartialUpdateCompitoInClasse>;

export type EntityResponseType = HttpResponse<ICompitoInClasse>;
export type EntityArrayResponseType = HttpResponse<ICompitoInClasse[]>;

@Injectable({ providedIn: 'root' })
export class CompitoInClasseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compito-in-classes');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(compitoInClasse: NewCompitoInClasse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compitoInClasse);
    return this.http
      .post<RestCompitoInClasse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(compitoInClasse: ICompitoInClasse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compitoInClasse);
    return this.http
      .put<RestCompitoInClasse>(`${this.resourceUrl}/${this.getCompitoInClasseIdentifier(compitoInClasse)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(compitoInClasse: PartialUpdateCompitoInClasse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(compitoInClasse);
    return this.http
      .patch<RestCompitoInClasse>(`${this.resourceUrl}/${this.getCompitoInClasseIdentifier(compitoInClasse)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCompitoInClasse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCompitoInClasse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getCompitoInClasseIdentifier(compitoInClasse: Pick<ICompitoInClasse, 'id'>): number {
    return compitoInClasse.id;
  }

  compareCompitoInClasse(o1: Pick<ICompitoInClasse, 'id'> | null, o2: Pick<ICompitoInClasse, 'id'> | null): boolean {
    return o1 && o2 ? this.getCompitoInClasseIdentifier(o1) === this.getCompitoInClasseIdentifier(o2) : o1 === o2;
  }

  addCompitoInClasseToCollectionIfMissing<Type extends Pick<ICompitoInClasse, 'id'>>(
    compitoInClasseCollection: Type[],
    ...compitoInClassesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const compitoInClasses: Type[] = compitoInClassesToCheck.filter(isPresent);
    if (compitoInClasses.length > 0) {
      const compitoInClasseCollectionIdentifiers = compitoInClasseCollection.map(
        compitoInClasseItem => this.getCompitoInClasseIdentifier(compitoInClasseItem)!,
      );
      const compitoInClassesToAdd = compitoInClasses.filter(compitoInClasseItem => {
        const compitoInClasseIdentifier = this.getCompitoInClasseIdentifier(compitoInClasseItem);
        if (compitoInClasseCollectionIdentifiers.includes(compitoInClasseIdentifier)) {
          return false;
        }
        compitoInClasseCollectionIdentifiers.push(compitoInClasseIdentifier);
        return true;
      });
      return [...compitoInClassesToAdd, ...compitoInClasseCollection];
    }
    return compitoInClasseCollection;
  }

  protected convertDateFromClient<T extends ICompitoInClasse | NewCompitoInClasse | PartialUpdateCompitoInClasse>(
    compitoInClasse: T,
  ): RestOf<T> {
    return {
      ...compitoInClasse,
      data: compitoInClasse.data?.format(DATE_FORMAT) ?? null,
      dataRestituizione: compitoInClasse.dataRestituizione?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restCompitoInClasse: RestCompitoInClasse): ICompitoInClasse {
    return {
      ...restCompitoInClasse,
      data: restCompitoInClasse.data ? dayjs(restCompitoInClasse.data) : undefined,
      dataRestituizione: restCompitoInClasse.dataRestituizione ? dayjs(restCompitoInClasse.dataRestituizione) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCompitoInClasse>): HttpResponse<ICompitoInClasse> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCompitoInClasse[]>): HttpResponse<ICompitoInClasse[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
