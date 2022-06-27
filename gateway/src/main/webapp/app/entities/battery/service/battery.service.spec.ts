import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBattery, Battery } from '../battery.model';

import { BatteryService } from './battery.service';

describe('Battery Service', () => {
  let service: BatteryService;
  let httpMock: HttpTestingController;
  let elemDefault: IBattery;
  let expectedResult: IBattery | IBattery[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BatteryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      serialNo: 'AAAAAAA',
      hwVersion: 0,
      swVersion: 0,
      manufactureDate: 'AAAAAAA',
      capacity: 0,
      maxCharge: 0,
      maxDiscarge: 0,
      maxVol: 0,
      minVol: 0,
      used: false,
      soc: 0,
      soh: 0,
      temp: 0,
      ownerId: 0,
      renterId: 0,
      cycleCount: 0,
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

    it('should create a Battery', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Battery()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Battery', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          serialNo: 'BBBBBB',
          hwVersion: 1,
          swVersion: 1,
          manufactureDate: 'BBBBBB',
          capacity: 1,
          maxCharge: 1,
          maxDiscarge: 1,
          maxVol: 1,
          minVol: 1,
          used: true,
          soc: 1,
          soh: 1,
          temp: 1,
          ownerId: 1,
          renterId: 1,
          cycleCount: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Battery', () => {
      const patchObject = Object.assign(
        {
          hwVersion: 1,
          manufactureDate: 'BBBBBB',
          capacity: 1,
          minVol: 1,
          soc: 1,
          renterId: 1,
        },
        new Battery()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Battery', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          serialNo: 'BBBBBB',
          hwVersion: 1,
          swVersion: 1,
          manufactureDate: 'BBBBBB',
          capacity: 1,
          maxCharge: 1,
          maxDiscarge: 1,
          maxVol: 1,
          minVol: 1,
          used: true,
          soc: 1,
          soh: 1,
          temp: 1,
          ownerId: 1,
          renterId: 1,
          cycleCount: 1,
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

    it('should delete a Battery', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBatteryToCollectionIfMissing', () => {
      it('should add a Battery to an empty array', () => {
        const battery: IBattery = { id: 123 };
        expectedResult = service.addBatteryToCollectionIfMissing([], battery);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(battery);
      });

      it('should not add a Battery to an array that contains it', () => {
        const battery: IBattery = { id: 123 };
        const batteryCollection: IBattery[] = [
          {
            ...battery,
          },
          { id: 456 },
        ];
        expectedResult = service.addBatteryToCollectionIfMissing(batteryCollection, battery);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Battery to an array that doesn't contain it", () => {
        const battery: IBattery = { id: 123 };
        const batteryCollection: IBattery[] = [{ id: 456 }];
        expectedResult = service.addBatteryToCollectionIfMissing(batteryCollection, battery);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(battery);
      });

      it('should add only unique Battery to an array', () => {
        const batteryArray: IBattery[] = [{ id: 123 }, { id: 456 }, { id: 18244 }];
        const batteryCollection: IBattery[] = [{ id: 123 }];
        expectedResult = service.addBatteryToCollectionIfMissing(batteryCollection, ...batteryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const battery: IBattery = { id: 123 };
        const battery2: IBattery = { id: 456 };
        expectedResult = service.addBatteryToCollectionIfMissing([], battery, battery2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(battery);
        expect(expectedResult).toContain(battery2);
      });

      it('should accept null and undefined values', () => {
        const battery: IBattery = { id: 123 };
        expectedResult = service.addBatteryToCollectionIfMissing([], null, battery, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(battery);
      });

      it('should return initial array if no Battery is added', () => {
        const batteryCollection: IBattery[] = [{ id: 123 }];
        expectedResult = service.addBatteryToCollectionIfMissing(batteryCollection, undefined, null);
        expect(expectedResult).toEqual(batteryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
