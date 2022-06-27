import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IVehicleState } from '../vehicle-state.model';
import { VehicleStateService } from '../service/vehicle-state.service';

@Component({
  templateUrl: './vehicle-state-delete-dialog.component.html',
})
export class VehicleStateDeleteDialogComponent {
  vehicleState?: IVehicleState;

  constructor(protected vehicleStateService: VehicleStateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleStateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
