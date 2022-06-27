import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrg } from '../org.model';

@Component({
  selector: 'jhi-org-detail',
  templateUrl: './org-detail.component.html',
})
export class OrgDetailComponent implements OnInit {
  org: IOrg | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ org }) => {
      this.org = org;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
