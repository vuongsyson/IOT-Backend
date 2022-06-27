import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBss } from '../bss.model';
import { BssService } from '../service/bss.service';
import { BssDeleteDialogComponent } from '../delete/bss-delete-dialog.component';

@Component({
  selector: 'jhi-bss',
  templateUrl: './bss.component.html',
})
export class BssComponent implements OnInit {
  bsses?: IBss[];
  isLoading = false;

  constructor(protected bssService: BssService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bssService.query().subscribe({
      next: (res: HttpResponse<IBss[]>) => {
        this.isLoading = false;
        this.bsses = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBss): number {
    return item.id!;
  }

  delete(bss: IBss): void {
    const modalRef = this.modalService.open(BssDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bss = bss;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
