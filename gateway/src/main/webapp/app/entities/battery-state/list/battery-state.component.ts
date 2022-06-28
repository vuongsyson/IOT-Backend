import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBatteryState } from '../battery-state.model';
import { BatteryStateService } from '../service/battery-state.service';
import { BatteryStateDeleteDialogComponent } from '../delete/battery-state-delete-dialog.component';

@Component({
  selector: 'jhi-battery-state',
  templateUrl: './battery-state.component.html',
})
export class BatteryStateComponent implements OnInit {
  batteryStates?: IBatteryState[];
  isLoading = false;

  constructor(protected batteryStateService: BatteryStateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.batteryStateService.query().subscribe({
      next: (res: HttpResponse<IBatteryState[]>) => {
        this.isLoading = false;
        this.batteryStates = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBatteryState): number {
    return item.id!;
  }

  delete(batteryState: IBatteryState): void {
    const modalRef = this.modalService.open(BatteryStateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.batteryState = batteryState;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
