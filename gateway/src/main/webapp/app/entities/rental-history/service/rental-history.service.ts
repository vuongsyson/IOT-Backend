import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IRentalHistory, getRentalHistoryIdentifier } from '../rental-history.model';

export type EntityResponseType = HttpResponse<IRentalHistory>;
export type EntityArrayResponseType = HttpResponse<IRentalHistory[]>;

@Injectable({ providedIn: 'root' })
export class RentalHistoryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/rental-histories');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(rentalHistory: IRentalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rentalHistory);
    return this.http
      .post<IRentalHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(rentalHistory: IRentalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rentalHistory);
    return this.http
      .put<IRentalHistory>(`${this.resourceUrl}/${getRentalHistoryIdentifier(rentalHistory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(rentalHistory: IRentalHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(rentalHistory);
    return this.http
      .patch<IRentalHistory>(`${this.resourceUrl}/${getRentalHistoryIdentifier(rentalHistory) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IRentalHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IRentalHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addRentalHistoryToCollectionIfMissing(
    rentalHistoryCollection: IRentalHistory[],
    ...rentalHistoriesToCheck: (IRentalHistory | null | undefined)[]
  ): IRentalHistory[] {
    const rentalHistories: IRentalHistory[] = rentalHistoriesToCheck.filter(isPresent);
    if (rentalHistories.length > 0) {
      const rentalHistoryCollectionIdentifiers = rentalHistoryCollection.map(
        rentalHistoryItem => getRentalHistoryIdentifier(rentalHistoryItem)!
      );
      const rentalHistoriesToAdd = rentalHistories.filter(rentalHistoryItem => {
        const rentalHistoryIdentifier = getRentalHistoryIdentifier(rentalHistoryItem);
        if (rentalHistoryIdentifier == null || rentalHistoryCollectionIdentifiers.includes(rentalHistoryIdentifier)) {
          return false;
        }
        rentalHistoryCollectionIdentifiers.push(rentalHistoryIdentifier);
        return true;
      });
      return [...rentalHistoriesToAdd, ...rentalHistoryCollection];
    }
    return rentalHistoryCollection;
  }

  protected convertDateFromClient(rentalHistory: IRentalHistory): IRentalHistory {
    return Object.assign({}, rentalHistory, {
      timeStart: rentalHistory.timeStart?.isValid() ? rentalHistory.timeStart.toJSON() : undefined,
      timeEnd: rentalHistory.timeEnd?.isValid() ? rentalHistory.timeEnd.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeStart = res.body.timeStart ? dayjs(res.body.timeStart) : undefined;
      res.body.timeEnd = res.body.timeEnd ? dayjs(res.body.timeEnd) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((rentalHistory: IRentalHistory) => {
        rentalHistory.timeStart = rentalHistory.timeStart ? dayjs(rentalHistory.timeStart) : undefined;
        rentalHistory.timeEnd = rentalHistory.timeEnd ? dayjs(rentalHistory.timeEnd) : undefined;
      });
    }
    return res;
  }
}
