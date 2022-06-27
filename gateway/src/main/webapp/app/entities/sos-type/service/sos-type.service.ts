import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISosType, getSosTypeIdentifier } from '../sos-type.model';

export type EntityResponseType = HttpResponse<ISosType>;
export type EntityArrayResponseType = HttpResponse<ISosType[]>;

@Injectable({ providedIn: 'root' })
export class SosTypeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sos-types');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sosType: ISosType): Observable<EntityResponseType> {
    return this.http.post<ISosType>(this.resourceUrl, sosType, { observe: 'response' });
  }

  update(sosType: ISosType): Observable<EntityResponseType> {
    return this.http.put<ISosType>(`${this.resourceUrl}/${getSosTypeIdentifier(sosType) as number}`, sosType, { observe: 'response' });
  }

  partialUpdate(sosType: ISosType): Observable<EntityResponseType> {
    return this.http.patch<ISosType>(`${this.resourceUrl}/${getSosTypeIdentifier(sosType) as number}`, sosType, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISosType>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISosType[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSosTypeToCollectionIfMissing(sosTypeCollection: ISosType[], ...sosTypesToCheck: (ISosType | null | undefined)[]): ISosType[] {
    const sosTypes: ISosType[] = sosTypesToCheck.filter(isPresent);
    if (sosTypes.length > 0) {
      const sosTypeCollectionIdentifiers = sosTypeCollection.map(sosTypeItem => getSosTypeIdentifier(sosTypeItem)!);
      const sosTypesToAdd = sosTypes.filter(sosTypeItem => {
        const sosTypeIdentifier = getSosTypeIdentifier(sosTypeItem);
        if (sosTypeIdentifier == null || sosTypeCollectionIdentifiers.includes(sosTypeIdentifier)) {
          return false;
        }
        sosTypeCollectionIdentifiers.push(sosTypeIdentifier);
        return true;
      });
      return [...sosTypesToAdd, ...sosTypeCollection];
    }
    return sosTypeCollection;
  }
}
