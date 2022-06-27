import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrg, Org } from '../org.model';
import { OrgService } from '../service/org.service';

@Injectable({ providedIn: 'root' })
export class OrgRoutingResolveService implements Resolve<IOrg> {
  constructor(protected service: OrgService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrg> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((org: HttpResponse<Org>) => {
          if (org.body) {
            return of(org.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Org());
  }
}
