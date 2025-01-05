import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { AuthSuccess } from '../interfaces/authSuccess.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { UserDto } from 'src/app/interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private pathService = '/api/auth';

  constructor(private httpClient: HttpClient) {}

  register(registerRequest: RegisterRequest): Observable<AuthSuccess> {
    return this.httpClient
      .post<AuthSuccess>(`${this.pathService}/register`, registerRequest)
      .pipe(
        tap((response) => {
          console.log('Register response:', response);

          localStorage.setItem('token', response.token);

          if (response.user) {
            localStorage.setItem('currentUser', JSON.stringify(response.user));
          } else {
            console.error('User data not returned.');
          }
        })
      );
  }

  login(loginRequest: LoginRequest): Observable<AuthSuccess> {
    return this.httpClient
      .post<AuthSuccess>(`${this.pathService}/login`, loginRequest)
      .pipe(
        tap((response) => {
          console.log('Login response:', response);
          localStorage.setItem('token', response.token);
          if (response.user) {
            localStorage.setItem('currentUser', JSON.stringify(response.user));
          } else {
            console.error('User data not returned.');
          }
        })
      );
  }

  me(): Observable<UserDto> {
    return this.httpClient.get<UserDto>('/api/user/me');
  }

  get currentUser(): UserDto | null {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
  }
}
