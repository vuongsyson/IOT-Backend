export interface IGroup {
  id?: number;
  name?: string;
}

export class Group implements IGroup {
  constructor(public id?: number, public name?: string) {}
}

export function getGroupIdentifier(group: IGroup): number | undefined {
  return group.id;
}
