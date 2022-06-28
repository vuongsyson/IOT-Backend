import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRentalHistory } from '../rental-history.model';
import { RentalHistoryService } from '../service/rental-history.service';

@Component({
  templateUrl: './rental-history-delete-dialog.component.html',
})
export class RentalHistoryDeleteDialogComponent {
  rentalHistory?: IRentalHistory;

  constructor(protected rentalHistoryService: RentalHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.rentalHistoryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
