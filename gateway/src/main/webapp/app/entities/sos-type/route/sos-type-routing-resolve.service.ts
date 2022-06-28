import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISosType, SosType } from '../sos-type.model';
import { SosTypeService } from '../service/sos-type.service';

@Injectable({ providedIn: 'root' })
export class SosTypeRoutingResolveService implements Resolve<ISosType> {
  constructor(protected service: SosTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISosType> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sosType: HttpResponse<SosType>) => {
          if (sosType.body) {
            return of(sosType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SosType());
  }
}
