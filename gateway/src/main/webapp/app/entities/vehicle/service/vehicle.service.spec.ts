import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IVehicle, Vehicle } from '../vehicle.model';

import { VehicleService } from './vehicle.service';

describe('Vehicle Service', () => {
  let service: VehicleService;
  let httpMock: HttpTestingController;
  let elemDefault: IVehicle;
  let expectedResult: IVehicle | IVehicle[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(VehicleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      clearance: 0,
      maxPower: 0,
      maxSpeed: 0,
      maxLoad: 0,
      weightTotal: 0,
      maxDistance: 0,
      wheelBase: 0,
      hwVersion: 0,
      swVersion: 0,
      serialNumber: 'AAAAAAA',
      manufactureDate: 'AAAAAAA',
      lotNumber: 0,
      color: 'AAAAAAA',
      vehicleType: 'AAAAAAA',
      used: false,
      userId: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Vehicle', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Vehicle()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Vehicle', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          clearance: 1,
          maxPower: 1,
          maxSpeed: 1,
          maxLoad: 1,
          weightTotal: 1,
          maxDistance: 1,
          wheelBase: 1,
          hwVersion: 1,
          swVersion: 1,
          serialNumber: 'BBBBBB',
          manufactureDate: 'BBBBBB',
          lotNumber: 1,
          color: 'BBBBBB',
          vehicleType: 'BBBBBB',
          used: true,
          userId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Vehicle', () => {
      const patchObject = Object.assign(
        {
          clearance: 1,
          maxPower: 1,
          maxDistance: 1,
          hwVersion: 1,
          manufactureDate: 'BBBBBB',
          color: 'BBBBBB',
          vehicleType: 'BBBBBB',
          used: true,
        },
        new Vehicle()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Vehicle', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          clearance: 1,
          maxPower: 1,
          maxSpeed: 1,
          maxLoad: 1,
          weightTotal: 1,
          maxDistance: 1,
          wheelBase: 1,
          hwVersion: 1,
          swVersion: 1,
          serialNumber: 'BBBBBB',
          manufactureDate: 'BBBBBB',
          lotNumber: 1,
          color: 'BBBBBB',
          vehicleType: 'BBBBBB',
          used: true,
          userId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Vehicle', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addVehicleToCollectionIfMissing', () => {
      it('should add a Vehicle to an empty array', () => {
        const vehicle: IVehicle = { id: 123 };
        expectedResult = service.addVehicleToCollectionIfMissing([], vehicle);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicle);
      });

      it('should not add a Vehicle to an array that contains it', () => {
        const vehicle: IVehicle = { id: 123 };
        const vehicleCollection: IVehicle[] = [
          {
            ...vehicle,
          },
          { id: 456 },
        ];
        expectedResult = service.addVehicleToCollectionIfMissing(vehicleCollection, vehicle);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Vehicle to an array that doesn't contain it", () => {
        const vehicle: IVehicle = { id: 123 };
        const vehicleCollection: IVehicle[] = [{ id: 456 }];
        expectedResult = service.addVehicleToCollectionIfMissing(vehicleCollection, vehicle);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicle);
      });

      it('should add only unique Vehicle to an array', () => {
        const vehicleArray: IVehicle[] = [{ id: 123 }, { id: 456 }, { id: 17409 }];
        const vehicleCollection: IVehicle[] = [{ id: 123 }];
        expectedResult = service.addVehicleToCollectionIfMissing(vehicleCollection, ...vehicleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const vehicle: IVehicle = { id: 123 };
        const vehicle2: IVehicle = { id: 456 };
        expectedResult = service.addVehicleToCollectionIfMissing([], vehicle, vehicle2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(vehicle);
        expect(expectedResult).toContain(vehicle2);
      });

      it('should accept null and undefined values', () => {
        const vehicle: IVehicle = { id: 123 };
        expectedResult = service.addVehicleToCollectionIfMissing([], null, vehicle, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(vehicle);
      });

      it('should return initial array if no Vehicle is added', () => {
        const vehicleCollection: IVehicle[] = [{ id: 123 }];
        expectedResult = service.addVehicleToCollectionIfMissing(vehicleCollection, undefined, null);
        expect(expectedResult).toEqual(vehicleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
