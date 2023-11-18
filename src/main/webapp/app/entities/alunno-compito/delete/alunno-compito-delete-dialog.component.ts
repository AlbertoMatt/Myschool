import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAlunnoCompito } from '../alunno-compito.model';
import { AlunnoCompitoService } from '../service/alunno-compito.service';

@Component({
  standalone: true,
  templateUrl: './alunno-compito-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AlunnoCompitoDeleteDialogComponent {
  alunnoCompito?: IAlunnoCompito;

  constructor(
    protected alunnoCompitoService: AlunnoCompitoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alunnoCompitoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
