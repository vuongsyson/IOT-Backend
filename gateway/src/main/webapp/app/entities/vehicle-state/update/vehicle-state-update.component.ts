import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IVehicleState, VehicleState } from '../vehicle-state.model';
import { VehicleStateService } from '../service/vehicle-state.service';

@Component({
  selector: 'jhi-vehicle-state-update',
  templateUrl: './vehicle-state-update.component.html',
})
export class VehicleStateUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    speed: [],
    lat: [],
    lon: [],
    error: [],
    serialNumber: [],
    status: [],
    odo: [],
    power: [],
    throttle: [],
    time: [],
  });

  constructor(protected vehicleStateService: VehicleStateService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicleState }) => {
      if (vehicleState.id === undefined) {
        const today = dayjs().startOf('day');
        vehicleState.time = today;
      }

      this.updateForm(vehicleState);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicleState = this.createFromForm();
    if (vehicleState.id !== undefined) {
      this.subscribeToSaveResponse(this.vehicleStateService.update(vehicleState));
    } else {
      this.subscribeToSaveResponse(this.vehicleStateService.create(vehicleState));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicleState>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(vehicleState: IVehicleState): void {
    this.editForm.patchValue({
      id: vehicleState.id,
      speed: vehicleState.speed,
      lat: vehicleState.lat,
      lon: vehicleState.lon,
      error: vehicleState.error,
      serialNumber: vehicleState.serialNumber,
      status: vehicleState.status,
      odo: vehicleState.odo,
      power: vehicleState.power,
      throttle: vehicleState.throttle,
      time: vehicleState.time ? vehicleState.time.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IVehicleState {
    return {
      ...new VehicleState(),
      id: this.editForm.get(['id'])!.value,
      speed: this.editForm.get(['speed'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lon: this.editForm.get(['lon'])!.value,
      error: this.editForm.get(['error'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      status: this.editForm.get(['status'])!.value,
      odo: this.editForm.get(['odo'])!.value,
      power: this.editForm.get(['power'])!.value,
      throttle: this.editForm.get(['throttle'])!.value,
      time: this.editForm.get(['time'])!.value ? dayjs(this.editForm.get(['time'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
