import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClasse } from '../classe.model';
import { ClasseService } from '../service/classe.service';
import { ClasseFormService, ClasseFormGroup } from './classe-form.service';

@Component({
  standalone: true,
  selector: 'jhi-classe-update',
  templateUrl: './classe-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClasseUpdateComponent implements OnInit {
  isSaving = false;
  classe: IClasse | null = null;

  editForm: ClasseFormGroup = this.classeFormService.createClasseFormGroup();

  constructor(
    protected classeService: ClasseService,
    protected classeFormService: ClasseFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classe }) => {
      this.classe = classe;
      if (classe) {
        this.updateForm(classe);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classe = this.classeFormService.getClasse(this.editForm);
    if (classe.id !== null) {
      this.subscribeToSaveResponse(this.classeService.update(classe));
    } else {
      this.subscribeToSaveResponse(this.classeService.create(classe));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClasse>>): void {
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

  protected updateForm(classe: IClasse): void {
    this.classe = classe;
    this.classeFormService.resetForm(this.editForm, classe);
  }
}
