import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { ICourtCase } from 'app/shared/model/court-case.model';

export interface ICaseSession {
  id?: number;
  sessionDate?: dayjs.Dayjs;
  sessionTime?: dayjs.Dayjs | null;
  description?: string | null;
  notes?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  client?: IClient;
  courtCase?: ICourtCase;
}

export const defaultValue: Readonly<ICaseSession> = {};
