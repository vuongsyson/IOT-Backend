import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IUserAuth, UserAuth } from '../user-auth.model';
import { UserAuthService } from '../service/user-auth.service';

@Component({
  selector: 'jhi-user-auth-update',
  templateUrl: './user-auth-update.component.html',
})
export class UserAuthUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    email: [],
    phone: [],
    username: [],
    authorities: [],
    password: [],
  });

  constructor(protected userAuthService: UserAuthService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAuth }) => {
      this.updateForm(userAuth);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userAuth = this.createFromForm();
    if (userAuth.id !== undefined) {
      this.subscribeToSaveResponse(this.userAuthService.update(userAuth));
    } else {
      this.subscribeToSaveResponse(this.userAuthService.create(userAuth));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserAuth>>): void {
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

  protected updateForm(userAuth: IUserAuth): void {
    this.editForm.patchValue({
      id: userAuth.id,
      email: userAuth.email,
      phone: userAuth.phone,
      username: userAuth.username,
      authorities: userAuth.authorities,
      password: userAuth.password,
    });
  }

  protected createFromForm(): IUserAuth {
    return {
      ...new UserAuth(),
      id: this.editForm.get(['id'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      username: this.editForm.get(['username'])!.value,
      authorities: this.editForm.get(['authorities'])!.value,
      password: this.editForm.get(['password'])!.value,
    };
  }
}
