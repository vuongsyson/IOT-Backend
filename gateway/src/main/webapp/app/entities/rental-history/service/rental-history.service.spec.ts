import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRentalHistory, RentalHistory } from '../rental-history.model';

import { RentalHistoryService } from './rental-history.service';

describe('RentalHistory Service', () => {
  let service: RentalHistoryService;
  let httpMock: HttpTestingController;
  let elemDefault: IRentalHistory;
  let expectedResult: IRentalHistory | IRentalHistory[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RentalHistoryService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      userId: 0,
      batteryId: 0,
      timeStart: currentDate,
      timeEnd: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeStart: currentDate.format(DATE_TIME_FORMAT),
          timeEnd: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a RentalHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeStart: currentDate.format(DATE_TIME_FORMAT),
          timeEnd: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeStart: currentDate,
          timeEnd: currentDate,
        },
        returnedFromService
      );

      service.create(new RentalHistory()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RentalHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          userId: 1,
          batteryId: 1,
          timeStart: currentDate.format(DATE_TIME_FORMAT),
          timeEnd: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeStart: currentDate,
          timeEnd: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RentalHistory', () => {
      const patchObject = Object.assign(
        {
          userId: 1,
          batteryId: 1,
          timeStart: currentDate.format(DATE_TIME_FORMAT),
        },
        new RentalHistory()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeStart: currentDate,
          timeEnd: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RentalHistory', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          userId: 1,
          batteryId: 1,
          timeStart: currentDate.format(DATE_TIME_FORMAT),
          timeEnd: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeStart: currentDate,
          timeEnd: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a RentalHistory', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRentalHistoryToCollectionIfMissing', () => {
      it('should add a RentalHistory to an empty array', () => {
        const rentalHistory: IRentalHistory = { id: 123 };
        expectedResult = service.addRentalHistoryToCollectionIfMissing([], rentalHistory);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rentalHistory);
      });

      it('should not add a RentalHistory to an array that contains it', () => {
        const rentalHistory: IRentalHistory = { id: 123 };
        const rentalHistoryCollection: IRentalHistory[] = [
          {
            ...rentalHistory,
          },
          { id: 456 },
        ];
        expectedResult = service.addRentalHistoryToCollectionIfMissing(rentalHistoryCollection, rentalHistory);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RentalHistory to an array that doesn't contain it", () => {
        const rentalHistory: IRentalHistory = { id: 123 };
        const rentalHistoryCollection: IRentalHistory[] = [{ id: 456 }];
        expectedResult = service.addRentalHistoryToCollectionIfMissing(rentalHistoryCollection, rentalHistory);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rentalHistory);
      });

      it('should add only unique RentalHistory to an array', () => {
        const rentalHistoryArray: IRentalHistory[] = [{ id: 123 }, { id: 456 }, { id: 77679 }];
        const rentalHistoryCollection: IRentalHistory[] = [{ id: 123 }];
        expectedResult = service.addRentalHistoryToCollectionIfMissing(rentalHistoryCollection, ...rentalHistoryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const rentalHistory: IRentalHistory = { id: 123 };
        const rentalHistory2: IRentalHistory = { id: 456 };
        expectedResult = service.addRentalHistoryToCollectionIfMissing([], rentalHistory, rentalHistory2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(rentalHistory);
        expect(expectedResult).toContain(rentalHistory2);
      });

      it('should accept null and undefined values', () => {
        const rentalHistory: IRentalHistory = { id: 123 };
        expectedResult = service.addRentalHistoryToCollectionIfMissing([], null, rentalHistory, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(rentalHistory);
      });

      it('should return initial array if no RentalHistory is added', () => {
        const rentalHistoryCollection: IRentalHistory[] = [{ id: 123 }];
        expectedResult = service.addRentalHistoryToCollectionIfMissing(rentalHistoryCollection, undefined, null);
        expect(expectedResult).toEqual(rentalHistoryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
