import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBpSwapRecord, BpSwapRecord } from '../bp-swap-record.model';
import { BpSwapRecordService } from '../service/bp-swap-record.service';

@Injectable({ providedIn: 'root' })
export class BpSwapRecordRoutingResolveService implements Resolve<IBpSwapRecord> {
  constructor(protected service: BpSwapRecordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBpSwapRecord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bpSwapRecord: HttpResponse<BpSwapRecord>) => {
          if (bpSwapRecord.body) {
            return of(bpSwapRecord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BpSwapRecord());
  }
}
