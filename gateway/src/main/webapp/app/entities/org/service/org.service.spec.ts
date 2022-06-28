import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrg, Org } from '../org.model';

import { OrgService } from './org.service';

describe('Org Service', () => {
  let service: OrgService;
  let httpMock: HttpTestingController;
  let elemDefault: IOrg;
  let expectedResult: IOrg | IOrg[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrgService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      orgId: 0,
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

    it('should create a Org', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Org()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Org', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          orgId: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Org', () => {
      const patchObject = Object.assign(
        {
          orgId: 1,
        },
        new Org()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Org', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          orgId: 1,
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

    it('should delete a Org', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addOrgToCollectionIfMissing', () => {
      it('should add a Org to an empty array', () => {
        const org: IOrg = { id: 123 };
        expectedResult = service.addOrgToCollectionIfMissing([], org);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(org);
      });

      it('should not add a Org to an array that contains it', () => {
        const org: IOrg = { id: 123 };
        const orgCollection: IOrg[] = [
          {
            ...org,
          },
          { id: 456 },
        ];
        expectedResult = service.addOrgToCollectionIfMissing(orgCollection, org);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Org to an array that doesn't contain it", () => {
        const org: IOrg = { id: 123 };
        const orgCollection: IOrg[] = [{ id: 456 }];
        expectedResult = service.addOrgToCollectionIfMissing(orgCollection, org);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(org);
      });

      it('should add only unique Org to an array', () => {
        const orgArray: IOrg[] = [{ id: 123 }, { id: 456 }, { id: 1219 }];
        const orgCollection: IOrg[] = [{ id: 123 }];
        expectedResult = service.addOrgToCollectionIfMissing(orgCollection, ...orgArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const org: IOrg = { id: 123 };
        const org2: IOrg = { id: 456 };
        expectedResult = service.addOrgToCollectionIfMissing([], org, org2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(org);
        expect(expectedResult).toContain(org2);
      });

      it('should accept null and undefined values', () => {
        const org: IOrg = { id: 123 };
        expectedResult = service.addOrgToCollectionIfMissing([], null, org, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(org);
      });

      it('should return initial array if no Org is added', () => {
        const orgCollection: IOrg[] = [{ id: 123 }];
        expectedResult = service.addOrgToCollectionIfMissing(orgCollection, undefined, null);
        expect(expectedResult).toEqual(orgCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
