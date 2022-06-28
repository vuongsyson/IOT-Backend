import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRentalHistory, RentalHistory } from '../rental-history.model';
import { RentalHistoryService } from '../service/rental-history.service';

import { RentalHistoryRoutingResolveService } from './rental-history-routing-resolve.service';

describe('RentalHistory routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RentalHistoryRoutingResolveService;
  let service: RentalHistoryService;
  let resultRentalHistory: IRentalHistory | undefined;

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
    routingResolveService = TestBed.inject(RentalHistoryRoutingResolveService);
    service = TestBed.inject(RentalHistoryService);
    resultRentalHistory = undefined;
  });

  describe('resolve', () => {
    it('should return IRentalHistory returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRentalHistory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRentalHistory).toEqual({ id: 123 });
    });

    it('should return new IRentalHistory if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRentalHistory = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRentalHistory).toEqual(new RentalHistory());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RentalHistory })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRentalHistory = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRentalHistory).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
