import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BatteryComponent } from '../list/battery.component';
import { BatteryDetailComponent } from '../detail/battery-detail.component';
import { BatteryUpdateComponent } from '../update/battery-update.component';
import { BatteryRoutingResolveService } from './battery-routing-resolve.service';

const batteryRoute: Routes = [
  {
    path: '',
    component: BatteryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BatteryDetailComponent,
    resolve: {
      battery: BatteryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BatteryUpdateComponent,
    resolve: {
      battery: BatteryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BatteryUpdateComponent,
    resolve: {
      battery: BatteryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(batteryRoute)],
  exports: [RouterModule],
})
export class BatteryRoutingModule {}
