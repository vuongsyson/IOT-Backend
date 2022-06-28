import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IOrg, Org } from '../org.model';
import { OrgService } from '../service/org.service';

@Component({
  selector: 'jhi-org-update',
  templateUrl: './org-update.component.html',
})
export class OrgUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    orgId: [],
  });

  constructor(protected orgService: OrgService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ org }) => {
      this.updateForm(org);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const org = this.createFromForm();
    if (org.id !== undefined) {
      this.subscribeToSaveResponse(this.orgService.update(org));
    } else {
      this.subscribeToSaveResponse(this.orgService.create(org));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrg>>): void {
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

  protected updateForm(org: IOrg): void {
    this.editForm.patchValue({
      id: org.id,
      orgId: org.orgId,
    });
  }

  protected createFromForm(): IOrg {
    return {
      ...new Org(),
      id: this.editForm.get(['id'])!.value,
      orgId: this.editForm.get(['orgId'])!.value,
    };
  }
}
