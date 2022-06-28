import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISosRequest } from '../sos-request.model';
import { SosRequestService } from '../service/sos-request.service';
import { SosRequestDeleteDialogComponent } from '../delete/sos-request-delete-dialog.component';

@Component({
  selector: 'jhi-sos-request',
  templateUrl: './sos-request.component.html',
})
export class SosRequestComponent implements OnInit {
  sosRequests?: ISosRequest[];
  isLoading = false;

  constructor(protected sosRequestService: SosRequestService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.sosRequestService.query().subscribe({
      next: (res: HttpResponse<ISosRequest[]>) => {
        this.isLoading = false;
        this.sosRequests = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ISosRequest): number {
    return item.id!;
  }

  delete(sosRequest: ISosRequest): void {
    const modalRef = this.modalService.open(SosRequestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.sosRequest = sosRequest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
