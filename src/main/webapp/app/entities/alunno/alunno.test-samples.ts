import dayjs from 'dayjs/esm';

import { IAlunno, NewAlunno } from './alunno.model';

export const sampleWithRequiredData: IAlunno = {
  id: 8314,
  nome: 'rudiment boohoo',
  cognome: 'boo when',
  dataNascita: dayjs('2023-11-13'),
};

export const sampleWithPartialData: IAlunno = {
  id: 27420,
  nome: 'blond oh',
  cognome: 'collision',
  dataNascita: dayjs('2023-11-13'),
};

export const sampleWithFullData: IAlunno = {
  id: 16790,
  nome: 'except',
  cognome: 'enchant hm',
  dataNascita: dayjs('2023-11-12'),
  mediaVoti: 23389.45,
};

export const sampleWithNewData: NewAlunno = {
  nome: 'rarely pawn openly',
  cognome: 'indeed regularly dapper',
  dataNascita: dayjs('2023-11-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
