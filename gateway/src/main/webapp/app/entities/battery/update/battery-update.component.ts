import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBattery, Battery } from '../battery.model';
import { BatteryService } from '../service/battery.service';
import { IRentalHistory } from 'app/entities/rental-history/rental-history.model';
import { RentalHistoryService } from 'app/entities/rental-history/service/rental-history.service';

@Component({
  selector: 'jhi-battery-update',
  templateUrl: './battery-update.component.html',
})
export class BatteryUpdateComponent implements OnInit {
  isSaving = false;

  rentalHistoriesSharedCollection: IRentalHistory[] = [];

  editForm = this.fb.group({
    id: [],
    serialNo: [null, [Validators.required]],
    hwVersion: [null, [Validators.required]],
    swVersion: [null, [Validators.required]],
    manufactureDate: [null, [Validators.required]],
    capacity: [null, [Validators.required]],
    maxCharge: [null, [Validators.required]],
    maxDiscarge: [null, [Validators.required]],
    maxVol: [null, [Validators.required]],
    minVol: [null, [Validators.required]],
    used: [null, [Validators.required]],
    soc: [null, [Validators.required]],
    soh: [null, [Validators.required]],
    temp: [null, [Validators.required]],
    ownerId: [null, [Validators.required]],
    renterId: [null, [Validators.required]],
    cycleCount: [null, [Validators.required]],
    rentalHistory: [],
  });

  constructor(
    protected batteryService: BatteryService,
    protected rentalHistoryService: RentalHistoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ battery }) => {
      this.updateForm(battery);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const battery = this.createFromForm();
    if (battery.id !== undefined) {
      this.subscribeToSaveResponse(this.batteryService.update(battery));
    } else {
      this.subscribeToSaveResponse(this.batteryService.create(battery));
    }
  }

  trackRentalHistoryById(_index: number, item: IRentalHistory): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBattery>>): void {
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

  protected updateForm(battery: IBattery): void {
    this.editForm.patchValue({
      id: battery.id,
      serialNo: battery.serialNo,
      hwVersion: battery.hwVersion,
      swVersion: battery.swVersion,
      manufactureDate: battery.manufactureDate,
      capacity: battery.capacity,
      maxCharge: battery.maxCharge,
      maxDiscarge: battery.maxDiscarge,
      maxVol: battery.maxVol,
      minVol: battery.minVol,
      used: battery.used,
      soc: battery.soc,
      soh: battery.soh,
      temp: battery.temp,
      ownerId: battery.ownerId,
      renterId: battery.renterId,
      cycleCount: battery.cycleCount,
      rentalHistory: battery.rentalHistory,
    });

    this.rentalHistoriesSharedCollection = this.rentalHistoryService.addRentalHistoryToCollectionIfMissing(
      this.rentalHistoriesSharedCollection,
      battery.rentalHistory
    );
  }

  protected loadRelationshipsOptions(): void {
    this.rentalHistoryService
      .query()
      .pipe(map((res: HttpResponse<IRentalHistory[]>) => res.body ?? []))
      .pipe(
        map((rentalHistories: IRentalHistory[]) =>
          this.rentalHistoryService.addRentalHistoryToCollectionIfMissing(rentalHistories, this.editForm.get('rentalHistory')!.value)
        )
      )
      .subscribe((rentalHistories: IRentalHistory[]) => (this.rentalHistoriesSharedCollection = rentalHistories));
  }

  protected createFromForm(): IBattery {
    return {
      ...new Battery(),
      id: this.editForm.get(['id'])!.value,
      serialNo: this.editForm.get(['serialNo'])!.value,
      hwVersion: this.editForm.get(['hwVersion'])!.value,
      swVersion: this.editForm.get(['swVersion'])!.value,
      manufactureDate: this.editForm.get(['manufactureDate'])!.value,
      capacity: this.editForm.get(['capacity'])!.value,
      maxCharge: this.editForm.get(['maxCharge'])!.value,
      maxDiscarge: this.editForm.get(['maxDiscarge'])!.value,
      maxVol: this.editForm.get(['maxVol'])!.value,
      minVol: this.editForm.get(['minVol'])!.value,
      used: this.editForm.get(['used'])!.value,
      soc: this.editForm.get(['soc'])!.value,
      soh: this.editForm.get(['soh'])!.value,
      temp: this.editForm.get(['temp'])!.value,
      ownerId: this.editForm.get(['ownerId'])!.value,
      renterId: this.editForm.get(['renterId'])!.value,
      cycleCount: this.editForm.get(['cycleCount'])!.value,
      rentalHistory: this.editForm.get(['rentalHistory'])!.value,
    };
  }
}
