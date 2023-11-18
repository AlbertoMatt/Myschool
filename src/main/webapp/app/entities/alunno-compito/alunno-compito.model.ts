import { IAlunno } from 'app/entities/alunno/alunno.model';
import { ICompitoInClasse } from 'app/entities/compito-in-classe/compito-in-classe.model';

export interface IAlunnoCompito {
  id: number;
  risultatoNumerico?: number | null;
  alunno?: Pick<IAlunno, 'id'> | null;
  compito?: Pick<ICompitoInClasse, 'id'> | null;
}

export type NewAlunnoCompito = Omit<IAlunnoCompito, 'id'> & { id: null };
