import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBatteryState } from '../battery-state.model';
import { BatteryStateService } from '../service/battery-state.service';

@Component({
  templateUrl: './battery-state-delete-dialog.component.html',
})
export class BatteryStateDeleteDialogComponent {
  batteryState?: IBatteryState;

  constructor(protected batteryStateService: BatteryStateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.batteryStateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
