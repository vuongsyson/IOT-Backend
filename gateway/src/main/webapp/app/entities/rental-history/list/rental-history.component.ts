import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRentalHistory } from '../rental-history.model';
import { RentalHistoryService } from '../service/rental-history.service';
import { RentalHistoryDeleteDialogComponent } from '../delete/rental-history-delete-dialog.component';

@Component({
  selector: 'jhi-rental-history',
  templateUrl: './rental-history.component.html',
})
export class RentalHistoryComponent implements OnInit {
  rentalHistories?: IRentalHistory[];
  isLoading = false;

  constructor(protected rentalHistoryService: RentalHistoryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.rentalHistoryService.query().subscribe({
      next: (res: HttpResponse<IRentalHistory[]>) => {
        this.isLoading = false;
        this.rentalHistories = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IRentalHistory): number {
    return item.id!;
  }

  delete(rentalHistory: IRentalHistory): void {
    const modalRef = this.modalService.open(RentalHistoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.rentalHistory = rentalHistory;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
