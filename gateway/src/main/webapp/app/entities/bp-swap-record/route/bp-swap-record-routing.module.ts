import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BpSwapRecordComponent } from '../list/bp-swap-record.component';
import { BpSwapRecordDetailComponent } from '../detail/bp-swap-record-detail.component';
import { BpSwapRecordUpdateComponent } from '../update/bp-swap-record-update.component';
import { BpSwapRecordRoutingResolveService } from './bp-swap-record-routing-resolve.service';

const bpSwapRecordRoute: Routes = [
  {
    path: '',
    component: BpSwapRecordComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BpSwapRecordDetailComponent,
    resolve: {
      bpSwapRecord: BpSwapRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BpSwapRecordUpdateComponent,
    resolve: {
      bpSwapRecord: BpSwapRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BpSwapRecordUpdateComponent,
    resolve: {
      bpSwapRecord: BpSwapRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bpSwapRecordRoute)],
  exports: [RouterModule],
})
export class BpSwapRecordRoutingModule {}
