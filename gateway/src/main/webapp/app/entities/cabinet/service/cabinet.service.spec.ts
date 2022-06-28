import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICabinet, Cabinet } from '../cabinet.model';

import { CabinetService } from './cabinet.service';

describe('Cabinet Service', () => {
  let service: CabinetService;
  let httpMock: HttpTestingController;
  let elemDefault: ICabinet;
  let expectedResult: ICabinet | ICabinet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CabinetService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      bssId: 0,
      bpId: 0,
      bpReady: false,
      swapNo: 0,
      stateCode: 0,
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

    it('should create a Cabinet', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Cabinet()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Cabinet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bssId: 1,
          bpId: 1,
          bpReady: true,
          swapNo: 1,
          stateCode: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Cabinet', () => {
      const patchObject = Object.assign(
        {
          bpId: 1,
          bpReady: true,
          swapNo: 1,
          stateCode: 1,
        },
        new Cabinet()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Cabinet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          bssId: 1,
          bpId: 1,
          bpReady: true,
          swapNo: 1,
          stateCode: 1,
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

    it('should delete a Cabinet', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCabinetToCollectionIfMissing', () => {
      it('should add a Cabinet to an empty array', () => {
        const cabinet: ICabinet = { id: 123 };
        expectedResult = service.addCabinetToCollectionIfMissing([], cabinet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cabinet);
      });

      it('should not add a Cabinet to an array that contains it', () => {
        const cabinet: ICabinet = { id: 123 };
        const cabinetCollection: ICabinet[] = [
          {
            ...cabinet,
          },
          { id: 456 },
        ];
        expectedResult = service.addCabinetToCollectionIfMissing(cabinetCollection, cabinet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Cabinet to an array that doesn't contain it", () => {
        const cabinet: ICabinet = { id: 123 };
        const cabinetCollection: ICabinet[] = [{ id: 456 }];
        expectedResult = service.addCabinetToCollectionIfMissing(cabinetCollection, cabinet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cabinet);
      });

      it('should add only unique Cabinet to an array', () => {
        const cabinetArray: ICabinet[] = [{ id: 123 }, { id: 456 }, { id: 809 }];
        const cabinetCollection: ICabinet[] = [{ id: 123 }];
        expectedResult = service.addCabinetToCollectionIfMissing(cabinetCollection, ...cabinetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const cabinet: ICabinet = { id: 123 };
        const cabinet2: ICabinet = { id: 456 };
        expectedResult = service.addCabinetToCollectionIfMissing([], cabinet, cabinet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(cabinet);
        expect(expectedResult).toContain(cabinet2);
      });

      it('should accept null and undefined values', () => {
        const cabinet: ICabinet = { id: 123 };
        expectedResult = service.addCabinetToCollectionIfMissing([], null, cabinet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(cabinet);
      });

      it('should return initial array if no Cabinet is added', () => {
        const cabinetCollection: ICabinet[] = [{ id: 123 }];
        expectedResult = service.addCabinetToCollectionIfMissing(cabinetCollection, undefined, null);
        expect(expectedResult).toEqual(cabinetCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
