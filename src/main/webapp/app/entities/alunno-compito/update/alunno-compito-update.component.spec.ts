import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAlunno } from 'app/entities/alunno/alunno.model';
import { AlunnoService } from 'app/entities/alunno/service/alunno.service';
import { ICompitoInClasse } from 'app/entities/compito-in-classe/compito-in-classe.model';
import { CompitoInClasseService } from 'app/entities/compito-in-classe/service/compito-in-classe.service';
import { IAlunnoCompito } from '../alunno-compito.model';
import { AlunnoCompitoService } from '../service/alunno-compito.service';
import { AlunnoCompitoFormService } from './alunno-compito-form.service';

import { AlunnoCompitoUpdateComponent } from './alunno-compito-update.component';

describe('AlunnoCompito Management Update Component', () => {
  let comp: AlunnoCompitoUpdateComponent;
  let fixture: ComponentFixture<AlunnoCompitoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alunnoCompitoFormService: AlunnoCompitoFormService;
  let alunnoCompitoService: AlunnoCompitoService;
  let alunnoService: AlunnoService;
  let compitoInClasseService: CompitoInClasseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AlunnoCompitoUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AlunnoCompitoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlunnoCompitoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alunnoCompitoFormService = TestBed.inject(AlunnoCompitoFormService);
    alunnoCompitoService = TestBed.inject(AlunnoCompitoService);
    alunnoService = TestBed.inject(AlunnoService);
    compitoInClasseService = TestBed.inject(CompitoInClasseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Alunno query and add missing value', () => {
      const alunnoCompito: IAlunnoCompito = { id: 456 };
      const alunno: IAlunno = { id: 5372 };
      alunnoCompito.alunno = alunno;

      const alunnoCollection: IAlunno[] = [{ id: 24971 }];
      jest.spyOn(alunnoService, 'query').mockReturnValue(of(new HttpResponse({ body: alunnoCollection })));
      const additionalAlunnos = [alunno];
      const expectedCollection: IAlunno[] = [...additionalAlunnos, ...alunnoCollection];
      jest.spyOn(alunnoService, 'addAlunnoToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alunnoCompito });
      comp.ngOnInit();

      expect(alunnoService.query).toHaveBeenCalled();
      expect(alunnoService.addAlunnoToCollectionIfMissing).toHaveBeenCalledWith(
        alunnoCollection,
        ...additionalAlunnos.map(expect.objectContaining),
      );
      expect(comp.alunnosSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CompitoInClasse query and add missing value', () => {
      const alunnoCompito: IAlunnoCompito = { id: 456 };
      const compito: ICompitoInClasse = { id: 32625 };
      alunnoCompito.compito = compito;

      const compitoInClasseCollection: ICompitoInClasse[] = [{ id: 28611 }];
      jest.spyOn(compitoInClasseService, 'query').mockReturnValue(of(new HttpResponse({ body: compitoInClasseCollection })));
      const additionalCompitoInClasses = [compito];
      const expectedCollection: ICompitoInClasse[] = [...additionalCompitoInClasses, ...compitoInClasseCollection];
      jest.spyOn(compitoInClasseService, 'addCompitoInClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alunnoCompito });
      comp.ngOnInit();

      expect(compitoInClasseService.query).toHaveBeenCalled();
      expect(compitoInClasseService.addCompitoInClasseToCollectionIfMissing).toHaveBeenCalledWith(
        compitoInClasseCollection,
        ...additionalCompitoInClasses.map(expect.objectContaining),
      );
      expect(comp.compitoInClassesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const alunnoCompito: IAlunnoCompito = { id: 456 };
      const alunno: IAlunno = { id: 21808 };
      alunnoCompito.alunno = alunno;
      const compito: ICompitoInClasse = { id: 11740 };
      alunnoCompito.compito = compito;

      activatedRoute.data = of({ alunnoCompito });
      comp.ngOnInit();

      expect(comp.alunnosSharedCollection).toContain(alunno);
      expect(comp.compitoInClassesSharedCollection).toContain(compito);
      expect(comp.alunnoCompito).toEqual(alunnoCompito);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlunnoCompito>>();
      const alunnoCompito = { id: 123 };
      jest.spyOn(alunnoCompitoFormService, 'getAlunnoCompito').mockReturnValue(alunnoCompito);
      jest.spyOn(alunnoCompitoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alunnoCompito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alunnoCompito }));
      saveSubject.complete();

      // THEN
      expect(alunnoCompitoFormService.getAlunnoCompito).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alunnoCompitoService.update).toHaveBeenCalledWith(expect.objectContaining(alunnoCompito));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlunnoCompito>>();
      const alunnoCompito = { id: 123 };
      jest.spyOn(alunnoCompitoFormService, 'getAlunnoCompito').mockReturnValue({ id: null });
      jest.spyOn(alunnoCompitoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alunnoCompito: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alunnoCompito }));
      saveSubject.complete();

      // THEN
      expect(alunnoCompitoFormService.getAlunnoCompito).toHaveBeenCalled();
      expect(alunnoCompitoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlunnoCompito>>();
      const alunnoCompito = { id: 123 };
      jest.spyOn(alunnoCompitoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alunnoCompito });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alunnoCompitoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAlunno', () => {
      it('Should forward to alunnoService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(alunnoService, 'compareAlunno');
        comp.compareAlunno(entity, entity2);
        expect(alunnoService.compareAlunno).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCompitoInClasse', () => {
      it('Should forward to compitoInClasseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(compitoInClasseService, 'compareCompitoInClasse');
        comp.compareCompitoInClasse(entity, entity2);
        expect(compitoInClasseService.compareCompitoInClasse).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
