import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBpSwapRecord } from '../bp-swap-record.model';
import { BpSwapRecordService } from '../service/bp-swap-record.service';

@Component({
  templateUrl: './bp-swap-record-delete-dialog.component.html',
})
export class BpSwapRecordDeleteDialogComponent {
  bpSwapRecord?: IBpSwapRecord;

  constructor(protected bpSwapRecordService: BpSwapRecordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bpSwapRecordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
