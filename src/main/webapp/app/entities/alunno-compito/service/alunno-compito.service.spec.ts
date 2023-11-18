import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAlunnoCompito } from '../alunno-compito.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../alunno-compito.test-samples';

import { AlunnoCompitoService, RestAlunnoCompito } from './alunno-compito.service';

const requireRestSample: RestAlunnoCompito = {
  ...sampleWithRequiredData,
  dataRestituizione: sampleWithRequiredData.dataRestituizione?.toJSON(),
};

describe('AlunnoCompito Service', () => {
  let service: AlunnoCompitoService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlunnoCompito | IAlunnoCompito[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AlunnoCompitoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a AlunnoCompito', () => {
      const alunnoCompito = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alunnoCompito).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AlunnoCompito', () => {
      const alunnoCompito = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alunnoCompito).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AlunnoCompito', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AlunnoCompito', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AlunnoCompito', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAlunnoCompitoToCollectionIfMissing', () => {
      it('should add a AlunnoCompito to an empty array', () => {
        const alunnoCompito: IAlunnoCompito = sampleWithRequiredData;
        expectedResult = service.addAlunnoCompitoToCollectionIfMissing([], alunnoCompito);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alunnoCompito);
      });

      it('should not add a AlunnoCompito to an array that contains it', () => {
        const alunnoCompito: IAlunnoCompito = sampleWithRequiredData;
        const alunnoCompitoCollection: IAlunnoCompito[] = [
          {
            ...alunnoCompito,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlunnoCompitoToCollectionIfMissing(alunnoCompitoCollection, alunnoCompito);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AlunnoCompito to an array that doesn't contain it", () => {
        const alunnoCompito: IAlunnoCompito = sampleWithRequiredData;
        const alunnoCompitoCollection: IAlunnoCompito[] = [sampleWithPartialData];
        expectedResult = service.addAlunnoCompitoToCollectionIfMissing(alunnoCompitoCollection, alunnoCompito);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alunnoCompito);
      });

      it('should add only unique AlunnoCompito to an array', () => {
        const alunnoCompitoArray: IAlunnoCompito[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alunnoCompitoCollection: IAlunnoCompito[] = [sampleWithRequiredData];
        expectedResult = service.addAlunnoCompitoToCollectionIfMissing(alunnoCompitoCollection, ...alunnoCompitoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alunnoCompito: IAlunnoCompito = sampleWithRequiredData;
        const alunnoCompito2: IAlunnoCompito = sampleWithPartialData;
        expectedResult = service.addAlunnoCompitoToCollectionIfMissing([], alunnoCompito, alunnoCompito2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alunnoCompito);
        expect(expectedResult).toContain(alunnoCompito2);
      });

      it('should accept null and undefined values', () => {
        const alunnoCompito: IAlunnoCompito = sampleWithRequiredData;
        expectedResult = service.addAlunnoCompitoToCollectionIfMissing([], null, alunnoCompito, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alunnoCompito);
      });

      it('should return initial array if no AlunnoCompito is added', () => {
        const alunnoCompitoCollection: IAlunnoCompito[] = [sampleWithRequiredData];
        expectedResult = service.addAlunnoCompitoToCollectionIfMissing(alunnoCompitoCollection, undefined, null);
        expect(expectedResult).toEqual(alunnoCompitoCollection);
      });
    });

    describe('compareAlunnoCompito', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlunnoCompito(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAlunnoCompito(entity1, entity2);
        const compareResult2 = service.compareAlunnoCompito(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAlunnoCompito(entity1, entity2);
        const compareResult2 = service.compareAlunnoCompito(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAlunnoCompito(entity1, entity2);
        const compareResult2 = service.compareAlunnoCompito(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
