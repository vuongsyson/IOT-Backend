import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBpSwapRecord, BpSwapRecord } from '../bp-swap-record.model';
import { BpSwapRecordService } from '../service/bp-swap-record.service';

@Component({
  selector: 'jhi-bp-swap-record-update',
  templateUrl: './bp-swap-record-update.component.html',
})
export class BpSwapRecordUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    oldBat: [],
    newBat: [],
    oldCab: [],
    newCab: [],
    bss: [],
    user: [],
    state: [],
    error: [],
  });

  constructor(protected bpSwapRecordService: BpSwapRecordService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bpSwapRecord }) => {
      this.updateForm(bpSwapRecord);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bpSwapRecord = this.createFromForm();
    if (bpSwapRecord.id !== undefined) {
      this.subscribeToSaveResponse(this.bpSwapRecordService.update(bpSwapRecord));
    } else {
      this.subscribeToSaveResponse(this.bpSwapRecordService.create(bpSwapRecord));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBpSwapRecord>>): void {
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

  protected updateForm(bpSwapRecord: IBpSwapRecord): void {
    this.editForm.patchValue({
      id: bpSwapRecord.id,
      oldBat: bpSwapRecord.oldBat,
      newBat: bpSwapRecord.newBat,
      oldCab: bpSwapRecord.oldCab,
      newCab: bpSwapRecord.newCab,
      bss: bpSwapRecord.bss,
      user: bpSwapRecord.user,
      state: bpSwapRecord.state,
      error: bpSwapRecord.error,
    });
  }

  protected createFromForm(): IBpSwapRecord {
    return {
      ...new BpSwapRecord(),
      id: this.editForm.get(['id'])!.value,
      oldBat: this.editForm.get(['oldBat'])!.value,
      newBat: this.editForm.get(['newBat'])!.value,
      oldCab: this.editForm.get(['oldCab'])!.value,
      newCab: this.editForm.get(['newCab'])!.value,
      bss: this.editForm.get(['bss'])!.value,
      user: this.editForm.get(['user'])!.value,
      state: this.editForm.get(['state'])!.value,
      error: this.editForm.get(['error'])!.value,
    };
  }
}
