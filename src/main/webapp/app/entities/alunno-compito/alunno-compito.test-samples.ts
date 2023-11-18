import dayjs from 'dayjs/esm';

import { IAlunnoCompito, NewAlunnoCompito } from './alunno-compito.model';

export const sampleWithRequiredData: IAlunnoCompito = {
  id: 22273,
  risultatoNumerico: 2.19,
};

export const sampleWithPartialData: IAlunnoCompito = {
  id: 863,
  risultatoNumerico: 6.78,
  dataRestituizione: dayjs('2023-11-12T13:34'),
};

export const sampleWithFullData: IAlunnoCompito = {
  id: 10415,
  risultatoNumerico: 5.22,
  dataRestituizione: dayjs('2023-11-13T02:51'),
};

export const sampleWithNewData: NewAlunnoCompito = {
  risultatoNumerico: 9.35,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
