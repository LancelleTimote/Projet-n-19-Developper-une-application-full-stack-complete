import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PostsService } from '../../services/posts.service';

@Component({
  selector: 'app-form',
  templateUrl: './form.component.html',
  styleUrls: ['./form.component.scss'],
})
export class FormComponent implements OnInit {
  form!: FormGroup;
  onError: boolean = false;
  themes: { id: number; name: string }[] = [];

  constructor(
    private fb: FormBuilder,
    private postsService: PostsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      topic: ['', Validators.required],
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(10)]],
    });

    this.fetchThemes();
  }

  fetchThemes(): void {
    this.themes = [
      { id: 1, name: 'Technologie' },
      { id: 2, name: 'Science' },
      { id: 3, name: 'Art' },
    ];
  }

  submit(): void {
    if (this.form.invalid) {
      this.onError = true;
      return;
    }

    const formData = new FormData();
    formData.append('topic', this.form.get('topic')!.value);
    formData.append('title', this.form.get('title')!.value);
    formData.append('content', this.form.get('content')!.value);

    this.postsService.create(formData).subscribe({
      next: () => {
        this.router.navigate(['/posts']);
      },
      error: () => {
        this.onError = true;
      },
    });
  }
}
