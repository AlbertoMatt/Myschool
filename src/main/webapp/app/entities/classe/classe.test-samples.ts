import { IClasse, NewClasse } from './classe.model';

export const sampleWithRequiredData: IClasse = {
  id: 5267,
  indicazioneNumerica: 3,
  sezione: 'A',
};

export const sampleWithPartialData: IClasse = {
  id: 21188,
  indicazioneNumerica: 4,
  sezione: 'D',
  note: 'swiftly',
};

export const sampleWithFullData: IClasse = {
  id: 13372,
  indicazioneNumerica: 1,
  sezione: 'C',
  note: 'ew ack how',
};

export const sampleWithNewData: NewClasse = {
  indicazioneNumerica: 2,
  sezione: 'B',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
