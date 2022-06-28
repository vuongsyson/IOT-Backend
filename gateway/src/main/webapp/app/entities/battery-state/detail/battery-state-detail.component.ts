import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBatteryState } from '../battery-state.model';

@Component({
  selector: 'jhi-battery-state-detail',
  templateUrl: './battery-state-detail.component.html',
})
export class BatteryStateDetailComponent implements OnInit {
  batteryState: IBatteryState | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ batteryState }) => {
      this.batteryState = batteryState;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
