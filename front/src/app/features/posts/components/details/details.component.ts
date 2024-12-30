import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostsService } from '../../services/posts.service';
import { Post } from '../../interfaces/post.interface';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Comment } from '../../interfaces/comment.interface';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
})
export class DetailsComponent implements OnInit {
  post?: Post;
  comments: Comment[] = [];
  form!: FormGroup;
  isSubmitting = false;

  constructor(
    private route: ActivatedRoute,
    private postsService: PostsService,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.initForm();

    this.postsService.details(postId.toString()).subscribe(
      (data) => (this.post = data),
      (error) => console.error('Error fetching post details:', error)
    );

    this.postsService.getComments(postId).subscribe(
      (comments) => (this.comments = comments),
      (error) => console.error('Error fetching comments:', error)
    );
  }

  initForm(): void {
    this.form = this.fb.group({
      content: ['', [Validators.required, Validators.minLength(5)]],
    });
  }

  submit(): void {
    const postId = this.post?.id;
    const userId = 1;

    if (this.form.valid && postId) {
      this.isSubmitting = true;

      const newComment = {
        content: this.form.value.content,
        post: { id: postId },
        user: { id: userId },
      };

      this.postsService.createComment(newComment).subscribe(
        (comment) => {
          console.log('Réponse du serveur (commentaire créé) :', comment);
          this.comments.push(comment);
          this.form.reset();
          this.isSubmitting = false;
        },
        (error) => {
          console.error('Erreur lors de la création du commentaire:', error);
          alert('Erreur lors de la création du commentaire.');
          this.isSubmitting = false;
        }
      );
    }
  }
}
