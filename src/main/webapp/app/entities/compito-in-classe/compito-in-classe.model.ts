import dayjs from 'dayjs/esm';
import { Materia } from 'app/entities/enumerations/materia.model';

export interface ICompitoInClasse {
  id: number;
  materia?: keyof typeof Materia | null;
  data?: dayjs.Dayjs | null;
  dataRestituizione?: dayjs.Dayjs | null;
}

export type NewCompitoInClasse = Omit<ICompitoInClasse, 'id'> & { id: null };
