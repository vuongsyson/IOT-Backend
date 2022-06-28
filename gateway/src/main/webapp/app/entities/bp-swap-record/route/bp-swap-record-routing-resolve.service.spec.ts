import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IBpSwapRecord, BpSwapRecord } from '../bp-swap-record.model';
import { BpSwapRecordService } from '../service/bp-swap-record.service';

import { BpSwapRecordRoutingResolveService } from './bp-swap-record-routing-resolve.service';

describe('BpSwapRecord routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BpSwapRecordRoutingResolveService;
  let service: BpSwapRecordService;
  let resultBpSwapRecord: IBpSwapRecord | undefined;

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
    routingResolveService = TestBed.inject(BpSwapRecordRoutingResolveService);
    service = TestBed.inject(BpSwapRecordService);
    resultBpSwapRecord = undefined;
  });

  describe('resolve', () => {
    it('should return IBpSwapRecord returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBpSwapRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBpSwapRecord).toEqual({ id: 123 });
    });

    it('should return new IBpSwapRecord if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBpSwapRecord = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBpSwapRecord).toEqual(new BpSwapRecord());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BpSwapRecord })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBpSwapRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBpSwapRecord).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
