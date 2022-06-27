import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IRefreshToken, RefreshToken } from '../refresh-token.model';
import { RefreshTokenService } from '../service/refresh-token.service';

@Component({
  selector: 'jhi-refresh-token-update',
  templateUrl: './refresh-token-update.component.html',
})
export class RefreshTokenUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    username: [],
  });

  constructor(protected refreshTokenService: RefreshTokenService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ refreshToken }) => {
      this.updateForm(refreshToken);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const refreshToken = this.createFromForm();
    if (refreshToken.id !== undefined) {
      this.subscribeToSaveResponse(this.refreshTokenService.update(refreshToken));
    } else {
      this.subscribeToSaveResponse(this.refreshTokenService.create(refreshToken));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRefreshToken>>): void {
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

  protected updateForm(refreshToken: IRefreshToken): void {
    this.editForm.patchValue({
      id: refreshToken.id,
      username: refreshToken.username,
    });
  }

  protected createFromForm(): IRefreshToken {
    return {
      ...new RefreshToken(),
      id: this.editForm.get(['id'])!.value,
      username: this.editForm.get(['username'])!.value,
    };
  }
}
