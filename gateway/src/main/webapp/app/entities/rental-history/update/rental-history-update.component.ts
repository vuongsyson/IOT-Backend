import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRentalHistory, RentalHistory } from '../rental-history.model';
import { RentalHistoryService } from '../service/rental-history.service';

@Component({
  selector: 'jhi-rental-history-update',
  templateUrl: './rental-history-update.component.html',
})
export class RentalHistoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    userId: [],
    batteryId: [],
    timeStart: [],
    timeEnd: [],
  });

  constructor(protected rentalHistoryService: RentalHistoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rentalHistory }) => {
      if (rentalHistory.id === undefined) {
        const today = dayjs().startOf('day');
        rentalHistory.timeStart = today;
        rentalHistory.timeEnd = today;
      }

      this.updateForm(rentalHistory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const rentalHistory = this.createFromForm();
    if (rentalHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.rentalHistoryService.update(rentalHistory));
    } else {
      this.subscribeToSaveResponse(this.rentalHistoryService.create(rentalHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRentalHistory>>): void {
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

  protected updateForm(rentalHistory: IRentalHistory): void {
    this.editForm.patchValue({
      id: rentalHistory.id,
      userId: rentalHistory.userId,
      batteryId: rentalHistory.batteryId,
      timeStart: rentalHistory.timeStart ? rentalHistory.timeStart.format(DATE_TIME_FORMAT) : null,
      timeEnd: rentalHistory.timeEnd ? rentalHistory.timeEnd.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IRentalHistory {
    return {
      ...new RentalHistory(),
      id: this.editForm.get(['id'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      batteryId: this.editForm.get(['batteryId'])!.value,
      timeStart: this.editForm.get(['timeStart'])!.value ? dayjs(this.editForm.get(['timeStart'])!.value, DATE_TIME_FORMAT) : undefined,
      timeEnd: this.editForm.get(['timeEnd'])!.value ? dayjs(this.editForm.get(['timeEnd'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
