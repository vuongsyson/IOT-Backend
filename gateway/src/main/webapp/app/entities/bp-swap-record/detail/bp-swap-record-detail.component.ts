import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBpSwapRecord } from '../bp-swap-record.model';

@Component({
  selector: 'jhi-bp-swap-record-detail',
  templateUrl: './bp-swap-record-detail.component.html',
})
export class BpSwapRecordDetailComponent implements OnInit {
  bpSwapRecord: IBpSwapRecord | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bpSwapRecord }) => {
      this.bpSwapRecord = bpSwapRecord;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
