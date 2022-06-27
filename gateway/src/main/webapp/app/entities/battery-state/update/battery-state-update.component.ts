import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBatteryState, BatteryState } from '../battery-state.model';
import { BatteryStateService } from '../service/battery-state.service';

@Component({
  selector: 'jhi-battery-state-update',
  templateUrl: './battery-state-update.component.html',
})
export class BatteryStateUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    serialNumber: [null, [Validators.required]],
    vol: [null, [Validators.required]],
    cur: [null, [Validators.required]],
    soc: [null, [Validators.required]],
    soh: [null, [Validators.required]],
    state: [null, [Validators.required]],
    status: [null, [Validators.required]],
  });

  constructor(protected batteryStateService: BatteryStateService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ batteryState }) => {
      this.updateForm(batteryState);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const batteryState = this.createFromForm();
    if (batteryState.id !== undefined) {
      this.subscribeToSaveResponse(this.batteryStateService.update(batteryState));
    } else {
      this.subscribeToSaveResponse(this.batteryStateService.create(batteryState));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBatteryState>>): void {
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

  protected updateForm(batteryState: IBatteryState): void {
    this.editForm.patchValue({
      id: batteryState.id,
      serialNumber: batteryState.serialNumber,
      vol: batteryState.vol,
      cur: batteryState.cur,
      soc: batteryState.soc,
      soh: batteryState.soh,
      state: batteryState.state,
      status: batteryState.status,
    });
  }

  protected createFromForm(): IBatteryState {
    return {
      ...new BatteryState(),
      id: this.editForm.get(['id'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      vol: this.editForm.get(['vol'])!.value,
      cur: this.editForm.get(['cur'])!.value,
      soc: this.editForm.get(['soc'])!.value,
      soh: this.editForm.get(['soh'])!.value,
      state: this.editForm.get(['state'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
