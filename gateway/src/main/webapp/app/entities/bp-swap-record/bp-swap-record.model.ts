export interface IBpSwapRecord {
  id?: number;
  oldBat?: string | null;
  newBat?: string | null;
  oldCab?: string | null;
  newCab?: string | null;
  bss?: string | null;
  user?: number | null;
  state?: number | null;
  error?: number | null;
}

export class BpSwapRecord implements IBpSwapRecord {
  constructor(
    public id?: number,
    public oldBat?: string | null,
    public newBat?: string | null,
    public oldCab?: string | null,
    public newCab?: string | null,
    public bss?: string | null,
    public user?: number | null,
    public state?: number | null,
    public error?: number | null
  ) {}
}

export function getBpSwapRecordIdentifier(bpSwapRecord: IBpSwapRecord): number | undefined {
  return bpSwapRecord.id;
}
