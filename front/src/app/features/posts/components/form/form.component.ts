import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { PostsService } from '../../services/posts.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
})
export class FormComponent implements OnInit {
  form!: FormGroup;
  onError: boolean = false;
  onSuccess: boolean = false;
  topics: { id: number; name: string }[] = [];

  constructor(
    private fb: FormBuilder,
    private postsService: PostsService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      topic: ['', Validators.required],
      title: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ],
      ],
      content: ['', [Validators.required, Validators.minLength(10)]],
    });

    this.fetchTopics();
  }

  fetchTopics(): void {
    this.postsService.getTopics().subscribe({
      next: (topics) => {
        this.topics = topics;
      },
      error: () => {
        this.onError = true;
      },
    });
  }

  submit(): void {
    if (this.form.invalid) {
      this.onError = true;
      console.error('Le formulaire est invalide', this.form.errors);
      return;
    }

    const currentUser = this.authService.currentUser;

    if (!currentUser) {
      console.error('Utilisateur non connecté');
      this.onError = true;
      return;
    }

    const payload = {
      topic: { id: this.form.get('topic')!.value },
      title: this.form.get('title')!.value,
      content: this.form.get('content')!.value,
      author: { id: currentUser.id },
    };

    console.log('Payload envoyé au backend:', payload);

    this.postsService.create(payload).subscribe({
      next: (response) => {
        console.log('Création réussie');
        this.onError = false;
        this.onSuccess = true;
        this.form.reset();
        this.router.navigate(['/posts']);
      },
      error: (error) => {
        console.error("Erreur lors de l'envoi au backend:", error);
        this.onError = true;
        this.onSuccess = false;
      },
    });
  }
}
