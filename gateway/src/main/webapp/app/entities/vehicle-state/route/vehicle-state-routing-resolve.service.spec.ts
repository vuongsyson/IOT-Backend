import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IVehicleState, VehicleState } from '../vehicle-state.model';
import { VehicleStateService } from '../service/vehicle-state.service';

import { VehicleStateRoutingResolveService } from './vehicle-state-routing-resolve.service';

describe('VehicleState routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: VehicleStateRoutingResolveService;
  let service: VehicleStateService;
  let resultVehicleState: IVehicleState | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(VehicleStateRoutingResolveService);
    service = TestBed.inject(VehicleStateService);
    resultVehicleState = undefined;
  });

  describe('resolve', () => {
    it('should return IVehicleState returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleState = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVehicleState).toEqual({ id: 123 });
    });

    it('should return new IVehicleState if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleState = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultVehicleState).toEqual(new VehicleState());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as VehicleState })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultVehicleState = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultVehicleState).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
