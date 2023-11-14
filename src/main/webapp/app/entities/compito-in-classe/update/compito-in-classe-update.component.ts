import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAlunno } from 'app/entities/alunno/alunno.model';
import { AlunnoService } from 'app/entities/alunno/service/alunno.service';
import { Materia } from 'app/entities/enumerations/materia.model';
import { CompitoInClasseService } from '../service/compito-in-classe.service';
import { ICompitoInClasse } from '../compito-in-classe.model';
import { CompitoInClasseFormService, CompitoInClasseFormGroup } from './compito-in-classe-form.service';

@Component({
  standalone: true,
  selector: 'jhi-compito-in-classe-update',
  templateUrl: './compito-in-classe-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CompitoInClasseUpdateComponent implements OnInit {
  isSaving = false;
  compitoInClasse: ICompitoInClasse | null = null;
  materiaValues = Object.keys(Materia);

  alunnosSharedCollection: IAlunno[] = [];

  editForm: CompitoInClasseFormGroup = this.compitoInClasseFormService.createCompitoInClasseFormGroup();

  constructor(
    protected compitoInClasseService: CompitoInClasseService,
    protected compitoInClasseFormService: CompitoInClasseFormService,
    protected alunnoService: AlunnoService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAlunno = (o1: IAlunno | null, o2: IAlunno | null): boolean => this.alunnoService.compareAlunno(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compitoInClasse }) => {
      this.compitoInClasse = compitoInClasse;
      if (compitoInClasse) {
        this.updateForm(compitoInClasse);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const compitoInClasse = this.compitoInClasseFormService.getCompitoInClasse(this.editForm);
    if (compitoInClasse.id !== null) {
      this.subscribeToSaveResponse(this.compitoInClasseService.update(compitoInClasse));
    } else {
      this.subscribeToSaveResponse(this.compitoInClasseService.create(compitoInClasse));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompitoInClasse>>): void {
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

  protected updateForm(compitoInClasse: ICompitoInClasse): void {
    this.compitoInClasse = compitoInClasse;
    this.compitoInClasseFormService.resetForm(this.editForm, compitoInClasse);

    this.alunnosSharedCollection = this.alunnoService.addAlunnoToCollectionIfMissing<IAlunno>(
      this.alunnosSharedCollection,
      compitoInClasse.alunnoDiRiferimento,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.alunnoService
      .query()
      .pipe(map((res: HttpResponse<IAlunno[]>) => res.body ?? []))
      .pipe(
        map((alunnos: IAlunno[]) =>
          this.alunnoService.addAlunnoToCollectionIfMissing<IAlunno>(alunnos, this.compitoInClasse?.alunnoDiRiferimento),
        ),
      )
      .subscribe((alunnos: IAlunno[]) => (this.alunnosSharedCollection = alunnos));
  }
}
