import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { AuthService } from '../features/auth/services/auth.service';
import { UserDto } from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private isLoggedSubject = new BehaviorSubject<boolean>(false);
  public isLogged = false;
  public user: UserDto | undefined;

  public $isLogged(): Observable<boolean> {
    return this.isLoggedSubject.asObservable();
  }

  logIn(user: UserDto): void {
    this.user = user;
    this.isLogged = true;
    this.next();
  }

  logOut(): void {
    localStorage.removeItem('token');
    this.user = undefined;
    this.isLogged = false;
    this.next();
  }

  private next(): void {
    this.isLoggedSubject.next(this.isLogged);
  }

  getCurrentUser(): any {
    const user = localStorage.getItem('currentUser');
    return user ? JSON.parse(user) : null;
  }
}
