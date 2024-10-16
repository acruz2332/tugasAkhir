import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IHistory } from '../history.model';
import { HistoryService } from '../service/history.service';

@Component({
  standalone: true,
  templateUrl: './history-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class HistoryDeleteDialogComponent {
  history?: IHistory;

  constructor(
    protected historyService: HistoryService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.historyService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
