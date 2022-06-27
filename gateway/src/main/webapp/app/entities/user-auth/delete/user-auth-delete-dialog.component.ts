import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserAuth } from '../user-auth.model';
import { UserAuthService } from '../service/user-auth.service';

@Component({
  templateUrl: './user-auth-delete-dialog.component.html',
})
export class UserAuthDeleteDialogComponent {
  userAuth?: IUserAuth;

  constructor(protected userAuthService: UserAuthService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.userAuthService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
