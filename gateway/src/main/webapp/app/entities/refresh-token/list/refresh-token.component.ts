import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRefreshToken } from '../refresh-token.model';
import { RefreshTokenService } from '../service/refresh-token.service';
import { RefreshTokenDeleteDialogComponent } from '../delete/refresh-token-delete-dialog.component';

@Component({
  selector: 'jhi-refresh-token',
  templateUrl: './refresh-token.component.html',
})
export class RefreshTokenComponent implements OnInit {
  refreshTokens?: IRefreshToken[];
  isLoading = false;

  constructor(protected refreshTokenService: RefreshTokenService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.refreshTokenService.query().subscribe({
      next: (res: HttpResponse<IRefreshToken[]>) => {
        this.isLoading = false;
        this.refreshTokens = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRefreshToken): number {
    return item.id!;
  }

  delete(refreshToken: IRefreshToken): void {
    const modalRef = this.modalService.open(RefreshTokenDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.refreshToken = refreshToken;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
