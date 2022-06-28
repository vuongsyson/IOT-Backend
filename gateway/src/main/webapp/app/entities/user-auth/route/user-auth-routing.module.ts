import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserAuthComponent } from '../list/user-auth.component';
import { UserAuthDetailComponent } from '../detail/user-auth-detail.component';
import { UserAuthUpdateComponent } from '../update/user-auth-update.component';
import { UserAuthRoutingResolveService } from './user-auth-routing-resolve.service';

const userAuthRoute: Routes = [
  {
    path: '',
    component: UserAuthComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserAuthDetailComponent,
    resolve: {
      userAuth: UserAuthRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserAuthUpdateComponent,
    resolve: {
      userAuth: UserAuthRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserAuthUpdateComponent,
    resolve: {
      userAuth: UserAuthRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(userAuthRoute)],
  exports: [RouterModule],
})
export class UserAuthRoutingModule {}
