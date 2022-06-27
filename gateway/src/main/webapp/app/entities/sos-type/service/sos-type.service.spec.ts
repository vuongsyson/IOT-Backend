import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISosType, SosType } from '../sos-type.model';

import { SosTypeService } from './sos-type.service';

describe('SosType Service', () => {
  let service: SosTypeService;
  let httpMock: HttpTestingController;
  let elemDefault: ISosType;
  let expectedResult: ISosType | ISosType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SosTypeService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
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

    it('should create a SosType', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new SosType()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SosType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SosType', () => {
      const patchObject = Object.assign({}, new SosType());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SosType', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
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

    it('should delete a SosType', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addSosTypeToCollectionIfMissing', () => {
      it('should add a SosType to an empty array', () => {
        const sosType: ISosType = { id: 123 };
        expectedResult = service.addSosTypeToCollectionIfMissing([], sosType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sosType);
      });

      it('should not add a SosType to an array that contains it', () => {
        const sosType: ISosType = { id: 123 };
        const sosTypeCollection: ISosType[] = [
          {
            ...sosType,
          },
          { id: 456 },
        ];
        expectedResult = service.addSosTypeToCollectionIfMissing(sosTypeCollection, sosType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SosType to an array that doesn't contain it", () => {
        const sosType: ISosType = { id: 123 };
        const sosTypeCollection: ISosType[] = [{ id: 456 }];
        expectedResult = service.addSosTypeToCollectionIfMissing(sosTypeCollection, sosType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sosType);
      });

      it('should add only unique SosType to an array', () => {
        const sosTypeArray: ISosType[] = [{ id: 123 }, { id: 456 }, { id: 69747 }];
        const sosTypeCollection: ISosType[] = [{ id: 123 }];
        expectedResult = service.addSosTypeToCollectionIfMissing(sosTypeCollection, ...sosTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sosType: ISosType = { id: 123 };
        const sosType2: ISosType = { id: 456 };
        expectedResult = service.addSosTypeToCollectionIfMissing([], sosType, sosType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sosType);
        expect(expectedResult).toContain(sosType2);
      });

      it('should accept null and undefined values', () => {
        const sosType: ISosType = { id: 123 };
        expectedResult = service.addSosTypeToCollectionIfMissing([], null, sosType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sosType);
      });

      it('should return initial array if no SosType is added', () => {
        const sosTypeCollection: ISosType[] = [{ id: 123 }];
        expectedResult = service.addSosTypeToCollectionIfMissing(sosTypeCollection, undefined, null);
        expect(expectedResult).toEqual(sosTypeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
