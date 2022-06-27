export interface ISosType {
  id?: number;
  name?: string;
}

export class SosType implements ISosType {
  constructor(public id?: number, public name?: string) {}
}

export function getSosTypeIdentifier(sosType: ISosType): number | undefined {
  return sosType.id;
}
