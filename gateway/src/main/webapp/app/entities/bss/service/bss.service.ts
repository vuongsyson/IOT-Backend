import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBss, getBssIdentifier } from '../bss.model';

export type EntityResponseType = HttpResponse<IBss>;
export type EntityArrayResponseType = HttpResponse<IBss[]>;

@Injectable({ providedIn: 'root' })
export class BssService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bsses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bss: IBss): Observable<EntityResponseType> {
    return this.http.post<IBss>(this.resourceUrl, bss, { observe: 'response' });
  }

  update(bss: IBss): Observable<EntityResponseType> {
    return this.http.put<IBss>(`${this.resourceUrl}/${getBssIdentifier(bss) as number}`, bss, { observe: 'response' });
  }

  partialUpdate(bss: IBss): Observable<EntityResponseType> {
    return this.http.patch<IBss>(`${this.resourceUrl}/${getBssIdentifier(bss) as number}`, bss, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBss>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBss[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBssToCollectionIfMissing(bssCollection: IBss[], ...bssesToCheck: (IBss | null | undefined)[]): IBss[] {
    const bsses: IBss[] = bssesToCheck.filter(isPresent);
    if (bsses.length > 0) {
      const bssCollectionIdentifiers = bssCollection.map(bssItem => getBssIdentifier(bssItem)!);
      const bssesToAdd = bsses.filter(bssItem => {
        const bssIdentifier = getBssIdentifier(bssItem);
        if (bssIdentifier == null || bssCollectionIdentifiers.includes(bssIdentifier)) {
          return false;
        }
        bssCollectionIdentifiers.push(bssIdentifier);
        return true;
      });
      return [...bssesToAdd, ...bssCollection];
    }
    return bssCollection;
  }
}
