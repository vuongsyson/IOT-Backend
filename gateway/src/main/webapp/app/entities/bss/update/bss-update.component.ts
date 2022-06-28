import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBss, Bss } from '../bss.model';
import { BssService } from '../service/bss.service';

@Component({
  selector: 'jhi-bss-update',
  templateUrl: './bss-update.component.html',
})
export class BssUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    address: [],
    serialNumber: [],
    hwVersion: [],
    swVersion: [],
    manufactureDate: [],
    lon: [],
    lat: [],
    typeCode: [],
    cabNum: [],
    cabEmptyNum: [],
    bpReadyNum: [],
    swapBpNo: [],
  });

  constructor(protected bssService: BssService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bss }) => {
      this.updateForm(bss);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bss = this.createFromForm();
    if (bss.id !== undefined) {
      this.subscribeToSaveResponse(this.bssService.update(bss));
    } else {
      this.subscribeToSaveResponse(this.bssService.create(bss));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBss>>): void {
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

  protected updateForm(bss: IBss): void {
    this.editForm.patchValue({
      id: bss.id,
      name: bss.name,
      address: bss.address,
      serialNumber: bss.serialNumber,
      hwVersion: bss.hwVersion,
      swVersion: bss.swVersion,
      manufactureDate: bss.manufactureDate,
      lon: bss.lon,
      lat: bss.lat,
      typeCode: bss.typeCode,
      cabNum: bss.cabNum,
      cabEmptyNum: bss.cabEmptyNum,
      bpReadyNum: bss.bpReadyNum,
      swapBpNo: bss.swapBpNo,
    });
  }

  protected createFromForm(): IBss {
    return {
      ...new Bss(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      address: this.editForm.get(['address'])!.value,
      serialNumber: this.editForm.get(['serialNumber'])!.value,
      hwVersion: this.editForm.get(['hwVersion'])!.value,
      swVersion: this.editForm.get(['swVersion'])!.value,
      manufactureDate: this.editForm.get(['manufactureDate'])!.value,
      lon: this.editForm.get(['lon'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      typeCode: this.editForm.get(['typeCode'])!.value,
      cabNum: this.editForm.get(['cabNum'])!.value,
      cabEmptyNum: this.editForm.get(['cabEmptyNum'])!.value,
      bpReadyNum: this.editForm.get(['bpReadyNum'])!.value,
      swapBpNo: this.editForm.get(['swapBpNo'])!.value,
    };
  }
}
