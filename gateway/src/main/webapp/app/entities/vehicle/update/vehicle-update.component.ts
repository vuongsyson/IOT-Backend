import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IVehicle, Vehicle } from '../vehicle.model';
import { VehicleService } from '../service/vehicle.service';

@Component({
  selector: 'jhi-vehicle-update',
  templateUrl: './vehicle-update.component.html',
})
export class VehicleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    clearance: [],
    maxPower: [],
    maxSpeed: [],
    maxLoad: [],
    weightTotal: [],
    maxDistance: [],
    wheelBase: [],
    hwVersion: [],
    swVersion: [],
    serialNumber: [],
    manufactureDate: [],
    lotNumber: [],
    color: [],
    vehicleType: [],
    used: [],
    userId: [],
  });

  constructor(protected vehicleService: VehicleService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vehicle }) => {
      this.updateForm(vehicle);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const vehicle = this.createFromForm();
    if (vehicle.id !== undefined) {
      this.subscribeToSaveResponse(this.vehicleService.update(vehicle));
    } else {
      this.subscribeToSaveResponse(this.vehicleService.create(vehicle));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVehicle>>): void {
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

  protected updateForm(vehicle: IVehicle): void {
    this.editForm.patchValue({
      id: vehicle.id,
      clearance: vehicle.clearance,
      maxPower: vehicle.maxPower,
      maxSpeed: vehicle.maxSpeed,
      maxLoad: vehicle.maxLoad,
      weightTotal: vehicle.weightTotal,
      maxDistance: vehicle.maxDistance,
      wheelBase: vehicle.wheelBase,
      hwVersion: vehicle.hwVersion,
      swVersion: vehicle.swVersion,
      serialNumber: vehicle.serialNumber,
      manufactureDate: vehicle.manufactureDate,
      lotNumber: vehicle.lotNumber,
      color: vehicle.color,
      vehicleType: vehicle.vehicleType,
      used: vehicle.used,
      userId: vehicle.userId,
    });
  }

  protected createFromForm(): IVehicle {
    return {
      ...new Vehicle(),
      id: this.editForm.get(['id'])!.value,
      clearance: this.editForm.get(['clearance'])!.value,
      maxPower: this.editForm.get(['maxPower'])!.value,
      maxSpeed: this.editForm.get(['maxSpeed'])!.value,
      maxLoad: this.editForm.get(['maxLoad'])!.value,
      weightTotal: this.editForm.get(['weightTotal'])!.value,
      maxDistance: this.editForm.get(['maxDistance'])!.value,
      wheelBase: this.editForm.get(['wheelBase'])!.value,
      hwVersion: this.editForm.get(['hwVersion'])!.value,
      swVersion: this.editForm.get(['swVersion'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      manufactureDate: this.editForm.get(['manufactureDate'])!.value,
      lotNumber: this.editForm.get(['lotNumber'])!.value,
      color: this.editForm.get(['color'])!.value,
      vehicleType: this.editForm.get(['vehicleType'])!.value,
      used: this.editForm.get(['used'])!.value,
      userId: this.editForm.get(['userId'])!.value,
    };
  }
}
