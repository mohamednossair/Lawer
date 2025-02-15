import dayjs from 'dayjs';
import { IClient } from 'app/shared/model/client.model';
import { ICourtCase } from 'app/shared/model/court-case.model';
import { IUser } from 'app/shared/model/user.model';

export interface ICaseDocument {
  id?: number;
  documentName?: string;
  documentType?: string;
  documentFileContentType?: string;
  documentFile?: string;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  client?: IClient;
  courtCase?: ICourtCase;
  user?: IUser | null;
}

export const defaultValue: Readonly<ICaseDocument> = {};
