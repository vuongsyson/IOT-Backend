export interface IEmployee {
  id?: number;
  name?: string | null;
  phone?: string | null;
  code?: string | null;
  isAssignment?: boolean | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public name?: string | null,
    public phone?: string | null,
    public code?: string | null,
    public isAssignment?: boolean | null
  ) {
    this.isAssignment = this.isAssignment ?? false;
  }
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
