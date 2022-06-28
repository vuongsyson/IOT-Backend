import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBss, Bss } from '../bss.model';

import { BssService } from './bss.service';

describe('Bss Service', () => {
  let service: BssService;
  let httpMock: HttpTestingController;
  let elemDefault: IBss;
  let expectedResult: IBss | IBss[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BssService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      address: 'AAAAAAA',
      serialNumber: 'AAAAAAA',
      hwVersion: 0,
      swVersion: 0,
      manufactureDate: 'AAAAAAA',
      lon: 0,
      lat: 0,
      typeCode: 0,
      cabNum: 0,
      cabEmptyNum: 0,
      bpReadyNum: 0,
      swapBpNo: 0,
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

    it('should create a Bss', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Bss()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Bss', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          address: 'BBBBBB',
          serialNumber: 'BBBBBB',
          hwVersion: 1,
          swVersion: 1,
          manufactureDate: 'BBBBBB',
          lon: 1,
          lat: 1,
          typeCode: 1,
          cabNum: 1,
          cabEmptyNum: 1,
          bpReadyNum: 1,
          swapBpNo: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Bss', () => {
      const patchObject = Object.assign(
        {
          serialNumber: 'BBBBBB',
          hwVersion: 1,
          lon: 1,
          typeCode: 1,
          cabNum: 1,
          bpReadyNum: 1,
          swapBpNo: 1,
        },
        new Bss()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Bss', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          address: 'BBBBBB',
          serialNumber: 'BBBBBB',
          hwVersion: 1,
          swVersion: 1,
          manufactureDate: 'BBBBBB',
          lon: 1,
          lat: 1,
          typeCode: 1,
          cabNum: 1,
          cabEmptyNum: 1,
          bpReadyNum: 1,
          swapBpNo: 1,
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

    it('should delete a Bss', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBssToCollectionIfMissing', () => {
      it('should add a Bss to an empty array', () => {
        const bss: IBss = { id: 123 };
        expectedResult = service.addBssToCollectionIfMissing([], bss);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bss);
      });

      it('should not add a Bss to an array that contains it', () => {
        const bss: IBss = { id: 123 };
        const bssCollection: IBss[] = [
          {
            ...bss,
          },
          { id: 456 },
        ];
        expectedResult = service.addBssToCollectionIfMissing(bssCollection, bss);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Bss to an array that doesn't contain it", () => {
        const bss: IBss = { id: 123 };
        const bssCollection: IBss[] = [{ id: 456 }];
        expectedResult = service.addBssToCollectionIfMissing(bssCollection, bss);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bss);
      });

      it('should add only unique Bss to an array', () => {
        const bssArray: IBss[] = [{ id: 123 }, { id: 456 }, { id: 84961 }];
        const bssCollection: IBss[] = [{ id: 123 }];
        expectedResult = service.addBssToCollectionIfMissing(bssCollection, ...bssArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bss: IBss = { id: 123 };
        const bss2: IBss = { id: 456 };
        expectedResult = service.addBssToCollectionIfMissing([], bss, bss2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bss);
        expect(expectedResult).toContain(bss2);
      });

      it('should accept null and undefined values', () => {
        const bss: IBss = { id: 123 };
        expectedResult = service.addBssToCollectionIfMissing([], null, bss, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bss);
      });

      it('should return initial array if no Bss is added', () => {
        const bssCollection: IBss[] = [{ id: 123 }];
        expectedResult = service.addBssToCollectionIfMissing(bssCollection, undefined, null);
        expect(expectedResult).toEqual(bssCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
