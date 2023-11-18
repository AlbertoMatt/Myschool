import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CompitoInClasseService } from '../service/compito-in-classe.service';
import { ICompitoInClasse } from '../compito-in-classe.model';
import { CompitoInClasseFormService } from './compito-in-classe-form.service';

import { CompitoInClasseUpdateComponent } from './compito-in-classe-update.component';

describe('CompitoInClasse Management Update Component', () => {
  let comp: CompitoInClasseUpdateComponent;
  let fixture: ComponentFixture<CompitoInClasseUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let compitoInClasseFormService: CompitoInClasseFormService;
  let compitoInClasseService: CompitoInClasseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CompitoInClasseUpdateComponent],
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
      .overrideTemplate(CompitoInClasseUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CompitoInClasseUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    compitoInClasseFormService = TestBed.inject(CompitoInClasseFormService);
    compitoInClasseService = TestBed.inject(CompitoInClasseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const compitoInClasse: ICompitoInClasse = { id: 456 };

      activatedRoute.data = of({ compitoInClasse });
      comp.ngOnInit();

      expect(comp.compitoInClasse).toEqual(compitoInClasse);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompitoInClasse>>();
      const compitoInClasse = { id: 123 };
      jest.spyOn(compitoInClasseFormService, 'getCompitoInClasse').mockReturnValue(compitoInClasse);
      jest.spyOn(compitoInClasseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compitoInClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compitoInClasse }));
      saveSubject.complete();

      // THEN
      expect(compitoInClasseFormService.getCompitoInClasse).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(compitoInClasseService.update).toHaveBeenCalledWith(expect.objectContaining(compitoInClasse));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompitoInClasse>>();
      const compitoInClasse = { id: 123 };
      jest.spyOn(compitoInClasseFormService, 'getCompitoInClasse').mockReturnValue({ id: null });
      jest.spyOn(compitoInClasseService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compitoInClasse: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: compitoInClasse }));
      saveSubject.complete();

      // THEN
      expect(compitoInClasseFormService.getCompitoInClasse).toHaveBeenCalled();
      expect(compitoInClasseService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICompitoInClasse>>();
      const compitoInClasse = { id: 123 };
      jest.spyOn(compitoInClasseService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ compitoInClasse });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(compitoInClasseService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
