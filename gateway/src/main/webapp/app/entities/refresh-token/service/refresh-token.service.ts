import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRefreshToken, getRefreshTokenIdentifier } from '../refresh-token.model';

export type EntityResponseType = HttpResponse<IRefreshToken>;
export type EntityArrayResponseType = HttpResponse<IRefreshToken[]>;

@Injectable({ providedIn: 'root' })
export class RefreshTokenService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/refresh-tokens');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(refreshToken: IRefreshToken): Observable<EntityResponseType> {
    return this.http.post<IRefreshToken>(this.resourceUrl, refreshToken, { observe: 'response' });
  }

  update(refreshToken: IRefreshToken): Observable<EntityResponseType> {
    return this.http.put<IRefreshToken>(`${this.resourceUrl}/${getRefreshTokenIdentifier(refreshToken) as number}`, refreshToken, {
      observe: 'response',
    });
  }

  partialUpdate(refreshToken: IRefreshToken): Observable<EntityResponseType> {
    return this.http.patch<IRefreshToken>(`${this.resourceUrl}/${getRefreshTokenIdentifier(refreshToken) as number}`, refreshToken, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IRefreshToken>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IRefreshToken[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRefreshTokenToCollectionIfMissing(
    refreshTokenCollection: IRefreshToken[],
    ...refreshTokensToCheck: (IRefreshToken | null | undefined)[]
  ): IRefreshToken[] {
    const refreshTokens: IRefreshToken[] = refreshTokensToCheck.filter(isPresent);
    if (refreshTokens.length > 0) {
      const refreshTokenCollectionIdentifiers = refreshTokenCollection.map(
        refreshTokenItem => getRefreshTokenIdentifier(refreshTokenItem)!
      );
      const refreshTokensToAdd = refreshTokens.filter(refreshTokenItem => {
        const refreshTokenIdentifier = getRefreshTokenIdentifier(refreshTokenItem);
        if (refreshTokenIdentifier == null || refreshTokenCollectionIdentifiers.includes(refreshTokenIdentifier)) {
          return false;
        }
        refreshTokenCollectionIdentifiers.push(refreshTokenIdentifier);
        return true;
      });
      return [...refreshTokensToAdd, ...refreshTokenCollection];
    }
    return refreshTokenCollection;
  }
}
