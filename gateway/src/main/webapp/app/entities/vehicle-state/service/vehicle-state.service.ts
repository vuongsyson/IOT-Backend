import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicleState, getVehicleStateIdentifier } from '../vehicle-state.model';

export type EntityResponseType = HttpResponse<IVehicleState>;
export type EntityArrayResponseType = HttpResponse<IVehicleState[]>;

@Injectable({ providedIn: 'root' })
export class VehicleStateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicle-states');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vehicleState: IVehicleState): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleState);
    return this.http
      .post<IVehicleState>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vehicleState: IVehicleState): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleState);
    return this.http
      .put<IVehicleState>(`${this.resourceUrl}/${getVehicleStateIdentifier(vehicleState) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vehicleState: IVehicleState): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vehicleState);
    return this.http
      .patch<IVehicleState>(`${this.resourceUrl}/${getVehicleStateIdentifier(vehicleState) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVehicleState>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVehicleState[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVehicleStateToCollectionIfMissing(
    vehicleStateCollection: IVehicleState[],
    ...vehicleStatesToCheck: (IVehicleState | null | undefined)[]
  ): IVehicleState[] {
    const vehicleStates: IVehicleState[] = vehicleStatesToCheck.filter(isPresent);
    if (vehicleStates.length > 0) {
      const vehicleStateCollectionIdentifiers = vehicleStateCollection.map(
        vehicleStateItem => getVehicleStateIdentifier(vehicleStateItem)!
      );
      const vehicleStatesToAdd = vehicleStates.filter(vehicleStateItem => {
        const vehicleStateIdentifier = getVehicleStateIdentifier(vehicleStateItem);
        if (vehicleStateIdentifier == null || vehicleStateCollectionIdentifiers.includes(vehicleStateIdentifier)) {
          return false;
        }
        vehicleStateCollectionIdentifiers.push(vehicleStateIdentifier);
        return true;
      });
      return [...vehicleStatesToAdd, ...vehicleStateCollection];
    }
    return vehicleStateCollection;
  }

  protected convertDateFromClient(vehicleState: IVehicleState): IVehicleState {
    return Object.assign({}, vehicleState, {
      time: vehicleState.time?.isValid() ? vehicleState.time.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.time = res.body.time ? dayjs(res.body.time) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vehicleState: IVehicleState) => {
        vehicleState.time = vehicleState.time ? dayjs(vehicleState.time) : undefined;
      });
    }
    return res;
  }
}
