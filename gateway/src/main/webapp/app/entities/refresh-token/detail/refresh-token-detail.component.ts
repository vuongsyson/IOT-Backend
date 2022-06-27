import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRefreshToken } from '../refresh-token.model';

@Component({
  selector: 'jhi-refresh-token-detail',
  templateUrl: './refresh-token-detail.component.html',
})
export class RefreshTokenDetailComponent implements OnInit {
  refreshToken: IRefreshToken | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ refreshToken }) => {
      this.refreshToken = refreshToken;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
