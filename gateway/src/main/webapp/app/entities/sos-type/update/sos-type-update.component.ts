import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISosType, SosType } from '../sos-type.model';
import { SosTypeService } from '../service/sos-type.service';

@Component({
  selector: 'jhi-sos-type-update',
  templateUrl: './sos-type-update.component.html',
})
export class SosTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
  });

  constructor(protected sosTypeService: SosTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sosType }) => {
      this.updateForm(sosType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sosType = this.createFromForm();
    if (sosType.id !== undefined) {
      this.subscribeToSaveResponse(this.sosTypeService.update(sosType));
    } else {
      this.subscribeToSaveResponse(this.sosTypeService.create(sosType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISosType>>): void {
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

  protected updateForm(sosType: ISosType): void {
    this.editForm.patchValue({
      id: sosType.id,
      name: sosType.name,
    });
  }

  protected createFromForm(): ISosType {
    return {
      ...new SosType(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
    };
  }
}
