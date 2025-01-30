import dayjs from 'dayjs';

export interface ILawyer {
  id?: number;
  lawyerName?: string;
  address?: string | null;
  contactNumber?: string | null;
  specialization?: string | null;
  email?: string | null;
  registrationNumber?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ILawyer> = {};
