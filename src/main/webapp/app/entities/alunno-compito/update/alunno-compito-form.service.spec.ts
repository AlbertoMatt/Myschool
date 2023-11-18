import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../alunno-compito.test-samples';

import { AlunnoCompitoFormService } from './alunno-compito-form.service';

describe('AlunnoCompito Form Service', () => {
  let service: AlunnoCompitoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlunnoCompitoFormService);
  });

  describe('Service methods', () => {
    describe('createAlunnoCompitoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlunnoCompitoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            risultatoNumerico: expect.any(Object),
            dataRestituizione: expect.any(Object),
            alunno: expect.any(Object),
            compito: expect.any(Object),
          }),
        );
      });

      it('passing IAlunnoCompito should create a new form with FormGroup', () => {
        const formGroup = service.createAlunnoCompitoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            risultatoNumerico: expect.any(Object),
            dataRestituizione: expect.any(Object),
            alunno: expect.any(Object),
            compito: expect.any(Object),
          }),
        );
      });
    });

    describe('getAlunnoCompito', () => {
      it('should return NewAlunnoCompito for default AlunnoCompito initial value', () => {
        const formGroup = service.createAlunnoCompitoFormGroup(sampleWithNewData);

        const alunnoCompito = service.getAlunnoCompito(formGroup) as any;

        expect(alunnoCompito).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlunnoCompito for empty AlunnoCompito initial value', () => {
        const formGroup = service.createAlunnoCompitoFormGroup();

        const alunnoCompito = service.getAlunnoCompito(formGroup) as any;

        expect(alunnoCompito).toMatchObject({});
      });

      it('should return IAlunnoCompito', () => {
        const formGroup = service.createAlunnoCompitoFormGroup(sampleWithRequiredData);

        const alunnoCompito = service.getAlunnoCompito(formGroup) as any;

        expect(alunnoCompito).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlunnoCompito should not enable id FormControl', () => {
        const formGroup = service.createAlunnoCompitoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlunnoCompito should disable id FormControl', () => {
        const formGroup = service.createAlunnoCompitoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
