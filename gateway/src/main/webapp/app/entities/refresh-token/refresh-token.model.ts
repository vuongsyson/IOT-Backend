export interface IRefreshToken {
  id?: number;
  username?: string | null;
}

export class RefreshToken implements IRefreshToken {
  constructor(public id?: number, public username?: string | null) {}
}

export function getRefreshTokenIdentifier(refreshToken: IRefreshToken): number | undefined {
  return refreshToken.id;
}
