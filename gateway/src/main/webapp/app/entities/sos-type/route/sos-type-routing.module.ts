import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SosTypeComponent } from '../list/sos-type.component';
import { SosTypeDetailComponent } from '../detail/sos-type-detail.component';
import { SosTypeUpdateComponent } from '../update/sos-type-update.component';
import { SosTypeRoutingResolveService } from './sos-type-routing-resolve.service';

const sosTypeRoute: Routes = [
  {
    path: '',
    component: SosTypeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SosTypeDetailComponent,
    resolve: {
      sosType: SosTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SosTypeUpdateComponent,
    resolve: {
      sosType: SosTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SosTypeUpdateComponent,
    resolve: {
      sosType: SosTypeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sosTypeRoute)],
  exports: [RouterModule],
})
export class SosTypeRoutingModule {}
