import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DetailsService {
  private subscriptionApiUrl = '/api/subscriptions';

  constructor(private http: HttpClient) {}

  /**
   * Récupère les abonnements de l'utilisateur connecté.
   * @returns Observable avec une liste d'abonnements.
   */
  getUserSubscriptions(): Observable<
    { topicId: number; topicTitle: string; topicDescription: string }[]
  > {
    return this.http.get<
      { topicId: number; topicTitle: string; topicDescription: string }[]
    >(`${this.subscriptionApiUrl}`);
  }

  /**
   * Désabonne l'utilisateur du topic spécifié.
   * @param topicId - L'ID du topic à désabonner.
   * @returns Observable vide.
   */
  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`${this.subscriptionApiUrl}/${topicId}`);
  }
}
