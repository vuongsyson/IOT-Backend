import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBpSwapRecord, getBpSwapRecordIdentifier } from '../bp-swap-record.model';

export type EntityResponseType = HttpResponse<IBpSwapRecord>;
export type EntityArrayResponseType = HttpResponse<IBpSwapRecord[]>;

@Injectable({ providedIn: 'root' })
export class BpSwapRecordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bp-swap-records');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bpSwapRecord: IBpSwapRecord): Observable<EntityResponseType> {
    return this.http.post<IBpSwapRecord>(this.resourceUrl, bpSwapRecord, { observe: 'response' });
  }

  update(bpSwapRecord: IBpSwapRecord): Observable<EntityResponseType> {
    return this.http.put<IBpSwapRecord>(`${this.resourceUrl}/${getBpSwapRecordIdentifier(bpSwapRecord) as number}`, bpSwapRecord, {
      observe: 'response',
    });
  }

  partialUpdate(bpSwapRecord: IBpSwapRecord): Observable<EntityResponseType> {
    return this.http.patch<IBpSwapRecord>(`${this.resourceUrl}/${getBpSwapRecordIdentifier(bpSwapRecord) as number}`, bpSwapRecord, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBpSwapRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBpSwapRecord[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBpSwapRecordToCollectionIfMissing(
    bpSwapRecordCollection: IBpSwapRecord[],
    ...bpSwapRecordsToCheck: (IBpSwapRecord | null | undefined)[]
  ): IBpSwapRecord[] {
    const bpSwapRecords: IBpSwapRecord[] = bpSwapRecordsToCheck.filter(isPresent);
    if (bpSwapRecords.length > 0) {
      const bpSwapRecordCollectionIdentifiers = bpSwapRecordCollection.map(
        bpSwapRecordItem => getBpSwapRecordIdentifier(bpSwapRecordItem)!
      );
      const bpSwapRecordsToAdd = bpSwapRecords.filter(bpSwapRecordItem => {
        const bpSwapRecordIdentifier = getBpSwapRecordIdentifier(bpSwapRecordItem);
        if (bpSwapRecordIdentifier == null || bpSwapRecordCollectionIdentifiers.includes(bpSwapRecordIdentifier)) {
          return false;
        }
        bpSwapRecordCollectionIdentifiers.push(bpSwapRecordIdentifier);
        return true;
      });
      return [...bpSwapRecordsToAdd, ...bpSwapRecordCollection];
    }
    return bpSwapRecordCollection;
  }
}
