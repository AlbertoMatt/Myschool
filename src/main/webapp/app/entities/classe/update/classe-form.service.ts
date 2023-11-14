import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IClasse, NewClasse } from '../classe.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClasse for edit and NewClasseFormGroupInput for create.
 */
type ClasseFormGroupInput = IClasse | PartialWithRequiredKeyOf<NewClasse>;

type ClasseFormDefaults = Pick<NewClasse, 'id'>;

type ClasseFormGroupContent = {
  id: FormControl<IClasse['id'] | NewClasse['id']>;
  indicazioneNumerica: FormControl<IClasse['indicazioneNumerica']>;
  sezione: FormControl<IClasse['sezione']>;
  note: FormControl<IClasse['note']>;
};

export type ClasseFormGroup = FormGroup<ClasseFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClasseFormService {
  createClasseFormGroup(classe: ClasseFormGroupInput = { id: null }): ClasseFormGroup {
    const classeRawValue = {
      ...this.getFormDefaults(),
      ...classe,
    };
    return new FormGroup<ClasseFormGroupContent>({
      id: new FormControl(
        { value: classeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      indicazioneNumerica: new FormControl(classeRawValue.indicazioneNumerica, {
        validators: [Validators.required, Validators.min(1), Validators.max(5)],
      }),
      sezione: new FormControl(classeRawValue.sezione, {
        validators: [Validators.required, Validators.maxLength(1), Validators.pattern('^[A-E]')],
      }),
      note: new FormControl(classeRawValue.note),
    });
  }

  getClasse(form: ClasseFormGroup): IClasse | NewClasse {
    return form.getRawValue() as IClasse | NewClasse;
  }

  resetForm(form: ClasseFormGroup, classe: ClasseFormGroupInput): void {
    const classeRawValue = { ...this.getFormDefaults(), ...classe };
    form.reset(
      {
        ...classeRawValue,
        id: { value: classeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClasseFormDefaults {
    return {
      id: null,
    };
  }
}
