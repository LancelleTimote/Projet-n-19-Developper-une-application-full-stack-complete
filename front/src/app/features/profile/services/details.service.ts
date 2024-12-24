import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from 'src/app/interfaces/user.interface';

@Injectable({
  providedIn: 'root',
})
export class DetailsService {
  private subscriptionApiUrl = '/api/subscriptions';
  private profileApiUrl = '/api/user';

  constructor(private http: HttpClient) {}

  getUserSubscriptions(): Observable<
    {
      topicId: number;
      topicTitle: string;
      topicDescription: string;
    }[]
  > {
    return this.http.get<
      {
        topicId: number;
        topicTitle: string;
        topicDescription: string;
      }[]
    >(this.subscriptionApiUrl);
  }

  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`${this.subscriptionApiUrl}/${topicId}`);
  }

  getProfile(): Observable<User> {
    return this.http.get<User>('/api/auth/me');
  }

  updateProfile(user: {
    username: string;
    email: string;
    password?: string;
  }): Observable<User> {
    return this.http.put<User>(`${this.profileApiUrl}/update`, user);
  }
}
