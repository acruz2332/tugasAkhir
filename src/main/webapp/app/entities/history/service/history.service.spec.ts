import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IHistory } from '../history.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../history.test-samples';

import { HistoryService } from './history.service';

const requireRestSample: IHistory = {
  ...sampleWithRequiredData,
};

describe('History Service', () => {
  let service: HistoryService;
  let httpMock: HttpTestingController;
  let expectedResult: IHistory | IHistory[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HistoryService);
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

    it('should create a History', () => {
      const history = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(history).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a History', () => {
      const history = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(history).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a History', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of History', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a History', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addHistoryToCollectionIfMissing', () => {
      it('should add a History to an empty array', () => {
        const history: IHistory = sampleWithRequiredData;
        expectedResult = service.addHistoryToCollectionIfMissing([], history);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(history);
      });

      it('should not add a History to an array that contains it', () => {
        const history: IHistory = sampleWithRequiredData;
        const historyCollection: IHistory[] = [
          {
            ...history,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addHistoryToCollectionIfMissing(historyCollection, history);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a History to an array that doesn't contain it", () => {
        const history: IHistory = sampleWithRequiredData;
        const historyCollection: IHistory[] = [sampleWithPartialData];
        expectedResult = service.addHistoryToCollectionIfMissing(historyCollection, history);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(history);
      });

      it('should add only unique History to an array', () => {
        const historyArray: IHistory[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const historyCollection: IHistory[] = [sampleWithRequiredData];
        expectedResult = service.addHistoryToCollectionIfMissing(historyCollection, ...historyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const history: IHistory = sampleWithRequiredData;
        const history2: IHistory = sampleWithPartialData;
        expectedResult = service.addHistoryToCollectionIfMissing([], history, history2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(history);
        expect(expectedResult).toContain(history2);
      });

      it('should accept null and undefined values', () => {
        const history: IHistory = sampleWithRequiredData;
        expectedResult = service.addHistoryToCollectionIfMissing([], null, history, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(history);
      });

      it('should return initial array if no History is added', () => {
        const historyCollection: IHistory[] = [sampleWithRequiredData];
        expectedResult = service.addHistoryToCollectionIfMissing(historyCollection, undefined, null);
        expect(expectedResult).toEqual(historyCollection);
      });
    });

    describe('compareHistory', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareHistory(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareHistory(entity1, entity2);
        const compareResult2 = service.compareHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareHistory(entity1, entity2);
        const compareResult2 = service.compareHistory(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareHistory(entity1, entity2);
        const compareResult2 = service.compareHistory(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
