import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IVehicleState, VehicleState } from '../vehicle-state.model';
import { VehicleStateService } from '../service/vehicle-state.service';

@Injectable({ providedIn: 'root' })
export class VehicleStateRoutingResolveService implements Resolve<IVehicleState> {
  constructor(protected service: VehicleStateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IVehicleState> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((vehicleState: HttpResponse<VehicleState>) => {
          if (vehicleState.body) {
            return of(vehicleState.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new VehicleState());
  }
}
