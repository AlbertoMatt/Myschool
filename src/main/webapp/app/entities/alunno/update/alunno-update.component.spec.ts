import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IClasse } from 'app/entities/classe/classe.model';
import { ClasseService } from 'app/entities/classe/service/classe.service';
import { AlunnoService } from '../service/alunno.service';
import { IAlunno } from '../alunno.model';
import { AlunnoFormService } from './alunno-form.service';

import { AlunnoUpdateComponent } from './alunno-update.component';

describe('Alunno Management Update Component', () => {
  let comp: AlunnoUpdateComponent;
  let fixture: ComponentFixture<AlunnoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let alunnoFormService: AlunnoFormService;
  let alunnoService: AlunnoService;
  let classeService: ClasseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AlunnoUpdateComponent],
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
      .overrideTemplate(AlunnoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AlunnoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    alunnoFormService = TestBed.inject(AlunnoFormService);
    alunnoService = TestBed.inject(AlunnoService);
    classeService = TestBed.inject(ClasseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Classe query and add missing value', () => {
      const alunno: IAlunno = { id: 456 };
      const classeDiAppartenenza: IClasse = { id: 29098 };
      alunno.classeDiAppartenenza = classeDiAppartenenza;

      const classeCollection: IClasse[] = [{ id: 21936 }];
      jest.spyOn(classeService, 'query').mockReturnValue(of(new HttpResponse({ body: classeCollection })));
      const additionalClasses = [classeDiAppartenenza];
      const expectedCollection: IClasse[] = [...additionalClasses, ...classeCollection];
      jest.spyOn(classeService, 'addClasseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ alunno });
      comp.ngOnInit();

      expect(classeService.query).toHaveBeenCalled();
      expect(classeService.addClasseToCollectionIfMissing).toHaveBeenCalledWith(
        classeCollection,
        ...additionalClasses.map(expect.objectContaining),
      );
      expect(comp.classesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const alunno: IAlunno = { id: 456 };
      const classeDiAppartenenza: IClasse = { id: 8038 };
      alunno.classeDiAppartenenza = classeDiAppartenenza;

      activatedRoute.data = of({ alunno });
      comp.ngOnInit();

      expect(comp.classesSharedCollection).toContain(classeDiAppartenenza);
      expect(comp.alunno).toEqual(alunno);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlunno>>();
      const alunno = { id: 123 };
      jest.spyOn(alunnoFormService, 'getAlunno').mockReturnValue(alunno);
      jest.spyOn(alunnoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alunno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alunno }));
      saveSubject.complete();

      // THEN
      expect(alunnoFormService.getAlunno).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(alunnoService.update).toHaveBeenCalledWith(expect.objectContaining(alunno));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlunno>>();
      const alunno = { id: 123 };
      jest.spyOn(alunnoFormService, 'getAlunno').mockReturnValue({ id: null });
      jest.spyOn(alunnoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alunno: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: alunno }));
      saveSubject.complete();

      // THEN
      expect(alunnoFormService.getAlunno).toHaveBeenCalled();
      expect(alunnoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAlunno>>();
      const alunno = { id: 123 };
      jest.spyOn(alunnoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ alunno });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(alunnoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClasse', () => {
      it('Should forward to classeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(classeService, 'compareClasse');
        comp.compareClasse(entity, entity2);
        expect(classeService.compareClasse).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
