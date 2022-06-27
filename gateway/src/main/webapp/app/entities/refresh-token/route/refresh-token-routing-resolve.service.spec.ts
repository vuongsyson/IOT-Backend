import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IRefreshToken, RefreshToken } from '../refresh-token.model';
import { RefreshTokenService } from '../service/refresh-token.service';

import { RefreshTokenRoutingResolveService } from './refresh-token-routing-resolve.service';

describe('RefreshToken routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: RefreshTokenRoutingResolveService;
  let service: RefreshTokenService;
  let resultRefreshToken: IRefreshToken | undefined;

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
    routingResolveService = TestBed.inject(RefreshTokenRoutingResolveService);
    service = TestBed.inject(RefreshTokenService);
    resultRefreshToken = undefined;
  });

  describe('resolve', () => {
    it('should return IRefreshToken returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRefreshToken = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRefreshToken).toEqual({ id: 123 });
    });

    it('should return new IRefreshToken if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRefreshToken = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultRefreshToken).toEqual(new RefreshToken());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as RefreshToken })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultRefreshToken = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultRefreshToken).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
