import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISosRequest } from '../sos-request.model';
import { SosRequestService } from '../service/sos-request.service';

@Component({
  templateUrl: './sos-request-delete-dialog.component.html',
})
export class SosRequestDeleteDialogComponent {
  sosRequest?: ISosRequest;

  constructor(protected sosRequestService: SosRequestService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sosRequestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
