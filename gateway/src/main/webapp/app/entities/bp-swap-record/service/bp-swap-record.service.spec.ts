import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBpSwapRecord, BpSwapRecord } from '../bp-swap-record.model';

import { BpSwapRecordService } from './bp-swap-record.service';

describe('BpSwapRecord Service', () => {
  let service: BpSwapRecordService;
  let httpMock: HttpTestingController;
  let elemDefault: IBpSwapRecord;
  let expectedResult: IBpSwapRecord | IBpSwapRecord[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BpSwapRecordService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      oldBat: 'AAAAAAA',
      newBat: 'AAAAAAA',
      oldCab: 'AAAAAAA',
      newCab: 'AAAAAAA',
      bss: 'AAAAAAA',
      user: 0,
      state: 0,
      error: 0,
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

    it('should create a BpSwapRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BpSwapRecord()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BpSwapRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          oldBat: 'BBBBBB',
          newBat: 'BBBBBB',
          oldCab: 'BBBBBB',
          newCab: 'BBBBBB',
          bss: 'BBBBBB',
          user: 1,
          state: 1,
          error: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BpSwapRecord', () => {
      const patchObject = Object.assign(
        {
          oldBat: 'BBBBBB',
          oldCab: 'BBBBBB',
          bss: 'BBBBBB',
          user: 1,
          state: 1,
        },
        new BpSwapRecord()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BpSwapRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          oldBat: 'BBBBBB',
          newBat: 'BBBBBB',
          oldCab: 'BBBBBB',
          newCab: 'BBBBBB',
          bss: 'BBBBBB',
          user: 1,
          state: 1,
          error: 1,
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

    it('should delete a BpSwapRecord', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBpSwapRecordToCollectionIfMissing', () => {
      it('should add a BpSwapRecord to an empty array', () => {
        const bpSwapRecord: IBpSwapRecord = { id: 123 };
        expectedResult = service.addBpSwapRecordToCollectionIfMissing([], bpSwapRecord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bpSwapRecord);
      });

      it('should not add a BpSwapRecord to an array that contains it', () => {
        const bpSwapRecord: IBpSwapRecord = { id: 123 };
        const bpSwapRecordCollection: IBpSwapRecord[] = [
          {
            ...bpSwapRecord,
          },
          { id: 456 },
        ];
        expectedResult = service.addBpSwapRecordToCollectionIfMissing(bpSwapRecordCollection, bpSwapRecord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BpSwapRecord to an array that doesn't contain it", () => {
        const bpSwapRecord: IBpSwapRecord = { id: 123 };
        const bpSwapRecordCollection: IBpSwapRecord[] = [{ id: 456 }];
        expectedResult = service.addBpSwapRecordToCollectionIfMissing(bpSwapRecordCollection, bpSwapRecord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bpSwapRecord);
      });

      it('should add only unique BpSwapRecord to an array', () => {
        const bpSwapRecordArray: IBpSwapRecord[] = [{ id: 123 }, { id: 456 }, { id: 3450 }];
        const bpSwapRecordCollection: IBpSwapRecord[] = [{ id: 123 }];
        expectedResult = service.addBpSwapRecordToCollectionIfMissing(bpSwapRecordCollection, ...bpSwapRecordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bpSwapRecord: IBpSwapRecord = { id: 123 };
        const bpSwapRecord2: IBpSwapRecord = { id: 456 };
        expectedResult = service.addBpSwapRecordToCollectionIfMissing([], bpSwapRecord, bpSwapRecord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bpSwapRecord);
        expect(expectedResult).toContain(bpSwapRecord2);
      });

      it('should accept null and undefined values', () => {
        const bpSwapRecord: IBpSwapRecord = { id: 123 };
        expectedResult = service.addBpSwapRecordToCollectionIfMissing([], null, bpSwapRecord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bpSwapRecord);
      });

      it('should return initial array if no BpSwapRecord is added', () => {
        const bpSwapRecordCollection: IBpSwapRecord[] = [{ id: 123 }];
        expectedResult = service.addBpSwapRecordToCollectionIfMissing(bpSwapRecordCollection, undefined, null);
        expect(expectedResult).toEqual(bpSwapRecordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
