import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { ICompitoInClasse } from '../compito-in-classe.model';

@Component({
  standalone: true,
  selector: 'jhi-compito-in-classe-detail',
  templateUrl: './compito-in-classe-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CompitoInClasseDetailComponent {
  @Input() compitoInClasse: ICompitoInClasse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
