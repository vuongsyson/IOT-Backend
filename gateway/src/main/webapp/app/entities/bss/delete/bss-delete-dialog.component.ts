import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBss } from '../bss.model';
import { BssService } from '../service/bss.service';

@Component({
  templateUrl: './bss-delete-dialog.component.html',
})
export class BssDeleteDialogComponent {
  bss?: IBss;

  constructor(protected bssService: BssService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bssService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
