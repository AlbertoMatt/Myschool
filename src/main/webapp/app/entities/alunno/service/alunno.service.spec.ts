import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAlunno } from '../alunno.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../alunno.test-samples';

import { AlunnoService, RestAlunno } from './alunno.service';

const requireRestSample: RestAlunno = {
  ...sampleWithRequiredData,
  dataNascita: sampleWithRequiredData.dataNascita?.format(DATE_FORMAT),
};

describe('Alunno Service', () => {
  let service: AlunnoService;
  let httpMock: HttpTestingController;
  let expectedResult: IAlunno | IAlunno[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AlunnoService);
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

    it('should create a Alunno', () => {
      const alunno = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(alunno).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Alunno', () => {
      const alunno = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(alunno).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Alunno', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Alunno', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Alunno', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAlunnoToCollectionIfMissing', () => {
      it('should add a Alunno to an empty array', () => {
        const alunno: IAlunno = sampleWithRequiredData;
        expectedResult = service.addAlunnoToCollectionIfMissing([], alunno);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alunno);
      });

      it('should not add a Alunno to an array that contains it', () => {
        const alunno: IAlunno = sampleWithRequiredData;
        const alunnoCollection: IAlunno[] = [
          {
            ...alunno,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAlunnoToCollectionIfMissing(alunnoCollection, alunno);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Alunno to an array that doesn't contain it", () => {
        const alunno: IAlunno = sampleWithRequiredData;
        const alunnoCollection: IAlunno[] = [sampleWithPartialData];
        expectedResult = service.addAlunnoToCollectionIfMissing(alunnoCollection, alunno);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alunno);
      });

      it('should add only unique Alunno to an array', () => {
        const alunnoArray: IAlunno[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const alunnoCollection: IAlunno[] = [sampleWithRequiredData];
        expectedResult = service.addAlunnoToCollectionIfMissing(alunnoCollection, ...alunnoArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const alunno: IAlunno = sampleWithRequiredData;
        const alunno2: IAlunno = sampleWithPartialData;
        expectedResult = service.addAlunnoToCollectionIfMissing([], alunno, alunno2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(alunno);
        expect(expectedResult).toContain(alunno2);
      });

      it('should accept null and undefined values', () => {
        const alunno: IAlunno = sampleWithRequiredData;
        expectedResult = service.addAlunnoToCollectionIfMissing([], null, alunno, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(alunno);
      });

      it('should return initial array if no Alunno is added', () => {
        const alunnoCollection: IAlunno[] = [sampleWithRequiredData];
        expectedResult = service.addAlunnoToCollectionIfMissing(alunnoCollection, undefined, null);
        expect(expectedResult).toEqual(alunnoCollection);
      });
    });

    describe('compareAlunno', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAlunno(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAlunno(entity1, entity2);
        const compareResult2 = service.compareAlunno(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAlunno(entity1, entity2);
        const compareResult2 = service.compareAlunno(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAlunno(entity1, entity2);
        const compareResult2 = service.compareAlunno(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
