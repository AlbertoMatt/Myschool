import dayjs from 'dayjs/esm';

import { IAlunno, NewAlunno } from './alunno.model';

export const sampleWithRequiredData: IAlunno = {
  id: 11357,
  nome: 'psst',
  cognome: 'boohoo franchise woot',
  dataNascita: dayjs('2023-11-12'),
};

export const sampleWithPartialData: IAlunno = {
  id: 6094,
  nome: 'ha whoever',
  cognome: 'almanac',
  dataNascita: dayjs('2023-11-13'),
};

export const sampleWithFullData: IAlunno = {
  id: 5895,
  nome: 'proliferate',
  cognome: 'intently which',
  dataNascita: dayjs('2023-11-13'),
};

export const sampleWithNewData: NewAlunno = {
  nome: 'till excluding',
  cognome: 'amongst bath',
  dataNascita: dayjs('2023-11-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
