import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IVehicleState, VehicleState } from '../vehicle-state.model';

import { VehicleStateService } from './vehicle-state.service';

describe('VehicleState Service', () => {
  let service: VehicleStateService;
  let httpMock: HttpTestingController;
  let elemDefault: IVehicleState;
  let expectedResult: IVehicleState | IVehicleState[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VehicleStateService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      speed: 0,
      lat: 0,
      lon: 0,
      error: 0,
      serialNumber: 'AAAAAAA',
      status: 'AAAAAAA',
      odo: 0,
      power: 0,
      throttle: 0,
      time: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          time: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a VehicleState', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          time: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          time: currentDate,
        },
        returnedFromService
      );

      service.create(new VehicleState()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a VehicleState', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          speed: 1,
          lat: 1,
          lon: 1,
          error: 1,
          serialNumber: 'BBBBBB',
          status: 'BBBBBB',
          odo: 1,
          power: 1,
          throttle: 1,
          time: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          time: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a VehicleState', () => {
      const patchObject = Object.assign(
        {
          lat: 1,
          serialNumber: 'BBBBBB',
          status: 'BBBBBB',
          odo: 1,
          power: 1,
          time: currentDate.format(DATE_TIME_FORMAT),
        },
        new VehicleState()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          time: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of VehicleState', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          speed: 1,
          lat: 1,
          lon: 1,
          error: 1,
          serialNumber: 'BBBBBB',
          status: 'BBBBBB',
          odo: 1,
          power: 1,
          throttle: 1,
          time: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          time: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a VehicleState', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVehicleStateToCollectionIfMissing', () => {
      it('should add a VehicleState to an empty array', () => {
        const vehicleState: IVehicleState = { id: 123 };
        expectedResult = service.addVehicleStateToCollectionIfMissing([], vehicleState);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleState);
      });

      it('should not add a VehicleState to an array that contains it', () => {
        const vehicleState: IVehicleState = { id: 123 };
        const vehicleStateCollection: IVehicleState[] = [
          {
            ...vehicleState,
          },
          { id: 456 },
        ];
        expectedResult = service.addVehicleStateToCollectionIfMissing(vehicleStateCollection, vehicleState);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a VehicleState to an array that doesn't contain it", () => {
        const vehicleState: IVehicleState = { id: 123 };
        const vehicleStateCollection: IVehicleState[] = [{ id: 456 }];
        expectedResult = service.addVehicleStateToCollectionIfMissing(vehicleStateCollection, vehicleState);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleState);
      });

      it('should add only unique VehicleState to an array', () => {
        const vehicleStateArray: IVehicleState[] = [{ id: 123 }, { id: 456 }, { id: 56931 }];
        const vehicleStateCollection: IVehicleState[] = [{ id: 123 }];
        expectedResult = service.addVehicleStateToCollectionIfMissing(vehicleStateCollection, ...vehicleStateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicleState: IVehicleState = { id: 123 };
        const vehicleState2: IVehicleState = { id: 456 };
        expectedResult = service.addVehicleStateToCollectionIfMissing([], vehicleState, vehicleState2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicleState);
        expect(expectedResult).toContain(vehicleState2);
      });

      it('should accept null and undefined values', () => {
        const vehicleState: IVehicleState = { id: 123 };
        expectedResult = service.addVehicleStateToCollectionIfMissing([], null, vehicleState, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicleState);
      });

      it('should return initial array if no VehicleState is added', () => {
        const vehicleStateCollection: IVehicleState[] = [{ id: 123 }];
        expectedResult = service.addVehicleStateToCollectionIfMissing(vehicleStateCollection, undefined, null);
        expect(expectedResult).toEqual(vehicleStateCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
