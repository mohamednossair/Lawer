import dayjs from 'dayjs';
import { ICourt } from 'app/shared/model/court.model';
import { IClient } from 'app/shared/model/client.model';
import { ICourtCaseType } from 'app/shared/model/court-case-type.model';
import { ICaseStatus } from 'app/shared/model/case-status.model';
import { ILawyer } from 'app/shared/model/lawyer.model';

export interface ICourtCase {
  id?: number;
  number?: string;
  caseYear?: string;
  courtCircuit?: string | null;
  registrationDate?: dayjs.Dayjs;
  attorneyNumber?: string | null;
  attorneyYear?: number | null;
  attorneyAuthentication?: string | null;
  opponentName?: string | null;
  opponentDescription?: string | null;
  opponentAddress?: string | null;
  subject?: string;
  notes?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
  court?: ICourt;
  client?: IClient;
  courtCaseType?: ICourtCaseType;
  caseStatus?: ICaseStatus;
  opponentLawyerId?: ILawyer | null;
}

export const defaultValue: Readonly<ICourtCase> = {};
