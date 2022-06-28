import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBpSwapRecord } from '../bp-swap-record.model';
import { BpSwapRecordService } from '../service/bp-swap-record.service';
import { BpSwapRecordDeleteDialogComponent } from '../delete/bp-swap-record-delete-dialog.component';

@Component({
  selector: 'jhi-bp-swap-record',
  templateUrl: './bp-swap-record.component.html',
})
export class BpSwapRecordComponent implements OnInit {
  bpSwapRecords?: IBpSwapRecord[];
  isLoading = false;

  constructor(protected bpSwapRecordService: BpSwapRecordService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bpSwapRecordService.query().subscribe({
      next: (res: HttpResponse<IBpSwapRecord[]>) => {
        this.isLoading = false;
        this.bpSwapRecords = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBpSwapRecord): number {
    return item.id!;
  }

  delete(bpSwapRecord: IBpSwapRecord): void {
    const modalRef = this.modalService.open(BpSwapRecordDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bpSwapRecord = bpSwapRecord;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
