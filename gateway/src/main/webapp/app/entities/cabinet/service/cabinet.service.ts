import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICabinet, getCabinetIdentifier } from '../cabinet.model';

export type EntityResponseType = HttpResponse<ICabinet>;
export type EntityArrayResponseType = HttpResponse<ICabinet[]>;

@Injectable({ providedIn: 'root' })
export class CabinetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/cabinets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(cabinet: ICabinet): Observable<EntityResponseType> {
    return this.http.post<ICabinet>(this.resourceUrl, cabinet, { observe: 'response' });
  }

  update(cabinet: ICabinet): Observable<EntityResponseType> {
    return this.http.put<ICabinet>(`${this.resourceUrl}/${getCabinetIdentifier(cabinet) as number}`, cabinet, { observe: 'response' });
  }

  partialUpdate(cabinet: ICabinet): Observable<EntityResponseType> {
    return this.http.patch<ICabinet>(`${this.resourceUrl}/${getCabinetIdentifier(cabinet) as number}`, cabinet, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICabinet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICabinet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCabinetToCollectionIfMissing(cabinetCollection: ICabinet[], ...cabinetsToCheck: (ICabinet | null | undefined)[]): ICabinet[] {
    const cabinets: ICabinet[] = cabinetsToCheck.filter(isPresent);
    if (cabinets.length > 0) {
      const cabinetCollectionIdentifiers = cabinetCollection.map(cabinetItem => getCabinetIdentifier(cabinetItem)!);
      const cabinetsToAdd = cabinets.filter(cabinetItem => {
        const cabinetIdentifier = getCabinetIdentifier(cabinetItem);
        if (cabinetIdentifier == null || cabinetCollectionIdentifiers.includes(cabinetIdentifier)) {
          return false;
        }
        cabinetCollectionIdentifiers.push(cabinetIdentifier);
        return true;
      });
      return [...cabinetsToAdd, ...cabinetCollection];
    }
    return cabinetCollection;
  }
}
