import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAlunno, NewAlunno } from '../alunno.model';

export type PartialUpdateAlunno = Partial<IAlunno> & Pick<IAlunno, 'id'>;

type RestOf<T extends IAlunno | NewAlunno> = Omit<T, 'dataNascita'> & {
  dataNascita?: string | null;
};

export type RestAlunno = RestOf<IAlunno>;

export type NewRestAlunno = RestOf<NewAlunno>;

export type PartialUpdateRestAlunno = RestOf<PartialUpdateAlunno>;

export type EntityResponseType = HttpResponse<IAlunno>;
export type EntityArrayResponseType = HttpResponse<IAlunno[]>;

@Injectable({ providedIn: 'root' })
export class AlunnoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/alunnos');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(alunno: NewAlunno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alunno);
    return this.http
      .post<RestAlunno>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(alunno: IAlunno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alunno);
    return this.http
      .put<RestAlunno>(`${this.resourceUrl}/${this.getAlunnoIdentifier(alunno)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(alunno: PartialUpdateAlunno): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(alunno);
    return this.http
      .patch<RestAlunno>(`${this.resourceUrl}/${this.getAlunnoIdentifier(alunno)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAlunno>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAlunno[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAlunnoIdentifier(alunno: Pick<IAlunno, 'id'>): number {
    return alunno.id;
  }

  compareAlunno(o1: Pick<IAlunno, 'id'> | null, o2: Pick<IAlunno, 'id'> | null): boolean {
    return o1 && o2 ? this.getAlunnoIdentifier(o1) === this.getAlunnoIdentifier(o2) : o1 === o2;
  }

  addAlunnoToCollectionIfMissing<Type extends Pick<IAlunno, 'id'>>(
    alunnoCollection: Type[],
    ...alunnosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const alunnos: Type[] = alunnosToCheck.filter(isPresent);
    if (alunnos.length > 0) {
      const alunnoCollectionIdentifiers = alunnoCollection.map(alunnoItem => this.getAlunnoIdentifier(alunnoItem)!);
      const alunnosToAdd = alunnos.filter(alunnoItem => {
        const alunnoIdentifier = this.getAlunnoIdentifier(alunnoItem);
        if (alunnoCollectionIdentifiers.includes(alunnoIdentifier)) {
          return false;
        }
        alunnoCollectionIdentifiers.push(alunnoIdentifier);
        return true;
      });
      return [...alunnosToAdd, ...alunnoCollection];
    }
    return alunnoCollection;
  }

  protected convertDateFromClient<T extends IAlunno | NewAlunno | PartialUpdateAlunno>(alunno: T): RestOf<T> {
    return {
      ...alunno,
      dataNascita: alunno.dataNascita?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAlunno: RestAlunno): IAlunno {
    return {
      ...restAlunno,
      dataNascita: restAlunno.dataNascita ? dayjs(restAlunno.dataNascita) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAlunno>): HttpResponse<IAlunno> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAlunno[]>): HttpResponse<IAlunno[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
