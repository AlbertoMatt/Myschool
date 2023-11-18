import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAlunno } from 'app/entities/alunno/alunno.model';
import { AlunnoService } from 'app/entities/alunno/service/alunno.service';
import { ICompitoInClasse } from 'app/entities/compito-in-classe/compito-in-classe.model';
import { CompitoInClasseService } from 'app/entities/compito-in-classe/service/compito-in-classe.service';
import { AlunnoCompitoService } from '../service/alunno-compito.service';
import { IAlunnoCompito } from '../alunno-compito.model';
import { AlunnoCompitoFormService, AlunnoCompitoFormGroup } from './alunno-compito-form.service';

@Component({
  standalone: true,
  selector: 'jhi-alunno-compito-update',
  templateUrl: './alunno-compito-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlunnoCompitoUpdateComponent implements OnInit {
  isSaving = false;
  alunnoCompito: IAlunnoCompito | null = null;

  alunnosSharedCollection: IAlunno[] = [];
  compitoInClassesSharedCollection: ICompitoInClasse[] = [];

  editForm: AlunnoCompitoFormGroup = this.alunnoCompitoFormService.createAlunnoCompitoFormGroup();

  constructor(
    protected alunnoCompitoService: AlunnoCompitoService,
    protected alunnoCompitoFormService: AlunnoCompitoFormService,
    protected alunnoService: AlunnoService,
    protected compitoInClasseService: CompitoInClasseService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAlunno = (o1: IAlunno | null, o2: IAlunno | null): boolean => this.alunnoService.compareAlunno(o1, o2);

  compareCompitoInClasse = (o1: ICompitoInClasse | null, o2: ICompitoInClasse | null): boolean =>
    this.compitoInClasseService.compareCompitoInClasse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alunnoCompito }) => {
      this.alunnoCompito = alunnoCompito;
      if (alunnoCompito) {
        this.updateForm(alunnoCompito);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alunnoCompito = this.alunnoCompitoFormService.getAlunnoCompito(this.editForm);
    if (alunnoCompito.id !== null) {
      this.subscribeToSaveResponse(this.alunnoCompitoService.update(alunnoCompito));
    } else {
      this.subscribeToSaveResponse(this.alunnoCompitoService.create(alunnoCompito));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlunnoCompito>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(alunnoCompito: IAlunnoCompito): void {
    this.alunnoCompito = alunnoCompito;
    this.alunnoCompitoFormService.resetForm(this.editForm, alunnoCompito);

    this.alunnosSharedCollection = this.alunnoService.addAlunnoToCollectionIfMissing<IAlunno>(
      this.alunnosSharedCollection,
      alunnoCompito.alunno,
    );
    this.compitoInClassesSharedCollection = this.compitoInClasseService.addCompitoInClasseToCollectionIfMissing<ICompitoInClasse>(
      this.compitoInClassesSharedCollection,
      alunnoCompito.compito,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alunnoService
      .query()
      .pipe(map((res: HttpResponse<IAlunno[]>) => res.body ?? []))
      .pipe(map((alunnos: IAlunno[]) => this.alunnoService.addAlunnoToCollectionIfMissing<IAlunno>(alunnos, this.alunnoCompito?.alunno)))
      .subscribe((alunnos: IAlunno[]) => (this.alunnosSharedCollection = alunnos));

    this.compitoInClasseService
      .query()
      .pipe(map((res: HttpResponse<ICompitoInClasse[]>) => res.body ?? []))
      .pipe(
        map((compitoInClasses: ICompitoInClasse[]) =>
          this.compitoInClasseService.addCompitoInClasseToCollectionIfMissing<ICompitoInClasse>(
            compitoInClasses,
            this.alunnoCompito?.compito,
          ),
        ),
      )
      .subscribe((compitoInClasses: ICompitoInClasse[]) => (this.compitoInClassesSharedCollection = compitoInClasses));
  }
}
