import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRentalHistory, RentalHistory } from '../rental-history.model';
import { RentalHistoryService } from '../service/rental-history.service';

@Injectable({ providedIn: 'root' })
export class RentalHistoryRoutingResolveService implements Resolve<IRentalHistory> {
  constructor(protected service: RentalHistoryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRentalHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((rentalHistory: HttpResponse<RentalHistory>) => {
          if (rentalHistory.body) {
            return of(rentalHistory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RentalHistory());
  }
}
