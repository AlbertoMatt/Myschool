import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICompitoInClasse } from '../compito-in-classe.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../compito-in-classe.test-samples';

import { CompitoInClasseService, RestCompitoInClasse } from './compito-in-classe.service';

const requireRestSample: RestCompitoInClasse = {
  ...sampleWithRequiredData,
  data: sampleWithRequiredData.data?.format(DATE_FORMAT),
};

describe('CompitoInClasse Service', () => {
  let service: CompitoInClasseService;
  let httpMock: HttpTestingController;
  let expectedResult: ICompitoInClasse | ICompitoInClasse[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CompitoInClasseService);
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

    it('should create a CompitoInClasse', () => {
      const compitoInClasse = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(compitoInClasse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CompitoInClasse', () => {
      const compitoInClasse = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(compitoInClasse).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CompitoInClasse', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CompitoInClasse', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a CompitoInClasse', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCompitoInClasseToCollectionIfMissing', () => {
      it('should add a CompitoInClasse to an empty array', () => {
        const compitoInClasse: ICompitoInClasse = sampleWithRequiredData;
        expectedResult = service.addCompitoInClasseToCollectionIfMissing([], compitoInClasse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compitoInClasse);
      });

      it('should not add a CompitoInClasse to an array that contains it', () => {
        const compitoInClasse: ICompitoInClasse = sampleWithRequiredData;
        const compitoInClasseCollection: ICompitoInClasse[] = [
          {
            ...compitoInClasse,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCompitoInClasseToCollectionIfMissing(compitoInClasseCollection, compitoInClasse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CompitoInClasse to an array that doesn't contain it", () => {
        const compitoInClasse: ICompitoInClasse = sampleWithRequiredData;
        const compitoInClasseCollection: ICompitoInClasse[] = [sampleWithPartialData];
        expectedResult = service.addCompitoInClasseToCollectionIfMissing(compitoInClasseCollection, compitoInClasse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compitoInClasse);
      });

      it('should add only unique CompitoInClasse to an array', () => {
        const compitoInClasseArray: ICompitoInClasse[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const compitoInClasseCollection: ICompitoInClasse[] = [sampleWithRequiredData];
        expectedResult = service.addCompitoInClasseToCollectionIfMissing(compitoInClasseCollection, ...compitoInClasseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const compitoInClasse: ICompitoInClasse = sampleWithRequiredData;
        const compitoInClasse2: ICompitoInClasse = sampleWithPartialData;
        expectedResult = service.addCompitoInClasseToCollectionIfMissing([], compitoInClasse, compitoInClasse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(compitoInClasse);
        expect(expectedResult).toContain(compitoInClasse2);
      });

      it('should accept null and undefined values', () => {
        const compitoInClasse: ICompitoInClasse = sampleWithRequiredData;
        expectedResult = service.addCompitoInClasseToCollectionIfMissing([], null, compitoInClasse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(compitoInClasse);
      });

      it('should return initial array if no CompitoInClasse is added', () => {
        const compitoInClasseCollection: ICompitoInClasse[] = [sampleWithRequiredData];
        expectedResult = service.addCompitoInClasseToCollectionIfMissing(compitoInClasseCollection, undefined, null);
        expect(expectedResult).toEqual(compitoInClasseCollection);
      });
    });

    describe('compareCompitoInClasse', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCompitoInClasse(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCompitoInClasse(entity1, entity2);
        const compareResult2 = service.compareCompitoInClasse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCompitoInClasse(entity1, entity2);
        const compareResult2 = service.compareCompitoInClasse(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCompitoInClasse(entity1, entity2);
        const compareResult2 = service.compareCompitoInClasse(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
