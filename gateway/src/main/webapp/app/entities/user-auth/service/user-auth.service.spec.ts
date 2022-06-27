import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IUserAuth, UserAuth } from '../user-auth.model';

import { UserAuthService } from './user-auth.service';

describe('UserAuth Service', () => {
  let service: UserAuthService;
  let httpMock: HttpTestingController;
  let elemDefault: IUserAuth;
  let expectedResult: IUserAuth | IUserAuth[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserAuthService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      email: 'AAAAAAA',
      phone: 'AAAAAAA',
      username: 'AAAAAAA',
      authorities: 'AAAAAAA',
      password: 'AAAAAAA',
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

    it('should create a UserAuth', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new UserAuth()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserAuth', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          email: 'BBBBBB',
          phone: 'BBBBBB',
          username: 'BBBBBB',
          authorities: 'BBBBBB',
          password: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserAuth', () => {
      const patchObject = Object.assign(
        {
          email: 'BBBBBB',
          phone: 'BBBBBB',
          password: 'BBBBBB',
        },
        new UserAuth()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserAuth', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          email: 'BBBBBB',
          phone: 'BBBBBB',
          username: 'BBBBBB',
          authorities: 'BBBBBB',
          password: 'BBBBBB',
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

    it('should delete a UserAuth', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addUserAuthToCollectionIfMissing', () => {
      it('should add a UserAuth to an empty array', () => {
        const userAuth: IUserAuth = { id: 123 };
        expectedResult = service.addUserAuthToCollectionIfMissing([], userAuth);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAuth);
      });

      it('should not add a UserAuth to an array that contains it', () => {
        const userAuth: IUserAuth = { id: 123 };
        const userAuthCollection: IUserAuth[] = [
          {
            ...userAuth,
          },
          { id: 456 },
        ];
        expectedResult = service.addUserAuthToCollectionIfMissing(userAuthCollection, userAuth);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserAuth to an array that doesn't contain it", () => {
        const userAuth: IUserAuth = { id: 123 };
        const userAuthCollection: IUserAuth[] = [{ id: 456 }];
        expectedResult = service.addUserAuthToCollectionIfMissing(userAuthCollection, userAuth);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAuth);
      });

      it('should add only unique UserAuth to an array', () => {
        const userAuthArray: IUserAuth[] = [{ id: 123 }, { id: 456 }, { id: 23774 }];
        const userAuthCollection: IUserAuth[] = [{ id: 123 }];
        expectedResult = service.addUserAuthToCollectionIfMissing(userAuthCollection, ...userAuthArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userAuth: IUserAuth = { id: 123 };
        const userAuth2: IUserAuth = { id: 456 };
        expectedResult = service.addUserAuthToCollectionIfMissing([], userAuth, userAuth2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userAuth);
        expect(expectedResult).toContain(userAuth2);
      });

      it('should accept null and undefined values', () => {
        const userAuth: IUserAuth = { id: 123 };
        expectedResult = service.addUserAuthToCollectionIfMissing([], null, userAuth, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userAuth);
      });

      it('should return initial array if no UserAuth is added', () => {
        const userAuthCollection: IUserAuth[] = [{ id: 123 }];
        expectedResult = service.addUserAuthToCollectionIfMissing(userAuthCollection, undefined, null);
        expect(expectedResult).toEqual(userAuthCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
