import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICompitoInClasse } from '../compito-in-classe.model';
import { CompitoInClasseService } from '../service/compito-in-classe.service';

@Component({
  standalone: true,
  templateUrl: './compito-in-classe-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CompitoInClasseDeleteDialogComponent {
  compitoInClasse?: ICompitoInClasse;

  constructor(
    protected compitoInClasseService: CompitoInClasseService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compitoInClasseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
