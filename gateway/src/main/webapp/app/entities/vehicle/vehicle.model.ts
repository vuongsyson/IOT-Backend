export interface IVehicle {
  id?: number;
  clearance?: number | null;
  maxPower?: number | null;
  maxSpeed?: number | null;
  maxLoad?: number | null;
  weightTotal?: number | null;
  maxDistance?: number | null;
  wheelBase?: number | null;
  hwVersion?: number | null;
  swVersion?: number | null;
  serialNumber?: string | null;
  manufactureDate?: string | null;
  lotNumber?: number | null;
  color?: string | null;
  vehicleType?: string | null;
  used?: boolean | null;
  userId?: number | null;
}

export class Vehicle implements IVehicle {
  constructor(
    public id?: number,
    public clearance?: number | null,
    public maxPower?: number | null,
    public maxSpeed?: number | null,
    public maxLoad?: number | null,
    public weightTotal?: number | null,
    public maxDistance?: number | null,
    public wheelBase?: number | null,
    public hwVersion?: number | null,
    public swVersion?: number | null,
    public serialNumber?: string | null,
    public manufactureDate?: string | null,
    public lotNumber?: number | null,
    public color?: string | null,
    public vehicleType?: string | null,
    public used?: boolean | null,
    public userId?: number | null
  ) {
    this.used = this.used ?? false;
  }
}

export function getVehicleIdentifier(vehicle: IVehicle): number | undefined {
  return vehicle.id;
}
