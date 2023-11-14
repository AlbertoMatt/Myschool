import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../compito-in-classe.test-samples';

import { CompitoInClasseFormService } from './compito-in-classe-form.service';

describe('CompitoInClasse Form Service', () => {
  let service: CompitoInClasseFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CompitoInClasseFormService);
  });

  describe('Service methods', () => {
    describe('createCompitoInClasseFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCompitoInClasseFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            data: expect.any(Object),
            materia: expect.any(Object),
            risultatoNumerico: expect.any(Object),
            alunnoDiRiferimento: expect.any(Object),
          }),
        );
      });

      it('passing ICompitoInClasse should create a new form with FormGroup', () => {
        const formGroup = service.createCompitoInClasseFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            data: expect.any(Object),
            materia: expect.any(Object),
            risultatoNumerico: expect.any(Object),
            alunnoDiRiferimento: expect.any(Object),
          }),
        );
      });
    });

    describe('getCompitoInClasse', () => {
      it('should return NewCompitoInClasse for default CompitoInClasse initial value', () => {
        const formGroup = service.createCompitoInClasseFormGroup(sampleWithNewData);

        const compitoInClasse = service.getCompitoInClasse(formGroup) as any;

        expect(compitoInClasse).toMatchObject(sampleWithNewData);
      });

      it('should return NewCompitoInClasse for empty CompitoInClasse initial value', () => {
        const formGroup = service.createCompitoInClasseFormGroup();

        const compitoInClasse = service.getCompitoInClasse(formGroup) as any;

        expect(compitoInClasse).toMatchObject({});
      });

      it('should return ICompitoInClasse', () => {
        const formGroup = service.createCompitoInClasseFormGroup(sampleWithRequiredData);

        const compitoInClasse = service.getCompitoInClasse(formGroup) as any;

        expect(compitoInClasse).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICompitoInClasse should not enable id FormControl', () => {
        const formGroup = service.createCompitoInClasseFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCompitoInClasse should disable id FormControl', () => {
        const formGroup = service.createCompitoInClasseFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
