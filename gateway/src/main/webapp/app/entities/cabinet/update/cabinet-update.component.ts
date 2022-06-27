import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICabinet, Cabinet } from '../cabinet.model';
import { CabinetService } from '../service/cabinet.service';

@Component({
  selector: 'jhi-cabinet-update',
  templateUrl: './cabinet-update.component.html',
})
export class CabinetUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    bssId: [null, [Validators.required]],
    bpId: [],
    bpReady: [],
    swapNo: [],
    stateCode: [],
  });

  constructor(protected cabinetService: CabinetService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cabinet }) => {
      this.updateForm(cabinet);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const cabinet = this.createFromForm();
    if (cabinet.id !== undefined) {
      this.subscribeToSaveResponse(this.cabinetService.update(cabinet));
    } else {
      this.subscribeToSaveResponse(this.cabinetService.create(cabinet));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICabinet>>): void {
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

  protected updateForm(cabinet: ICabinet): void {
    this.editForm.patchValue({
      id: cabinet.id,
      bssId: cabinet.bssId,
      bpId: cabinet.bpId,
      bpReady: cabinet.bpReady,
      swapNo: cabinet.swapNo,
      stateCode: cabinet.stateCode,
    });
  }

  protected createFromForm(): ICabinet {
    return {
      ...new Cabinet(),
      id: this.editForm.get(['id'])!.value,
      bssId: this.editForm.get(['bssId'])!.value,
      bpId: this.editForm.get(['bpId'])!.value,
      bpReady: this.editForm.get(['bpReady'])!.value,
      swapNo: this.editForm.get(['swapNo'])!.value,
      stateCode: this.editForm.get(['stateCode'])!.value,
    };
  }
}
