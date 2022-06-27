import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBatteryState, getBatteryStateIdentifier } from '../battery-state.model';

export type EntityResponseType = HttpResponse<IBatteryState>;
export type EntityArrayResponseType = HttpResponse<IBatteryState[]>;

@Injectable({ providedIn: 'root' })
export class BatteryStateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/battery-states');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(batteryState: IBatteryState): Observable<EntityResponseType> {
    return this.http.post<IBatteryState>(this.resourceUrl, batteryState, { observe: 'response' });
  }

  update(batteryState: IBatteryState): Observable<EntityResponseType> {
    return this.http.put<IBatteryState>(`${this.resourceUrl}/${getBatteryStateIdentifier(batteryState) as number}`, batteryState, {
      observe: 'response',
    });
  }

  partialUpdate(batteryState: IBatteryState): Observable<EntityResponseType> {
    return this.http.patch<IBatteryState>(`${this.resourceUrl}/${getBatteryStateIdentifier(batteryState) as number}`, batteryState, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBatteryState>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBatteryState[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBatteryStateToCollectionIfMissing(
    batteryStateCollection: IBatteryState[],
    ...batteryStatesToCheck: (IBatteryState | null | undefined)[]
  ): IBatteryState[] {
    const batteryStates: IBatteryState[] = batteryStatesToCheck.filter(isPresent);
    if (batteryStates.length > 0) {
      const batteryStateCollectionIdentifiers = batteryStateCollection.map(
        batteryStateItem => getBatteryStateIdentifier(batteryStateItem)!
      );
      const batteryStatesToAdd = batteryStates.filter(batteryStateItem => {
        const batteryStateIdentifier = getBatteryStateIdentifier(batteryStateItem);
        if (batteryStateIdentifier == null || batteryStateCollectionIdentifiers.includes(batteryStateIdentifier)) {
          return false;
        }
        batteryStateCollectionIdentifiers.push(batteryStateIdentifier);
        return true;
      });
      return [...batteryStatesToAdd, ...batteryStateCollection];
    }
    return batteryStateCollection;
  }
}
