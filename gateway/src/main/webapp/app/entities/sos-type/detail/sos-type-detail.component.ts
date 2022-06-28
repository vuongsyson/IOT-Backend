import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISosType } from '../sos-type.model';

@Component({
  selector: 'jhi-sos-type-detail',
  templateUrl: './sos-type-detail.component.html',
})
export class SosTypeDetailComponent implements OnInit {
  sosType: ISosType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sosType }) => {
      this.sosType = sosType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
