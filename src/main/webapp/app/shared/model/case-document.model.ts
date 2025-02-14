import dayjs from 'dayjs';
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
  courtCase?: ICourtCase | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<ICaseDocument> = {};
