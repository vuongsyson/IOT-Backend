export interface IUserAuth {
  id?: number;
  email?: string | null;
  phone?: string | null;
  username?: string | null;
  authorities?: string | null;
  password?: string | null;
}

export class UserAuth implements IUserAuth {
  constructor(
    public id?: number,
    public email?: string | null,
    public phone?: string | null,
    public username?: string | null,
    public authorities?: string | null,
    public password?: string | null
  ) {}
}

export function getUserAuthIdentifier(userAuth: IUserAuth): number | undefined {
  return userAuth.id;
}
