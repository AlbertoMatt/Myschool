import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClasse } from 'app/entities/classe/classe.model';
import { ClasseService } from 'app/entities/classe/service/classe.service';
import { IAlunno } from '../alunno.model';
import { AlunnoService } from '../service/alunno.service';
import { AlunnoFormService, AlunnoFormGroup } from './alunno-form.service';

@Component({
  standalone: true,
  selector: 'jhi-alunno-update',
  templateUrl: './alunno-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AlunnoUpdateComponent implements OnInit {
  isSaving = false;
  alunno: IAlunno | null = null;

  classesSharedCollection: IClasse[] = [];

  editForm: AlunnoFormGroup = this.alunnoFormService.createAlunnoFormGroup();

  constructor(
    protected alunnoService: AlunnoService,
    protected alunnoFormService: AlunnoFormService,
    protected classeService: ClasseService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareClasse = (o1: IClasse | null, o2: IClasse | null): boolean => this.classeService.compareClasse(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ alunno }) => {
      this.alunno = alunno;
      if (alunno) {
        this.updateForm(alunno);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const alunno = this.alunnoFormService.getAlunno(this.editForm);
    if (alunno.id !== null) {
      this.subscribeToSaveResponse(this.alunnoService.update(alunno));
    } else {
      this.subscribeToSaveResponse(this.alunnoService.create(alunno));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAlunno>>): void {
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

  protected updateForm(alunno: IAlunno): void {
    this.alunno = alunno;
    this.alunnoFormService.resetForm(this.editForm, alunno);

    this.classesSharedCollection = this.classeService.addClasseToCollectionIfMissing<IClasse>(
      this.classesSharedCollection,
      alunno.classeDiAppartenenza,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classeService
      .query()
      .pipe(map((res: HttpResponse<IClasse[]>) => res.body ?? []))
      .pipe(
        map((classes: IClasse[]) => this.classeService.addClasseToCollectionIfMissing<IClasse>(classes, this.alunno?.classeDiAppartenenza)),
      )
      .subscribe((classes: IClasse[]) => (this.classesSharedCollection = classes));
  }
}
