import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { SosState } from 'app/entities/enumerations/sos-state.model';
import { ISosRequest, SosRequest } from '../sos-request.model';

import { SosRequestService } from './sos-request.service';

describe('SosRequest Service', () => {
  let service: SosRequestService;
  let httpMock: HttpTestingController;
  let elemDefault: ISosRequest;
  let expectedResult: ISosRequest | ISosRequest[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SosRequestService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      userId: 0,
      phone: 'AAAAAAA',
      deviceSerialNumber: 'AAAAAAA',
      description: 'AAAAAAA',
      image: 'AAAAAAA',
      state: SosState.SEND_REQUEST,
      rating: 0,
      done: false,
      doneTime: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          doneTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a SosRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          doneTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          doneTime: currentDate,
        },
        returnedFromService
      );

      service.create(new SosRequest()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SosRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          userId: 1,
          phone: 'BBBBBB',
          deviceSerialNumber: 'BBBBBB',
          description: 'BBBBBB',
          image: 'BBBBBB',
          state: 'BBBBBB',
          rating: 1,
          done: true,
          doneTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          doneTime: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SosRequest', () => {
      const patchObject = Object.assign(
        {
          userId: 1,
          image: 'BBBBBB',
          rating: 1,
        },
        new SosRequest()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          doneTime: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SosRequest', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          userId: 1,
          phone: 'BBBBBB',
          deviceSerialNumber: 'BBBBBB',
          description: 'BBBBBB',
          image: 'BBBBBB',
          state: 'BBBBBB',
          rating: 1,
          done: true,
          doneTime: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          doneTime: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a SosRequest', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSosRequestToCollectionIfMissing', () => {
      it('should add a SosRequest to an empty array', () => {
        const sosRequest: ISosRequest = { id: 123 };
        expectedResult = service.addSosRequestToCollectionIfMissing([], sosRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sosRequest);
      });

      it('should not add a SosRequest to an array that contains it', () => {
        const sosRequest: ISosRequest = { id: 123 };
        const sosRequestCollection: ISosRequest[] = [
          {
            ...sosRequest,
          },
          { id: 456 },
        ];
        expectedResult = service.addSosRequestToCollectionIfMissing(sosRequestCollection, sosRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SosRequest to an array that doesn't contain it", () => {
        const sosRequest: ISosRequest = { id: 123 };
        const sosRequestCollection: ISosRequest[] = [{ id: 456 }];
        expectedResult = service.addSosRequestToCollectionIfMissing(sosRequestCollection, sosRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sosRequest);
      });

      it('should add only unique SosRequest to an array', () => {
        const sosRequestArray: ISosRequest[] = [{ id: 123 }, { id: 456 }, { id: 23709 }];
        const sosRequestCollection: ISosRequest[] = [{ id: 123 }];
        expectedResult = service.addSosRequestToCollectionIfMissing(sosRequestCollection, ...sosRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sosRequest: ISosRequest = { id: 123 };
        const sosRequest2: ISosRequest = { id: 456 };
        expectedResult = service.addSosRequestToCollectionIfMissing([], sosRequest, sosRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sosRequest);
        expect(expectedResult).toContain(sosRequest2);
      });

      it('should accept null and undefined values', () => {
        const sosRequest: ISosRequest = { id: 123 };
        expectedResult = service.addSosRequestToCollectionIfMissing([], null, sosRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sosRequest);
      });

      it('should return initial array if no SosRequest is added', () => {
        const sosRequestCollection: ISosRequest[] = [{ id: 123 }];
        expectedResult = service.addSosRequestToCollectionIfMissing(sosRequestCollection, undefined, null);
        expect(expectedResult).toEqual(sosRequestCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
