export interface IDeviceType {
  id?: number;
  name?: string;
}

export class DeviceType implements IDeviceType {
  constructor(public id?: number, public name?: string) {}
}

export function getDeviceTypeIdentifier(deviceType: IDeviceType): number | undefined {
  return deviceType.id;
}
