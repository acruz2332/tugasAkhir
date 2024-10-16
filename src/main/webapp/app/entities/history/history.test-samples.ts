import { IHistory, NewHistory } from './history.model';

export const sampleWithRequiredData: IHistory = {
  id: 13667,
  predictionInput: 'clavicle after slice',
  predictionResult: 'yippee gracefully',
};

export const sampleWithPartialData: IHistory = {
  id: 22525,
  predictionInput: 'tackle',
  predictionResult: 'happily camp lest',
};

export const sampleWithFullData: IHistory = {
  id: 12486,
  predictionInput: 'athwart yet',
  predictionResult: 'during gracefully',
};

export const sampleWithNewData: NewHistory = {
  predictionInput: 'incommode frighten',
  predictionResult: 'fumbling bolt',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
