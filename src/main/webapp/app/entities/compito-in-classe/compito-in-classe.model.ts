import dayjs from 'dayjs/esm';
import { IAlunno } from 'app/entities/alunno/alunno.model';
import { Materia } from 'app/entities/enumerations/materia.model';

export interface ICompitoInClasse {
  id: number;
  data?: dayjs.Dayjs | null;
  materia?: keyof typeof Materia | null;
  risultatoNumerico?: number | null;
  alunnoDiRiferimento?: Pick<IAlunno, 'id'> | null;
}

export type NewCompitoInClasse = Omit<ICompitoInClasse, 'id'> & { id: null };
