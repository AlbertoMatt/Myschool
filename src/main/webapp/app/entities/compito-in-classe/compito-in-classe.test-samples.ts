import dayjs from 'dayjs/esm';

import { ICompitoInClasse, NewCompitoInClasse } from './compito-in-classe.model';

export const sampleWithRequiredData: ICompitoInClasse = {
  id: 3416,
  materia: 'STORIA',
  data: dayjs('2023-11-12'),
};

export const sampleWithPartialData: ICompitoInClasse = {
  id: 9543,
  materia: 'INGLESE',
  data: dayjs('2023-11-13'),
};

export const sampleWithFullData: ICompitoInClasse = {
  id: 19218,
  materia: 'MATEMATICA',
  data: dayjs('2023-11-12'),
  dataRestituizione: dayjs('2023-11-12T12:31'),
};

export const sampleWithNewData: NewCompitoInClasse = {
  materia: 'MATEMATICA',
  data: dayjs('2023-11-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
