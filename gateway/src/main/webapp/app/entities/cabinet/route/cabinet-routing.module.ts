import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CabinetComponent } from '../list/cabinet.component';
import { CabinetDetailComponent } from '../detail/cabinet-detail.component';
import { CabinetUpdateComponent } from '../update/cabinet-update.component';
import { CabinetRoutingResolveService } from './cabinet-routing-resolve.service';

const cabinetRoute: Routes = [
  {
    path: '',
    component: CabinetComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CabinetDetailComponent,
    resolve: {
      cabinet: CabinetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CabinetUpdateComponent,
    resolve: {
      cabinet: CabinetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CabinetUpdateComponent,
    resolve: {
      cabinet: CabinetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(cabinetRoute)],
  exports: [RouterModule],
})
export class CabinetRoutingModule {}
