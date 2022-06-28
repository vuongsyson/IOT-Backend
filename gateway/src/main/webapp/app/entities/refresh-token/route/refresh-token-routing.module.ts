import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RefreshTokenComponent } from '../list/refresh-token.component';
import { RefreshTokenDetailComponent } from '../detail/refresh-token-detail.component';
import { RefreshTokenUpdateComponent } from '../update/refresh-token-update.component';
import { RefreshTokenRoutingResolveService } from './refresh-token-routing-resolve.service';

const refreshTokenRoute: Routes = [
  {
    path: '',
    component: RefreshTokenComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RefreshTokenDetailComponent,
    resolve: {
      refreshToken: RefreshTokenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RefreshTokenUpdateComponent,
    resolve: {
      refreshToken: RefreshTokenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RefreshTokenUpdateComponent,
    resolve: {
      refreshToken: RefreshTokenRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(refreshTokenRoute)],
  exports: [RouterModule],
})
export class RefreshTokenRoutingModule {}
