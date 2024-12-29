import { Component, OnInit } from '@angular/core';
import { PostsService } from '../../services/posts.service';
import { Post } from '../../interfaces/post.interface';

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent implements OnInit {
  posts: Post[] = [];
  onError: boolean = false;

  constructor(private postsService: PostsService) {}

  ngOnInit(): void {
    this.fetchPosts();
  }

  fetchPosts(): void {
    this.postsService.all().subscribe({
      next: (response) => {
        this.posts = response;
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des posts:', error);
        this.onError = true;
      },
    });
  }
}
