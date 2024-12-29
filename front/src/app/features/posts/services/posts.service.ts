import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from 'src/app/features/posts/interfaces/post.interface';
import { PostResponse } from '../interfaces/api/postResponse.interface';
import { PostsResponse } from '../interfaces/api/postsResponse.interface';

@Injectable({
  providedIn: 'root',
})
export class PostsService {
  private pathService = 'api/posts';
  private topicsService = 'api/topics';

  constructor(private httpClient: HttpClient) {}

  public all(): Observable<PostsResponse> {
    return this.httpClient.get<PostsResponse>(this.pathService);
  }

  public details(id: string): Observable<Post> {
    return this.httpClient.get<Post>(`${this.pathService}/${id}`);
  }

  create(payload: any): Observable<any> {
    return this.httpClient.post<any>('/api/posts', payload); // Envoi en JSON
  }

  public getTopics(): Observable<{ id: number; name: string }[]> {
    return this.httpClient.get<{ id: number; name: string }[]>(
      this.topicsService
    );
  }
}
