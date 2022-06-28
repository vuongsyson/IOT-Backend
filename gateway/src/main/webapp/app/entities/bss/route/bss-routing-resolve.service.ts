import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBss, Bss } from '../bss.model';
import { BssService } from '../service/bss.service';

@Injectable({ providedIn: 'root' })
export class BssRoutingResolveService implements Resolve<IBss> {
  constructor(protected service: BssService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBss> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bss: HttpResponse<Bss>) => {
          if (bss.body) {
            return of(bss.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Bss());
  }
}
