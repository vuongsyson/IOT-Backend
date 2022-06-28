import dayjs from 'dayjs/esm';
import { IBattery } from 'app/entities/battery/battery.model';

export interface IRentalHistory {
  id?: number;
  userId?: number | null;
  batteryId?: number | null;
  timeStart?: dayjs.Dayjs | null;
  timeEnd?: dayjs.Dayjs | null;
  batteries?: IBattery[] | null;
}

export class RentalHistory implements IRentalHistory {
  constructor(
    public id?: number,
    public userId?: number | null,
    public batteryId?: number | null,
    public timeStart?: dayjs.Dayjs | null,
    public timeEnd?: dayjs.Dayjs | null,
    public batteries?: IBattery[] | null
  ) {}
}

export function getRentalHistoryIdentifier(rentalHistory: IRentalHistory): number | undefined {
  return rentalHistory.id;
}
