import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ICompitoInClasse | NewCompitoInClasse> = Omit<T, 'dataRestituizione'> & {
  dataRestituizione?: string | null;
};

type CompitoInClasseFormRawValue = FormValueOf<ICompitoInClasse>;

type NewCompitoInClasseFormRawValue = FormValueOf<NewCompitoInClasse>;

type CompitoInClasseFormDefaults = Pick<NewCompitoInClasse, 'id' | 'dataRestituizione'>;

type CompitoInClasseFormGroupContent = {
  id: FormControl<CompitoInClasseFormRawValue['id'] | NewCompitoInClasse['id']>;
  materia: FormControl<CompitoInClasseFormRawValue['materia']>;
  data: FormControl<CompitoInClasseFormRawValue['data']>;
  dataRestituizione: FormControl<CompitoInClasseFormRawValue['dataRestituizione']>;
};

export type CompitoInClasseFormGroup = FormGroup<CompitoInClasseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CompitoInClasseFormService {
  createCompitoInClasseFormGroup(compitoInClasse: CompitoInClasseFormGroupInput = { id: null }): CompitoInClasseFormGroup {
    const compitoInClasseRawValue = this.convertCompitoInClasseToCompitoInClasseRawValue({
      ...this.getFormDefaults(),
      ...compitoInClasse,
    });
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
      dataRestituizione: new FormControl(compitoInClasseRawValue.dataRestituizione),
    });
  }

  getCompitoInClasse(form: CompitoInClasseFormGroup): ICompitoInClasse | NewCompitoInClasse {
    return this.convertCompitoInClasseRawValueToCompitoInClasse(
      form.getRawValue() as CompitoInClasseFormRawValue | NewCompitoInClasseFormRawValue,
    );
  }

  resetForm(form: CompitoInClasseFormGroup, compitoInClasse: CompitoInClasseFormGroupInput): void {
    const compitoInClasseRawValue = this.convertCompitoInClasseToCompitoInClasseRawValue({ ...this.getFormDefaults(), ...compitoInClasse });
    form.reset(
      {
        ...compitoInClasseRawValue,
        id: { value: compitoInClasseRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CompitoInClasseFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataRestituizione: currentTime,
    };
  }

  private convertCompitoInClasseRawValueToCompitoInClasse(
    rawCompitoInClasse: CompitoInClasseFormRawValue | NewCompitoInClasseFormRawValue,
  ): ICompitoInClasse | NewCompitoInClasse {
    return {
      ...rawCompitoInClasse,
      dataRestituizione: dayjs(rawCompitoInClasse.dataRestituizione, DATE_TIME_FORMAT),
    };
  }

  private convertCompitoInClasseToCompitoInClasseRawValue(
    compitoInClasse: ICompitoInClasse | (Partial<NewCompitoInClasse> & CompitoInClasseFormDefaults),
  ): CompitoInClasseFormRawValue | PartialWithRequiredKeyOf<NewCompitoInClasseFormRawValue> {
    return {
      ...compitoInClasse,
      dataRestituizione: compitoInClasse.dataRestituizione ? compitoInClasse.dataRestituizione.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
