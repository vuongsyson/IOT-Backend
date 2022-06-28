export interface ICabinet {
  id?: number;
  bssId?: number;
  bpId?: number | null;
  bpReady?: boolean | null;
  swapNo?: number | null;
  stateCode?: number | null;
}

export class Cabinet implements ICabinet {
  constructor(
    public id?: number,
    public bssId?: number,
    public bpId?: number | null,
    public bpReady?: boolean | null,
    public swapNo?: number | null,
    public stateCode?: number | null
  ) {
    this.bpReady = this.bpReady ?? false;
  }
}

export function getCabinetIdentifier(cabinet: ICabinet): number | undefined {
  return cabinet.id;
}
