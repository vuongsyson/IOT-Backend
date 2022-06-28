import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISosRequest, SosRequest } from '../sos-request.model';
import { SosRequestService } from '../service/sos-request.service';

@Injectable({ providedIn: 'root' })
export class SosRequestRoutingResolveService implements Resolve<ISosRequest> {
  constructor(protected service: SosRequestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISosRequest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sosRequest: HttpResponse<SosRequest>) => {
          if (sosRequest.body) {
            return of(sosRequest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SosRequest());
  }
}
