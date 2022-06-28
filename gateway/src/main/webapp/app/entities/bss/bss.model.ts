export interface IBss {
  id?: number;
  name?: string | null;
  address?: string | null;
  serialNumber?: string | null;
  hwVersion?: number | null;
  swVersion?: number | null;
  manufactureDate?: string | null;
  lon?: number | null;
  lat?: number | null;
  typeCode?: number | null;
  cabNum?: number | null;
  cabEmptyNum?: number | null;
  bpReadyNum?: number | null;
  swapBpNo?: number | null;
}

export class Bss implements IBss {
  constructor(
    public id?: number,
    public name?: string | null,
    public address?: string | null,
    public serialNumber?: string | null,
    public hwVersion?: number | null,
    public swVersion?: number | null,
    public manufactureDate?: string | null,
    public lon?: number | null,
    public lat?: number | null,
    public typeCode?: number | null,
    public cabNum?: number | null,
    public cabEmptyNum?: number | null,
    public bpReadyNum?: number | null,
    public swapBpNo?: number | null
  ) {}
}

export function getBssIdentifier(bss: IBss): number | undefined {
  return bss.id;
}
