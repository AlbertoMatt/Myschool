import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
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

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAlunnoCompito | NewAlunnoCompito> = Omit<T, 'dataRestituizione'> & {
  dataRestituizione?: string | null;
};

type AlunnoCompitoFormRawValue = FormValueOf<IAlunnoCompito>;

type NewAlunnoCompitoFormRawValue = FormValueOf<NewAlunnoCompito>;

type AlunnoCompitoFormDefaults = Pick<NewAlunnoCompito, 'id' | 'dataRestituizione'>;

type AlunnoCompitoFormGroupContent = {
  id: FormControl<AlunnoCompitoFormRawValue['id'] | NewAlunnoCompito['id']>;
  risultatoNumerico: FormControl<AlunnoCompitoFormRawValue['risultatoNumerico']>;
  dataRestituizione: FormControl<AlunnoCompitoFormRawValue['dataRestituizione']>;
  alunno: FormControl<AlunnoCompitoFormRawValue['alunno']>;
  compito: FormControl<AlunnoCompitoFormRawValue['compito']>;
};

export type AlunnoCompitoFormGroup = FormGroup<AlunnoCompitoFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AlunnoCompitoFormService {
  createAlunnoCompitoFormGroup(alunnoCompito: AlunnoCompitoFormGroupInput = { id: null }): AlunnoCompitoFormGroup {
    const alunnoCompitoRawValue = this.convertAlunnoCompitoToAlunnoCompitoRawValue({
      ...this.getFormDefaults(),
      ...alunnoCompito,
    });
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
      dataRestituizione: new FormControl(alunnoCompitoRawValue.dataRestituizione),
      alunno: new FormControl(alunnoCompitoRawValue.alunno, {
        validators: [Validators.required],
      }),
      compito: new FormControl(alunnoCompitoRawValue.compito, {
        validators: [Validators.required],
      }),
    });
  }

  getAlunnoCompito(form: AlunnoCompitoFormGroup): IAlunnoCompito | NewAlunnoCompito {
    return this.convertAlunnoCompitoRawValueToAlunnoCompito(form.getRawValue() as AlunnoCompitoFormRawValue | NewAlunnoCompitoFormRawValue);
  }

  resetForm(form: AlunnoCompitoFormGroup, alunnoCompito: AlunnoCompitoFormGroupInput): void {
    const alunnoCompitoRawValue = this.convertAlunnoCompitoToAlunnoCompitoRawValue({ ...this.getFormDefaults(), ...alunnoCompito });
    form.reset(
      {
        ...alunnoCompitoRawValue,
        id: { value: alunnoCompitoRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AlunnoCompitoFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dataRestituizione: currentTime,
    };
  }

  private convertAlunnoCompitoRawValueToAlunnoCompito(
    rawAlunnoCompito: AlunnoCompitoFormRawValue | NewAlunnoCompitoFormRawValue,
  ): IAlunnoCompito | NewAlunnoCompito {
    return {
      ...rawAlunnoCompito,
      dataRestituizione: dayjs(rawAlunnoCompito.dataRestituizione, DATE_TIME_FORMAT),
    };
  }

  private convertAlunnoCompitoToAlunnoCompitoRawValue(
    alunnoCompito: IAlunnoCompito | (Partial<NewAlunnoCompito> & AlunnoCompitoFormDefaults),
  ): AlunnoCompitoFormRawValue | PartialWithRequiredKeyOf<NewAlunnoCompitoFormRawValue> {
    return {
      ...alunnoCompito,
      dataRestituizione: alunnoCompito.dataRestituizione ? alunnoCompito.dataRestituizione.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
