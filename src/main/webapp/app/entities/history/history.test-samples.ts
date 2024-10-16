import { IHistory, NewHistory } from './history.model';

export const sampleWithRequiredData: IHistory = {
  id: 2271,
  predictionInput: 'reef among',
  predictionResult: 'understatement',
  predictionDay: 19608,
  windowSize: 30566,
};

export const sampleWithPartialData: IHistory = {
  id: 11227,
  predictionInput: 'boiling whoever sausage',
  predictionResult: 'whitewash hmph',
  predictionDay: 29173,
  windowSize: 5387,
};

export const sampleWithFullData: IHistory = {
  id: 9614,
  predictionInput: 'lest',
  predictionResult: 'backfill railroad',
  predictionDay: 32200,
  windowSize: 13025,
};

export const sampleWithNewData: NewHistory = {
  predictionInput: 'unto hopelessly',
  predictionResult: 'bitterly kookily',
  predictionDay: 28836,
  windowSize: 5620,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
