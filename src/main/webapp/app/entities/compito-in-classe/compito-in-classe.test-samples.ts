import dayjs from 'dayjs/esm';

import { ICompitoInClasse, NewCompitoInClasse } from './compito-in-classe.model';

export const sampleWithRequiredData: ICompitoInClasse = {
  id: 5419,
  materia: 'INGLESE',
  data: dayjs('2023-11-13'),
};

export const sampleWithPartialData: ICompitoInClasse = {
  id: 20239,
  materia: 'ITALIANO',
  data: dayjs('2023-11-12'),
};

export const sampleWithFullData: ICompitoInClasse = {
  id: 32395,
  materia: 'ITALIANO',
  data: dayjs('2023-11-12'),
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
