import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISosRequest } from '../sos-request.model';

@Component({
  selector: 'jhi-sos-request-detail',
  templateUrl: './sos-request-detail.component.html',
})
export class SosRequestDetailComponent implements OnInit {
  sosRequest: ISosRequest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sosRequest }) => {
      this.sosRequest = sosRequest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
