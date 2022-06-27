export interface IBatteryState {
  id?: number;
  serialNumber?: string;
  vol?: number;
  cur?: number;
  soc?: number;
  soh?: number;
  state?: number;
  status?: number;
}

export class BatteryState implements IBatteryState {
  constructor(
    public id?: number,
    public serialNumber?: string,
    public vol?: number,
    public cur?: number,
    public soc?: number,
    public soh?: number,
    public state?: number,
    public status?: number
  ) {}
}

export function getBatteryStateIdentifier(batteryState: IBatteryState): number | undefined {
  return batteryState.id;
}
