import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VehicleStateComponent } from '../list/vehicle-state.component';
import { VehicleStateDetailComponent } from '../detail/vehicle-state-detail.component';
import { VehicleStateUpdateComponent } from '../update/vehicle-state-update.component';
import { VehicleStateRoutingResolveService } from './vehicle-state-routing-resolve.service';

const vehicleStateRoute: Routes = [
  {
    path: '',
    component: VehicleStateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VehicleStateDetailComponent,
    resolve: {
      vehicleState: VehicleStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VehicleStateUpdateComponent,
    resolve: {
      vehicleState: VehicleStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VehicleStateUpdateComponent,
    resolve: {
      vehicleState: VehicleStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vehicleStateRoute)],
  exports: [RouterModule],
})
export class VehicleStateRoutingModule {}
