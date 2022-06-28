import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBattery } from '../battery.model';

@Component({
  selector: 'jhi-battery-detail',
  templateUrl: './battery-detail.component.html',
})
export class BatteryDetailComponent implements OnInit {
  battery: IBattery | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ battery }) => {
      this.battery = battery;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
