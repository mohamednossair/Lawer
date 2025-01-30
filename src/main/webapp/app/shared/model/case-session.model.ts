import dayjs from 'dayjs';
import { ICourtCase } from 'app/shared/model/court-case.model';

export interface ICaseSession {
  id?: number;
  sessionDate?: dayjs.Dayjs;
  sessionTime?: dayjs.Dayjs | null;
  description?: string | null;
  notes?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  courtCase?: ICourtCase | null;
}

export const defaultValue: Readonly<ICaseSession> = {};
