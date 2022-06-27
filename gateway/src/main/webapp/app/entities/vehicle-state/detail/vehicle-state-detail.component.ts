import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IVehicleState } from '../vehicle-state.model';

@Component({
  selector: 'jhi-vehicle-state-detail',
  templateUrl: './vehicle-state-detail.component.html',
})
export class VehicleStateDetailComponent implements OnInit {
  vehicleState: IVehicleState | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleState }) => {
      this.vehicleState = vehicleState;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
