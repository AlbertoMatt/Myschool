import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAlunno, NewAlunno } from '../alunno.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlunno for edit and NewAlunnoFormGroupInput for create.
 */
type AlunnoFormGroupInput = IAlunno | PartialWithRequiredKeyOf<NewAlunno>;

type AlunnoFormDefaults = Pick<NewAlunno, 'id'>;

type AlunnoFormGroupContent = {
  id: FormControl<IAlunno['id'] | NewAlunno['id']>;
  nome: FormControl<IAlunno['nome']>;
  cognome: FormControl<IAlunno['cognome']>;
  dataNascita: FormControl<IAlunno['dataNascita']>;
  mediaVoti: FormControl<IAlunno['mediaVoti']>;
  classeDiAppartenenza: FormControl<IAlunno['classeDiAppartenenza']>;
};

export type AlunnoFormGroup = FormGroup<AlunnoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlunnoFormService {
  createAlunnoFormGroup(alunno: AlunnoFormGroupInput = { id: null }): AlunnoFormGroup {
    const alunnoRawValue = {
      ...this.getFormDefaults(),
      ...alunno,
    };
    return new FormGroup<AlunnoFormGroupContent>({
      id: new FormControl(
        { value: alunnoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      nome: new FormControl(alunnoRawValue.nome, {
        validators: [Validators.required],
      }),
      cognome: new FormControl(alunnoRawValue.cognome, {
        validators: [Validators.required],
      }),
      dataNascita: new FormControl(alunnoRawValue.dataNascita, {
        validators: [Validators.required],
      }),
      mediaVoti: new FormControl(alunnoRawValue.mediaVoti),
      classeDiAppartenenza: new FormControl(alunnoRawValue.classeDiAppartenenza, {
        validators: [Validators.required],
      }),
    });
  }

  getAlunno(form: AlunnoFormGroup): IAlunno | NewAlunno {
    return form.getRawValue() as IAlunno | NewAlunno;
  }

  resetForm(form: AlunnoFormGroup, alunno: AlunnoFormGroupInput): void {
    const alunnoRawValue = { ...this.getFormDefaults(), ...alunno };
    form.reset(
      {
        ...alunnoRawValue,
        id: { value: alunnoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlunnoFormDefaults {
    return {
      id: null,
    };
  }
}
