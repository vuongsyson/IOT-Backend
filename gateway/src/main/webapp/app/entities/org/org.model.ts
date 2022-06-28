export interface IOrg {
  id?: number;
  orgId?: number | null;
}

export class Org implements IOrg {
  constructor(public id?: number, public orgId?: number | null) {}
}

export function getOrgIdentifier(org: IOrg): number | undefined {
  return org.id;
}
