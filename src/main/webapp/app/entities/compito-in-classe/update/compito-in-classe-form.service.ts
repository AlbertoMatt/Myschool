import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICompitoInClasse, NewCompitoInClasse } from '../compito-in-classe.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICompitoInClasse for edit and NewCompitoInClasseFormGroupInput for create.
 */
type CompitoInClasseFormGroupInput = ICompitoInClasse | PartialWithRequiredKeyOf<NewCompitoInClasse>;

type CompitoInClasseFormDefaults = Pick<NewCompitoInClasse, 'id'>;

type CompitoInClasseFormGroupContent = {
  id: FormControl<ICompitoInClasse['id'] | NewCompitoInClasse['id']>;
  materia: FormControl<ICompitoInClasse['materia']>;
  data: FormControl<ICompitoInClasse['data']>;
};

export type CompitoInClasseFormGroup = FormGroup<CompitoInClasseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompitoInClasseFormService {
  createCompitoInClasseFormGroup(compitoInClasse: CompitoInClasseFormGroupInput = { id: null }): CompitoInClasseFormGroup {
    const compitoInClasseRawValue = {
      ...this.getFormDefaults(),
      ...compitoInClasse,
    };
    return new FormGroup<CompitoInClasseFormGroupContent>({
      id: new FormControl(
        { value: compitoInClasseRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      materia: new FormControl(compitoInClasseRawValue.materia, {
        validators: [Validators.required],
      }),
      data: new FormControl(compitoInClasseRawValue.data, {
        validators: [Validators.required],
      }),
    });
  }

  getCompitoInClasse(form: CompitoInClasseFormGroup): ICompitoInClasse | NewCompitoInClasse {
    return form.getRawValue() as ICompitoInClasse | NewCompitoInClasse;
  }

  resetForm(form: CompitoInClasseFormGroup, compitoInClasse: CompitoInClasseFormGroupInput): void {
    const compitoInClasseRawValue = { ...this.getFormDefaults(), ...compitoInClasse };
    form.reset(
      {
        ...compitoInClasseRawValue,
        id: { value: compitoInClasseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompitoInClasseFormDefaults {
    return {
      id: null,
    };
  }
}
