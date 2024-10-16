import { IUser } from 'app/entities/user/user.model';

export interface IHistory {
  id: number;
  predictionInput?: string | null;
  predictionResult?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  createdDate?: string | null;
}

export type NewHistory = Omit<IHistory, 'id'> & { id: null };
