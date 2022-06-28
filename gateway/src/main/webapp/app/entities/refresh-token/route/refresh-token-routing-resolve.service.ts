import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IRefreshToken, RefreshToken } from '../refresh-token.model';
import { RefreshTokenService } from '../service/refresh-token.service';

@Injectable({ providedIn: 'root' })
export class RefreshTokenRoutingResolveService implements Resolve<IRefreshToken> {
  constructor(protected service: RefreshTokenService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IRefreshToken> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((refreshToken: HttpResponse<RefreshToken>) => {
          if (refreshToken.body) {
            return of(refreshToken.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new RefreshToken());
  }
}
