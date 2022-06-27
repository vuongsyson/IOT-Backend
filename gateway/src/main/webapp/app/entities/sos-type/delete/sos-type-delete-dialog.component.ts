import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISosType } from '../sos-type.model';
import { SosTypeService } from '../service/sos-type.service';

@Component({
  templateUrl: './sos-type-delete-dialog.component.html',
})
export class SosTypeDeleteDialogComponent {
  sosType?: ISosType;

  constructor(protected sosTypeService: SosTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sosTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
