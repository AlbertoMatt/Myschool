import { IAlunnoCompito, NewAlunnoCompito } from './alunno-compito.model';

export const sampleWithRequiredData: IAlunnoCompito = {
  id: 20279,
  risultatoNumerico: 6.8,
};

export const sampleWithPartialData: IAlunnoCompito = {
  id: 7189,
  risultatoNumerico: 3.32,
};

export const sampleWithFullData: IAlunnoCompito = {
  id: 863,
  risultatoNumerico: 6.78,
};

export const sampleWithNewData: NewAlunnoCompito = {
  risultatoNumerico: 8.78,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
