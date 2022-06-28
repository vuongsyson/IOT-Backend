import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IBatteryState, BatteryState } from '../battery-state.model';
import { BatteryStateService } from '../service/battery-state.service';

import { BatteryStateRoutingResolveService } from './battery-state-routing-resolve.service';

describe('BatteryState routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BatteryStateRoutingResolveService;
  let service: BatteryStateService;
  let resultBatteryState: IBatteryState | undefined;

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
    routingResolveService = TestBed.inject(BatteryStateRoutingResolveService);
    service = TestBed.inject(BatteryStateService);
    resultBatteryState = undefined;
  });

  describe('resolve', () => {
    it('should return IBatteryState returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBatteryState = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBatteryState).toEqual({ id: 123 });
    });

    it('should return new IBatteryState if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBatteryState = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBatteryState).toEqual(new BatteryState());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BatteryState })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBatteryState = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBatteryState).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
