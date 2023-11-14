import dayjs from 'dayjs/esm';

import { IAlunno, NewAlunno } from './alunno.model';

export const sampleWithRequiredData: IAlunno = {
  id: 5372,
  nome: 'meh hydrolyze condemn',
  cognome: 'plus times like',
  dataNascita: dayjs('2023-11-13'),
};

export const sampleWithPartialData: IAlunno = {
  id: 4870,
  nome: 'alongside',
  cognome: 'psst nucleotidase',
  dataNascita: dayjs('2023-11-13'),
};

export const sampleWithFullData: IAlunno = {
  id: 21238,
  nome: 'yum underground',
  cognome: 'cordon though blah',
  dataNascita: dayjs('2023-11-12'),
};

export const sampleWithNewData: NewAlunno = {
  nome: 'indeed regularly dapper',
  cognome: 'boo',
  dataNascita: dayjs('2023-11-13'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
