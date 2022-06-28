import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBattery, getBatteryIdentifier } from '../battery.model';

export type EntityResponseType = HttpResponse<IBattery>;
export type EntityArrayResponseType = HttpResponse<IBattery[]>;

@Injectable({ providedIn: 'root' })
export class BatteryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/batteries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(battery: IBattery): Observable<EntityResponseType> {
    return this.http.post<IBattery>(this.resourceUrl, battery, { observe: 'response' });
  }

  update(battery: IBattery): Observable<EntityResponseType> {
    return this.http.put<IBattery>(`${this.resourceUrl}/${getBatteryIdentifier(battery) as number}`, battery, { observe: 'response' });
  }

  partialUpdate(battery: IBattery): Observable<EntityResponseType> {
    return this.http.patch<IBattery>(`${this.resourceUrl}/${getBatteryIdentifier(battery) as number}`, battery, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBattery>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBattery[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBatteryToCollectionIfMissing(batteryCollection: IBattery[], ...batteriesToCheck: (IBattery | null | undefined)[]): IBattery[] {
    const batteries: IBattery[] = batteriesToCheck.filter(isPresent);
    if (batteries.length > 0) {
      const batteryCollectionIdentifiers = batteryCollection.map(batteryItem => getBatteryIdentifier(batteryItem)!);
      const batteriesToAdd = batteries.filter(batteryItem => {
        const batteryIdentifier = getBatteryIdentifier(batteryItem);
        if (batteryIdentifier == null || batteryCollectionIdentifiers.includes(batteryIdentifier)) {
          return false;
        }
        batteryCollectionIdentifiers.push(batteryIdentifier);
        return true;
      });
      return [...batteriesToAdd, ...batteryCollection];
    }
    return batteryCollection;
  }
}
