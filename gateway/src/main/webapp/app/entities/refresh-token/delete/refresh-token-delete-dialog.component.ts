import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRefreshToken } from '../refresh-token.model';
import { RefreshTokenService } from '../service/refresh-token.service';

@Component({
  templateUrl: './refresh-token-delete-dialog.component.html',
})
export class RefreshTokenDeleteDialogComponent {
  refreshToken?: IRefreshToken;

  constructor(protected refreshTokenService: RefreshTokenService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.refreshTokenService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
