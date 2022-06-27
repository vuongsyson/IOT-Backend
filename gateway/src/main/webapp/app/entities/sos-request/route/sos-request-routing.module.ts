import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SosRequestComponent } from '../list/sos-request.component';
import { SosRequestDetailComponent } from '../detail/sos-request-detail.component';
import { SosRequestUpdateComponent } from '../update/sos-request-update.component';
import { SosRequestRoutingResolveService } from './sos-request-routing-resolve.service';

const sosRequestRoute: Routes = [
  {
    path: '',
    component: SosRequestComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SosRequestDetailComponent,
    resolve: {
      sosRequest: SosRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SosRequestUpdateComponent,
    resolve: {
      sosRequest: SosRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SosRequestUpdateComponent,
    resolve: {
      sosRequest: SosRequestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sosRequestRoute)],
  exports: [RouterModule],
})
export class SosRequestRoutingModule {}
