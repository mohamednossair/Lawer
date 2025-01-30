import dayjs from 'dayjs';

export interface IClient {
  id?: number;
  clientName?: string;
  clientDescription?: string | null;
  contactNumber?: string;
  address?: string | null;
  nationalId?: string | null;
  email?: string | null;
  createdAt?: dayjs.Dayjs | null;
  updatedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IClient> = {};
