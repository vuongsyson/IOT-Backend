import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISosRequest, SosRequest } from '../sos-request.model';
import { SosRequestService } from '../service/sos-request.service';
import { SosState } from 'app/entities/enumerations/sos-state.model';

@Component({
  selector: 'jhi-sos-request-update',
  templateUrl: './sos-request-update.component.html',
})
export class SosRequestUpdateComponent implements OnInit {
  isSaving = false;
  sosStateValues = Object.keys(SosState);

  editForm = this.fb.group({
    id: [],
    userId: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    deviceSerialNumber: [null, [Validators.required]],
    description: [],
    image: [],
    state: [],
    rating: [null, [Validators.min(0), Validators.max(5)]],
    done: [],
    doneTime: [],
  });

  constructor(protected sosRequestService: SosRequestService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sosRequest }) => {
      if (sosRequest.id === undefined) {
        const today = dayjs().startOf('day');
        sosRequest.doneTime = today;
      }

      this.updateForm(sosRequest);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sosRequest = this.createFromForm();
    if (sosRequest.id !== undefined) {
      this.subscribeToSaveResponse(this.sosRequestService.update(sosRequest));
    } else {
      this.subscribeToSaveResponse(this.sosRequestService.create(sosRequest));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISosRequest>>): void {
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

  protected updateForm(sosRequest: ISosRequest): void {
    this.editForm.patchValue({
      id: sosRequest.id,
      userId: sosRequest.userId,
      phone: sosRequest.phone,
      deviceSerialNumber: sosRequest.deviceSerialNumber,
      description: sosRequest.description,
      image: sosRequest.image,
      state: sosRequest.state,
      rating: sosRequest.rating,
      done: sosRequest.done,
      doneTime: sosRequest.doneTime ? sosRequest.doneTime.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): ISosRequest {
    return {
      ...new SosRequest(),
      id: this.editForm.get(['id'])!.value,
      userId: this.editForm.get(['userId'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      deviceSerialNumber: this.editForm.get(['deviceSerialNumber'])!.value,
      description: this.editForm.get(['description'])!.value,
      image: this.editForm.get(['image'])!.value,
      state: this.editForm.get(['state'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      done: this.editForm.get(['done'])!.value,
      doneTime: this.editForm.get(['doneTime'])!.value ? dayjs(this.editForm.get(['doneTime'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
