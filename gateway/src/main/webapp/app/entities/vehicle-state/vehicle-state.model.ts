import dayjs from 'dayjs/esm';

export interface IVehicleState {
  id?: number;
  speed?: number | null;
  lat?: number | null;
  lon?: number | null;
  error?: number | null;
  serialNumber?: string | null;
  status?: string | null;
  odo?: number | null;
  power?: number | null;
  throttle?: number | null;
  time?: dayjs.Dayjs | null;
}

export class VehicleState implements IVehicleState {
  constructor(
    public id?: number,
    public speed?: number | null,
    public lat?: number | null,
    public lon?: number | null,
    public error?: number | null,
    public serialNumber?: string | null,
    public status?: string | null,
    public odo?: number | null,
    public power?: number | null,
    public throttle?: number | null,
    public time?: dayjs.Dayjs | null
  ) {}
}

export function getVehicleStateIdentifier(vehicleState: IVehicleState): number | undefined {
  return vehicleState.id;
}
