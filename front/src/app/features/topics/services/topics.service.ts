import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TopicsService {
  private apiUrl = `${environment.apiUrl}/topics`;
  private subscriptionApiUrl = `${environment.apiUrl}/subscriptions`;

  constructor(private http: HttpClient) {}

  getTopics(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  subscribeToTopic(topicId: number): Observable<any> {
    return this.http
      .post<any>(
        `${this.subscriptionApiUrl}/${topicId}`,
        {},
        { observe: 'response' }
      )
      .pipe(
        map((response) => {
          if (response.status === 201) {
            return (
              response.body || {
                status: 'success',
                message: 'Successfully subscribed to topic',
              }
            );
          }
          return response.body;
        }),
        catchError((error) => {
          console.error('Erreur dans subscribeToTopic:', error);
          throw error;
        })
      );
  }
}
