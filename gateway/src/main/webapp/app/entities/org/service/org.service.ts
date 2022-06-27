import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrg, getOrgIdentifier } from '../org.model';

export type EntityResponseType = HttpResponse<IOrg>;
export type EntityArrayResponseType = HttpResponse<IOrg[]>;

@Injectable({ providedIn: 'root' })
export class OrgService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/orgs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(org: IOrg): Observable<EntityResponseType> {
    return this.http.post<IOrg>(this.resourceUrl, org, { observe: 'response' });
  }

  update(org: IOrg): Observable<EntityResponseType> {
    return this.http.put<IOrg>(`${this.resourceUrl}/${getOrgIdentifier(org) as number}`, org, { observe: 'response' });
  }

  partialUpdate(org: IOrg): Observable<EntityResponseType> {
    return this.http.patch<IOrg>(`${this.resourceUrl}/${getOrgIdentifier(org) as number}`, org, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrg>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrg[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addOrgToCollectionIfMissing(orgCollection: IOrg[], ...orgsToCheck: (IOrg | null | undefined)[]): IOrg[] {
    const orgs: IOrg[] = orgsToCheck.filter(isPresent);
    if (orgs.length > 0) {
      const orgCollectionIdentifiers = orgCollection.map(orgItem => getOrgIdentifier(orgItem)!);
      const orgsToAdd = orgs.filter(orgItem => {
        const orgIdentifier = getOrgIdentifier(orgItem);
        if (orgIdentifier == null || orgCollectionIdentifiers.includes(orgIdentifier)) {
          return false;
        }
        orgCollectionIdentifiers.push(orgIdentifier);
        return true;
      });
      return [...orgsToAdd, ...orgCollection];
    }
    return orgCollection;
  }
}
