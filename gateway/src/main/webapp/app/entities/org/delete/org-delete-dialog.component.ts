import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrg } from '../org.model';
import { OrgService } from '../service/org.service';

@Component({
  templateUrl: './org-delete-dialog.component.html',
})
export class OrgDeleteDialogComponent {
  org?: IOrg;

  constructor(protected orgService: OrgService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orgService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
