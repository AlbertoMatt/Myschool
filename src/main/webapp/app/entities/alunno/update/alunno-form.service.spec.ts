import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../alunno.test-samples';

import { AlunnoFormService } from './alunno-form.service';

describe('Alunno Form Service', () => {
  let service: AlunnoFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AlunnoFormService);
  });

  describe('Service methods', () => {
    describe('createAlunnoFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAlunnoFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cognome: expect.any(Object),
            dataNascita: expect.any(Object),
            mediaVoti: expect.any(Object),
            classeDiAppartenenza: expect.any(Object),
          }),
        );
      });

      it('passing IAlunno should create a new form with FormGroup', () => {
        const formGroup = service.createAlunnoFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nome: expect.any(Object),
            cognome: expect.any(Object),
            dataNascita: expect.any(Object),
            mediaVoti: expect.any(Object),
            classeDiAppartenenza: expect.any(Object),
          }),
        );
      });
    });

    describe('getAlunno', () => {
      it('should return NewAlunno for default Alunno initial value', () => {
        const formGroup = service.createAlunnoFormGroup(sampleWithNewData);

        const alunno = service.getAlunno(formGroup) as any;

        expect(alunno).toMatchObject(sampleWithNewData);
      });

      it('should return NewAlunno for empty Alunno initial value', () => {
        const formGroup = service.createAlunnoFormGroup();

        const alunno = service.getAlunno(formGroup) as any;

        expect(alunno).toMatchObject({});
      });

      it('should return IAlunno', () => {
        const formGroup = service.createAlunnoFormGroup(sampleWithRequiredData);

        const alunno = service.getAlunno(formGroup) as any;

        expect(alunno).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAlunno should not enable id FormControl', () => {
        const formGroup = service.createAlunnoFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAlunno should disable id FormControl', () => {
        const formGroup = service.createAlunnoFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
