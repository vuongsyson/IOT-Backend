import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISosRequest, getSosRequestIdentifier } from '../sos-request.model';

export type EntityResponseType = HttpResponse<ISosRequest>;
export type EntityArrayResponseType = HttpResponse<ISosRequest[]>;

@Injectable({ providedIn: 'root' })
export class SosRequestService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sos-requests');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sosRequest: ISosRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sosRequest);
    return this.http
      .post<ISosRequest>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(sosRequest: ISosRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sosRequest);
    return this.http
      .put<ISosRequest>(`${this.resourceUrl}/${getSosRequestIdentifier(sosRequest) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(sosRequest: ISosRequest): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(sosRequest);
    return this.http
      .patch<ISosRequest>(`${this.resourceUrl}/${getSosRequestIdentifier(sosRequest) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ISosRequest>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ISosRequest[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSosRequestToCollectionIfMissing(
    sosRequestCollection: ISosRequest[],
    ...sosRequestsToCheck: (ISosRequest | null | undefined)[]
  ): ISosRequest[] {
    const sosRequests: ISosRequest[] = sosRequestsToCheck.filter(isPresent);
    if (sosRequests.length > 0) {
      const sosRequestCollectionIdentifiers = sosRequestCollection.map(sosRequestItem => getSosRequestIdentifier(sosRequestItem)!);
      const sosRequestsToAdd = sosRequests.filter(sosRequestItem => {
        const sosRequestIdentifier = getSosRequestIdentifier(sosRequestItem);
        if (sosRequestIdentifier == null || sosRequestCollectionIdentifiers.includes(sosRequestIdentifier)) {
          return false;
        }
        sosRequestCollectionIdentifiers.push(sosRequestIdentifier);
        return true;
      });
      return [...sosRequestsToAdd, ...sosRequestCollection];
    }
    return sosRequestCollection;
  }

  protected convertDateFromClient(sosRequest: ISosRequest): ISosRequest {
    return Object.assign({}, sosRequest, {
      doneTime: sosRequest.doneTime?.isValid() ? sosRequest.doneTime.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.doneTime = res.body.doneTime ? dayjs(res.body.doneTime) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((sosRequest: ISosRequest) => {
        sosRequest.doneTime = sosRequest.doneTime ? dayjs(sosRequest.doneTime) : undefined;
      });
    }
    return res;
  }
}
