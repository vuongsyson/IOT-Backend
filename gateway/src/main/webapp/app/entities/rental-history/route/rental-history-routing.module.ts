import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RentalHistoryComponent } from '../list/rental-history.component';
import { RentalHistoryDetailComponent } from '../detail/rental-history-detail.component';
import { RentalHistoryUpdateComponent } from '../update/rental-history-update.component';
import { RentalHistoryRoutingResolveService } from './rental-history-routing-resolve.service';

const rentalHistoryRoute: Routes = [
  {
    path: '',
    component: RentalHistoryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RentalHistoryDetailComponent,
    resolve: {
      rentalHistory: RentalHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RentalHistoryUpdateComponent,
    resolve: {
      rentalHistory: RentalHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RentalHistoryUpdateComponent,
    resolve: {
      rentalHistory: RentalHistoryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(rentalHistoryRoute)],
  exports: [RouterModule],
})
export class RentalHistoryRoutingModule {}
