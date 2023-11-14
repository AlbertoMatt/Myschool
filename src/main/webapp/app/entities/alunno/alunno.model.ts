import dayjs from 'dayjs/esm';
import { IClasse } from 'app/entities/classe/classe.model';

export interface IAlunno {
  id: number;
  nome?: string | null;
  cognome?: string | null;
  dataNascita?: dayjs.Dayjs | null;
  classeDiAppartenenza?: Pick<IClasse, 'id'> | null;
}

export type NewAlunno = Omit<IAlunno, 'id'> & { id: null };
