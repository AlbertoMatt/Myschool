import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IAlunnoCompito } from '../alunno-compito.model';

@Component({
  standalone: true,
  selector: 'jhi-alunno-compito-detail',
  templateUrl: './alunno-compito-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class AlunnoCompitoDetailComponent {
  @Input() alunnoCompito: IAlunnoCompito | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
