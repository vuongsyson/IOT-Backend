import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IUserAuth, getUserAuthIdentifier } from '../user-auth.model';

export type EntityResponseType = HttpResponse<IUserAuth>;
export type EntityArrayResponseType = HttpResponse<IUserAuth[]>;

@Injectable({ providedIn: 'root' })
export class UserAuthService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-auths');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(userAuth: IUserAuth): Observable<EntityResponseType> {
    return this.http.post<IUserAuth>(this.resourceUrl, userAuth, { observe: 'response' });
  }

  update(userAuth: IUserAuth): Observable<EntityResponseType> {
    return this.http.put<IUserAuth>(`${this.resourceUrl}/${getUserAuthIdentifier(userAuth) as number}`, userAuth, { observe: 'response' });
  }

  partialUpdate(userAuth: IUserAuth): Observable<EntityResponseType> {
    return this.http.patch<IUserAuth>(`${this.resourceUrl}/${getUserAuthIdentifier(userAuth) as number}`, userAuth, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserAuth>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IUserAuth[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addUserAuthToCollectionIfMissing(userAuthCollection: IUserAuth[], ...userAuthsToCheck: (IUserAuth | null | undefined)[]): IUserAuth[] {
    const userAuths: IUserAuth[] = userAuthsToCheck.filter(isPresent);
    if (userAuths.length > 0) {
      const userAuthCollectionIdentifiers = userAuthCollection.map(userAuthItem => getUserAuthIdentifier(userAuthItem)!);
      const userAuthsToAdd = userAuths.filter(userAuthItem => {
        const userAuthIdentifier = getUserAuthIdentifier(userAuthItem);
        if (userAuthIdentifier == null || userAuthCollectionIdentifiers.includes(userAuthIdentifier)) {
          return false;
        }
        userAuthCollectionIdentifiers.push(userAuthIdentifier);
        return true;
      });
      return [...userAuthsToAdd, ...userAuthCollection];
    }
    return userAuthCollection;
  }
}
