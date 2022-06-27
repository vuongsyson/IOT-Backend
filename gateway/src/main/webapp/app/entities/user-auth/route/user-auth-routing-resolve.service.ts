import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserAuth, UserAuth } from '../user-auth.model';
import { UserAuthService } from '../service/user-auth.service';

@Injectable({ providedIn: 'root' })
export class UserAuthRoutingResolveService implements Resolve<IUserAuth> {
  constructor(protected service: UserAuthService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IUserAuth> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((userAuth: HttpResponse<UserAuth>) => {
          if (userAuth.body) {
            return of(userAuth.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new UserAuth());
  }
}
