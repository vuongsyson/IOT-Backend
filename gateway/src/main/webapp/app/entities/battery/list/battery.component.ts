import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBattery } from '../battery.model';
import { BatteryService } from '../service/battery.service';
import { BatteryDeleteDialogComponent } from '../delete/battery-delete-dialog.component';

@Component({
  selector: 'jhi-battery',
  templateUrl: './battery.component.html',
})
export class BatteryComponent implements OnInit {
  batteries?: IBattery[];
  isLoading = false;

  constructor(protected batteryService: BatteryService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.batteryService.query().subscribe({
      next: (res: HttpResponse<IBattery[]>) => {
        this.isLoading = false;
        this.batteries = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IBattery): number {
    return item.id!;
  }

  delete(battery: IBattery): void {
    const modalRef = this.modalService.open(BatteryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.battery = battery;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
