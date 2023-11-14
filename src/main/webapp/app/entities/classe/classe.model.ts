export interface IClasse {
  id: number;
  indicazioneNumerica?: number | null;
  sezione?: string | null;
  note?: string | null;
}

export type NewClasse = Omit<IClasse, 'id'> & { id: null };
