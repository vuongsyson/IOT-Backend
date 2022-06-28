import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BssComponent } from '../list/bss.component';
import { BssDetailComponent } from '../detail/bss-detail.component';
import { BssUpdateComponent } from '../update/bss-update.component';
import { BssRoutingResolveService } from './bss-routing-resolve.service';

const bssRoute: Routes = [
  {
    path: '',
    component: BssComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BssDetailComponent,
    resolve: {
      bss: BssRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BssUpdateComponent,
    resolve: {
      bss: BssRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BssUpdateComponent,
    resolve: {
      bss: BssRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bssRoute)],
  exports: [RouterModule],
})
export class BssRoutingModule {}
