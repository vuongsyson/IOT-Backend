import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICabinet } from '../cabinet.model';
import { CabinetService } from '../service/cabinet.service';

@Component({
  templateUrl: './cabinet-delete-dialog.component.html',
})
export class CabinetDeleteDialogComponent {
  cabinet?: ICabinet;

  constructor(protected cabinetService: CabinetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.cabinetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
