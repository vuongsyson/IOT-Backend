import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICabinet } from '../cabinet.model';
import { CabinetService } from '../service/cabinet.service';
import { CabinetDeleteDialogComponent } from '../delete/cabinet-delete-dialog.component';

@Component({
  selector: 'jhi-cabinet',
  templateUrl: './cabinet.component.html',
})
export class CabinetComponent implements OnInit {
  cabinets?: ICabinet[];
  isLoading = false;

  constructor(protected cabinetService: CabinetService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.cabinetService.query().subscribe({
      next: (res: HttpResponse<ICabinet[]>) => {
        this.isLoading = false;
        this.cabinets = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: ICabinet): number {
    return item.id!;
  }

  delete(cabinet: ICabinet): void {
    const modalRef = this.modalService.open(CabinetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.cabinet = cabinet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
