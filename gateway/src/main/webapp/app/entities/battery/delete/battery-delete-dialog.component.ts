import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBattery } from '../battery.model';
import { BatteryService } from '../service/battery.service';

@Component({
  templateUrl: './battery-delete-dialog.component.html',
})
export class BatteryDeleteDialogComponent {
  battery?: IBattery;

  constructor(protected batteryService: BatteryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.batteryService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
