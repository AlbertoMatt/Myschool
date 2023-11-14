import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAlunno } from '../alunno.model';
import { AlunnoService } from '../service/alunno.service';

@Component({
  standalone: true,
  templateUrl: './alunno-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AlunnoDeleteDialogComponent {
  alunno?: IAlunno;

  constructor(
    protected alunnoService: AlunnoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.alunnoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
