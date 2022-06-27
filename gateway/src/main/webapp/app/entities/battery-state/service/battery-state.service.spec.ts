import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBatteryState, BatteryState } from '../battery-state.model';

import { BatteryStateService } from './battery-state.service';

describe('BatteryState Service', () => {
  let service: BatteryStateService;
  let httpMock: HttpTestingController;
  let elemDefault: IBatteryState;
  let expectedResult: IBatteryState | IBatteryState[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BatteryStateService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      serialNumber: 'AAAAAAA',
      vol: 0,
      cur: 0,
      soc: 0,
      soh: 0,
      state: 0,
      status: 0,
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

    it('should create a BatteryState', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BatteryState()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BatteryState', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          serialNumber: 'BBBBBB',
          vol: 1,
          cur: 1,
          soc: 1,
          soh: 1,
          state: 1,
          status: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BatteryState', () => {
      const patchObject = Object.assign(
        {
          serialNumber: 'BBBBBB',
          soc: 1,
          status: 1,
        },
        new BatteryState()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BatteryState', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          serialNumber: 'BBBBBB',
          vol: 1,
          cur: 1,
          soc: 1,
          soh: 1,
          state: 1,
          status: 1,
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

    it('should delete a BatteryState', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBatteryStateToCollectionIfMissing', () => {
      it('should add a BatteryState to an empty array', () => {
        const batteryState: IBatteryState = { id: 123 };
        expectedResult = service.addBatteryStateToCollectionIfMissing([], batteryState);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(batteryState);
      });

      it('should not add a BatteryState to an array that contains it', () => {
        const batteryState: IBatteryState = { id: 123 };
        const batteryStateCollection: IBatteryState[] = [
          {
            ...batteryState,
          },
          { id: 456 },
        ];
        expectedResult = service.addBatteryStateToCollectionIfMissing(batteryStateCollection, batteryState);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BatteryState to an array that doesn't contain it", () => {
        const batteryState: IBatteryState = { id: 123 };
        const batteryStateCollection: IBatteryState[] = [{ id: 456 }];
        expectedResult = service.addBatteryStateToCollectionIfMissing(batteryStateCollection, batteryState);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(batteryState);
      });

      it('should add only unique BatteryState to an array', () => {
        const batteryStateArray: IBatteryState[] = [{ id: 123 }, { id: 456 }, { id: 95459 }];
        const batteryStateCollection: IBatteryState[] = [{ id: 123 }];
        expectedResult = service.addBatteryStateToCollectionIfMissing(batteryStateCollection, ...batteryStateArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const batteryState: IBatteryState = { id: 123 };
        const batteryState2: IBatteryState = { id: 456 };
        expectedResult = service.addBatteryStateToCollectionIfMissing([], batteryState, batteryState2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(batteryState);
        expect(expectedResult).toContain(batteryState2);
      });

      it('should accept null and undefined values', () => {
        const batteryState: IBatteryState = { id: 123 };
        expectedResult = service.addBatteryStateToCollectionIfMissing([], null, batteryState, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(batteryState);
      });

      it('should return initial array if no BatteryState is added', () => {
        const batteryStateCollection: IBatteryState[] = [{ id: 123 }];
        expectedResult = service.addBatteryStateToCollectionIfMissing(batteryStateCollection, undefined, null);
        expect(expectedResult).toEqual(batteryStateCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
