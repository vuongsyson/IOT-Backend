import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRentalHistory } from '../rental-history.model';

@Component({
  selector: 'jhi-rental-history-detail',
  templateUrl: './rental-history-detail.component.html',
})
export class RentalHistoryDetailComponent implements OnInit {
  rentalHistory: IRentalHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ rentalHistory }) => {
      this.rentalHistory = rentalHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
