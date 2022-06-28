import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrgComponent } from '../list/org.component';
import { OrgDetailComponent } from '../detail/org-detail.component';
import { OrgUpdateComponent } from '../update/org-update.component';
import { OrgRoutingResolveService } from './org-routing-resolve.service';

const orgRoute: Routes = [
  {
    path: '',
    component: OrgComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrgDetailComponent,
    resolve: {
      org: OrgRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrgUpdateComponent,
    resolve: {
      org: OrgRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrgUpdateComponent,
    resolve: {
      org: OrgRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orgRoute)],
  exports: [RouterModule],
})
export class OrgRoutingModule {}
