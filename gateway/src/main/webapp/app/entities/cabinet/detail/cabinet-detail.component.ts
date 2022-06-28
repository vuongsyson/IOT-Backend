import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICabinet } from '../cabinet.model';

@Component({
  selector: 'jhi-cabinet-detail',
  templateUrl: './cabinet-detail.component.html',
})
export class CabinetDetailComponent implements OnInit {
  cabinet: ICabinet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ cabinet }) => {
      this.cabinet = cabinet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
