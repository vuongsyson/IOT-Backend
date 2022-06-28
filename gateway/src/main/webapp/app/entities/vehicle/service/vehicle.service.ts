import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVehicle, getVehicleIdentifier } from '../vehicle.model';

export type EntityResponseType = HttpResponse<IVehicle>;
export type EntityArrayResponseType = HttpResponse<IVehicle[]>;

@Injectable({ providedIn: 'root' })
export class VehicleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vehicles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vehicle: IVehicle): Observable<EntityResponseType> {
    return this.http.post<IVehicle>(this.resourceUrl, vehicle, { observe: 'response' });
  }

  update(vehicle: IVehicle): Observable<EntityResponseType> {
    return this.http.put<IVehicle>(`${this.resourceUrl}/${getVehicleIdentifier(vehicle) as number}`, vehicle, { observe: 'response' });
  }

  partialUpdate(vehicle: IVehicle): Observable<EntityResponseType> {
    return this.http.patch<IVehicle>(`${this.resourceUrl}/${getVehicleIdentifier(vehicle) as number}`, vehicle, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IVehicle>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IVehicle[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVehicleToCollectionIfMissing(vehicleCollection: IVehicle[], ...vehiclesToCheck: (IVehicle | null | undefined)[]): IVehicle[] {
    const vehicles: IVehicle[] = vehiclesToCheck.filter(isPresent);
    if (vehicles.length > 0) {
      const vehicleCollectionIdentifiers = vehicleCollection.map(vehicleItem => getVehicleIdentifier(vehicleItem)!);
      const vehiclesToAdd = vehicles.filter(vehicleItem => {
        const vehicleIdentifier = getVehicleIdentifier(vehicleItem);
        if (vehicleIdentifier == null || vehicleCollectionIdentifiers.includes(vehicleIdentifier)) {
          return false;
        }
        vehicleCollectionIdentifiers.push(vehicleIdentifier);
        return true;
      });
      return [...vehiclesToAdd, ...vehicleCollection];
    }
    return vehicleCollection;
  }
}
