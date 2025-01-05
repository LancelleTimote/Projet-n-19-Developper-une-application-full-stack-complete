import { UserDto } from 'src/app/interfaces/user.interface';

export interface AuthSuccess {
  token: string;
  user: UserDto;
}
