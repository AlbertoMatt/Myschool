import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAlunnoCompito, NewAlunnoCompito } from '../alunno-compito.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAlunnoCompito for edit and NewAlunnoCompitoFormGroupInput for create.
 */
type AlunnoCompitoFormGroupInput = IAlunnoCompito | PartialWithRequiredKeyOf<NewAlunnoCompito>;

type AlunnoCompitoFormDefaults = Pick<NewAlunnoCompito, 'id'>;

type AlunnoCompitoFormGroupContent = {
  id: FormControl<IAlunnoCompito['id'] | NewAlunnoCompito['id']>;
  risultatoNumerico: FormControl<IAlunnoCompito['risultatoNumerico']>;
  alunno: FormControl<IAlunnoCompito['alunno']>;
  compito: FormControl<IAlunnoCompito['compito']>;
};

export type AlunnoCompitoFormGroup = FormGroup<AlunnoCompitoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlunnoCompitoFormService {
  createAlunnoCompitoFormGroup(alunnoCompito: AlunnoCompitoFormGroupInput = { id: null }): AlunnoCompitoFormGroup {
    const alunnoCompitoRawValue = {
      ...this.getFormDefaults(),
      ...alunnoCompito,
    };
    return new FormGroup<AlunnoCompitoFormGroupContent>({
      id: new FormControl(
        { value: alunnoCompitoRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      risultatoNumerico: new FormControl(alunnoCompitoRawValue.risultatoNumerico, {
        validators: [Validators.required, Validators.min(0), Validators.max(10)],
      }),
      alunno: new FormControl(alunnoCompitoRawValue.alunno, {
        validators: [Validators.required],
      }),
      compito: new FormControl(alunnoCompitoRawValue.compito, {
        validators: [Validators.required],
      }),
    });
  }

  getAlunnoCompito(form: AlunnoCompitoFormGroup): IAlunnoCompito | NewAlunnoCompito {
    return form.getRawValue() as IAlunnoCompito | NewAlunnoCompito;
  }

  resetForm(form: AlunnoCompitoFormGroup, alunnoCompito: AlunnoCompitoFormGroupInput): void {
    const alunnoCompitoRawValue = { ...this.getFormDefaults(), ...alunnoCompito };
    form.reset(
      {
        ...alunnoCompitoRawValue,
        id: { value: alunnoCompitoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlunnoCompitoFormDefaults {
    return {
      id: null,
    };
  }
}
