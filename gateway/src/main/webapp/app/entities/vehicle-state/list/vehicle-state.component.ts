import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVehicleState } from '../vehicle-state.model';
import { VehicleStateService } from '../service/vehicle-state.service';
import { VehicleStateDeleteDialogComponent } from '../delete/vehicle-state-delete-dialog.component';

@Component({
  selector: 'jhi-vehicle-state',
  templateUrl: './vehicle-state.component.html',
})
export class VehicleStateComponent implements OnInit {
  vehicleStates?: IVehicleState[];
  isLoading = false;

  constructor(protected vehicleStateService: VehicleStateService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.vehicleStateService.query().subscribe({
      next: (res: HttpResponse<IVehicleState[]>) => {
        this.isLoading = false;
        this.vehicleStates = res.body ?? [];
      },
      error: () => {
        this.isLoading = false;
      },
    });
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(_index: number, item: IVehicleState): number {
    return item.id!;
  }

  delete(vehicleState: IVehicleState): void {
    const modalRef = this.modalService.open(VehicleStateDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.vehicleState = vehicleState;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
