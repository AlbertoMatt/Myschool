import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { Materia } from 'app/entities/enumerations/materia.model';
import { ICompitoInClasse } from '../compito-in-classe.model';
import { CompitoInClasseService } from '../service/compito-in-classe.service';
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

  editForm: CompitoInClasseFormGroup = this.compitoInClasseFormService.createCompitoInClasseFormGroup();

  constructor(
    protected compitoInClasseService: CompitoInClasseService,
    protected compitoInClasseFormService: CompitoInClasseFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ compitoInClasse }) => {
      this.compitoInClasse = compitoInClasse;
      if (compitoInClasse) {
        this.updateForm(compitoInClasse);
      }
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
  }
}
