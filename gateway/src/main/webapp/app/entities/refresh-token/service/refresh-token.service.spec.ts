import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IRefreshToken, RefreshToken } from '../refresh-token.model';

import { RefreshTokenService } from './refresh-token.service';

describe('RefreshToken Service', () => {
  let service: RefreshTokenService;
  let httpMock: HttpTestingController;
  let elemDefault: IRefreshToken;
  let expectedResult: IRefreshToken | IRefreshToken[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RefreshTokenService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      username: 'AAAAAAA',
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

    it('should create a RefreshToken', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new RefreshToken()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a RefreshToken', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          username: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a RefreshToken', () => {
      const patchObject = Object.assign({}, new RefreshToken());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of RefreshToken', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          username: 'BBBBBB',
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

    it('should delete a RefreshToken', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRefreshTokenToCollectionIfMissing', () => {
      it('should add a RefreshToken to an empty array', () => {
        const refreshToken: IRefreshToken = { id: 123 };
        expectedResult = service.addRefreshTokenToCollectionIfMissing([], refreshToken);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(refreshToken);
      });

      it('should not add a RefreshToken to an array that contains it', () => {
        const refreshToken: IRefreshToken = { id: 123 };
        const refreshTokenCollection: IRefreshToken[] = [
          {
            ...refreshToken,
          },
          { id: 456 },
        ];
        expectedResult = service.addRefreshTokenToCollectionIfMissing(refreshTokenCollection, refreshToken);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a RefreshToken to an array that doesn't contain it", () => {
        const refreshToken: IRefreshToken = { id: 123 };
        const refreshTokenCollection: IRefreshToken[] = [{ id: 456 }];
        expectedResult = service.addRefreshTokenToCollectionIfMissing(refreshTokenCollection, refreshToken);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(refreshToken);
      });

      it('should add only unique RefreshToken to an array', () => {
        const refreshTokenArray: IRefreshToken[] = [{ id: 123 }, { id: 456 }, { id: 5708 }];
        const refreshTokenCollection: IRefreshToken[] = [{ id: 123 }];
        expectedResult = service.addRefreshTokenToCollectionIfMissing(refreshTokenCollection, ...refreshTokenArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const refreshToken: IRefreshToken = { id: 123 };
        const refreshToken2: IRefreshToken = { id: 456 };
        expectedResult = service.addRefreshTokenToCollectionIfMissing([], refreshToken, refreshToken2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(refreshToken);
        expect(expectedResult).toContain(refreshToken2);
      });

      it('should accept null and undefined values', () => {
        const refreshToken: IRefreshToken = { id: 123 };
        expectedResult = service.addRefreshTokenToCollectionIfMissing([], null, refreshToken, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(refreshToken);
      });

      it('should return initial array if no RefreshToken is added', () => {
        const refreshTokenCollection: IRefreshToken[] = [{ id: 123 }];
        expectedResult = service.addRefreshTokenToCollectionIfMissing(refreshTokenCollection, undefined, null);
        expect(expectedResult).toEqual(refreshTokenCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
