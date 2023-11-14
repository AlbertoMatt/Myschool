import dayjs from 'dayjs/esm';

import { ICompitoInClasse, NewCompitoInClasse } from './compito-in-classe.model';

export const sampleWithRequiredData: ICompitoInClasse = {
  id: 32625,
  data: dayjs('2023-11-12'),
  materia: 'ITALIANO',
  risultatoNumerico: 1.04,
};

export const sampleWithPartialData: ICompitoInClasse = {
  id: 2389,
  data: dayjs('2023-11-12'),
  materia: 'INGLESE',
  risultatoNumerico: 2.91,
};

export const sampleWithFullData: ICompitoInClasse = {
  id: 18305,
  data: dayjs('2023-11-13'),
  materia: 'INGLESE',
  risultatoNumerico: 8.62,
};

export const sampleWithNewData: NewCompitoInClasse = {
  data: dayjs('2023-11-12'),
  materia: 'MATEMATICA',
  risultatoNumerico: 8.67,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
