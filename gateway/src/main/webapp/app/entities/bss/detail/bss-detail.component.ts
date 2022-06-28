import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBss } from '../bss.model';

@Component({
  selector: 'jhi-bss-detail',
  templateUrl: './bss-detail.component.html',
})
export class BssDetailComponent implements OnInit {
  bss: IBss | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bss }) => {
      this.bss = bss;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
