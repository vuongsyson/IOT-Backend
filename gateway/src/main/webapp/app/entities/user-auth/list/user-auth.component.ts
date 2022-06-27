import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IUserAuth } from '../user-auth.model';
import { UserAuthService } from '../service/user-auth.service';
import { UserAuthDeleteDialogComponent } from '../delete/user-auth-delete-dialog.component';

@Component({
  selector: 'jhi-user-auth',
  templateUrl: './user-auth.component.html',
})
export class UserAuthComponent implements OnInit {
  userAuths?: IUserAuth[];
  isLoading = false;

  constructor(protected userAuthService: UserAuthService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.userAuthService.query().subscribe({
      next: (res: HttpResponse<IUserAuth[]>) => {
        this.isLoading = false;
        this.userAuths = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IUserAuth): number {
    return item.id!;
  }

  delete(userAuth: IUserAuth): void {
    const modalRef = this.modalService.open(UserAuthDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.userAuth = userAuth;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
