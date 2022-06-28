import { IRentalHistory } from 'app/entities/rental-history/rental-history.model';

export interface IBattery {
  id?: number;
  serialNo?: string;
  hwVersion?: number;
  swVersion?: number;
  manufactureDate?: string;
  capacity?: number;
  maxCharge?: number;
  maxDiscarge?: number;
  maxVol?: number;
  minVol?: number;
  used?: boolean;
  soc?: number;
  soh?: number;
  temp?: number;
  ownerId?: number;
  renterId?: number;
  cycleCount?: number;
  rentalHistory?: IRentalHistory | null;
}

export class Battery implements IBattery {
  constructor(
    public id?: number,
    public serialNo?: string,
    public hwVersion?: number,
    public swVersion?: number,
    public manufactureDate?: string,
    public capacity?: number,
    public maxCharge?: number,
    public maxDiscarge?: number,
    public maxVol?: number,
    public minVol?: number,
    public used?: boolean,
    public soc?: number,
    public soh?: number,
    public temp?: number,
    public ownerId?: number,
    public renterId?: number,
    public cycleCount?: number,
    public rentalHistory?: IRentalHistory | null
  ) {
    this.used = this.used ?? false;
  }
}

export function getBatteryIdentifier(battery: IBattery): number | undefined {
  return battery.id;
}
