import dayjs from 'dayjs/esm';
import { SosState } from 'app/entities/enumerations/sos-state.model';

export interface ISosRequest {
  id?: number;
  userId?: number;
  phone?: string;
  deviceSerialNumber?: string;
  description?: string | null;
  image?: string | null;
  state?: SosState | null;
  rating?: number | null;
  done?: boolean | null;
  doneTime?: dayjs.Dayjs | null;
}

export class SosRequest implements ISosRequest {
  constructor(
    public id?: number,
    public userId?: number,
    public phone?: string,
    public deviceSerialNumber?: string,
    public description?: string | null,
    public image?: string | null,
    public state?: SosState | null,
    public rating?: number | null,
    public done?: boolean | null,
    public doneTime?: dayjs.Dayjs | null
  ) {
    this.done = this.done ?? false;
  }
}

export function getSosRequestIdentifier(sosRequest: ISosRequest): number | undefined {
  return sosRequest.id;
}
