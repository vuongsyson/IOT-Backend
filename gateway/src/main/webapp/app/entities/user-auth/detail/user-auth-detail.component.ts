import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUserAuth } from '../user-auth.model';

@Component({
  selector: 'jhi-user-auth-detail',
  templateUrl: './user-auth-detail.component.html',
})
export class UserAuthDetailComponent implements OnInit {
  userAuth: IUserAuth | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userAuth }) => {
      this.userAuth = userAuth;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
