import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BatteryStateComponent } from '../list/battery-state.component';
import { BatteryStateDetailComponent } from '../detail/battery-state-detail.component';
import { BatteryStateUpdateComponent } from '../update/battery-state-update.component';
import { BatteryStateRoutingResolveService } from './battery-state-routing-resolve.service';

const batteryStateRoute: Routes = [
  {
    path: '',
    component: BatteryStateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BatteryStateDetailComponent,
    resolve: {
      batteryState: BatteryStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BatteryStateUpdateComponent,
    resolve: {
      batteryState: BatteryStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BatteryStateUpdateComponent,
    resolve: {
      batteryState: BatteryStateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(batteryStateRoute)],
  exports: [RouterModule],
})
export class BatteryStateRoutingModule {}
