import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { DetailsService } from '../../services/details.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { passwordValidator } from 'src/app/validators/password.validator';
import { usernameValidator } from '../../../../validators/username.validator';

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss'],
})
export class DetailsComponent implements OnInit {
  subscriptions: {
    topicId: number;
    topicTitle: string;
    topicDescription: string;
  }[] = [];
  profileForm: FormGroup;

  constructor(
    private detailsService: DetailsService,
    private router: Router,
    private sessionService: SessionService,
    private formBuilder: FormBuilder
  ) {
    this.profileForm = this.formBuilder.group({
      username: ['', [usernameValidator]],
      email: ['', [Validators.email]],
      password: ['', []],
    });
  }

  ngOnInit(): void {
    this.loadSubscriptions();
    this.loadUserProfile();
  }

  loadSubscriptions(): void {
    this.detailsService.getUserSubscriptions().subscribe({
      next: (data) => {
        this.subscriptions = data;
      },
      error: (err) => {
        console.error('Error loading subscriptions:', err);
      },
    });
  }

  unsubscribe(topicId: number): void {
    this.detailsService.unsubscribe(topicId).subscribe({
      next: () => {
        this.subscriptions = this.subscriptions.filter(
          (sub) => sub.topicId !== topicId
        );
      },
      error: (err) => {
        console.error('Error unsubscribing:', err);
      },
    });
  }

  loadUserProfile(): void {
    this.detailsService.getProfile().subscribe({
      next: (user) => {
        this.profileForm.patchValue({
          username: user.username,
          email: user.email,
          password: '',
        });
        console.log(this.profileForm.value);

        this.profileForm.addControl('id', this.formBuilder.control(user.id));
      },
      error: (err) => {
        console.error('Error loading profile:', err);
      },
    });
  }

  isFormModified(): boolean {
    const currentUser = this.sessionService.getCurrentUser();

    const usernameChanged =
      this.profileForm.get('username')?.value !== currentUser.username;
    const emailChanged =
      this.profileForm.get('email')?.value !== currentUser.email;
    const passwordChanged =
      this.profileForm.get('password')?.value &&
      this.profileForm.get('password')?.value.trim() !== '';

    return usernameChanged || emailChanged || passwordChanged;
  }

  onSubmit(): void {
    const passwordControl = this.profileForm.get('password');

    if (
      passwordControl &&
      passwordControl.value &&
      passwordControl.value.trim() !== ''
    ) {
      passwordControl.setValidators([passwordValidator]);
    } else {
      passwordControl?.clearValidators();
    }

    passwordControl?.updateValueAndValidity();

    if (this.profileForm.invalid) {
      alert('Veuillez vérifier que tous les champs sont correctement remplis.');
      return;
    }

    const updatedUser = this.profileForm.value;
    const currentUser = this.sessionService.getCurrentUser();

    const updatedData: any = { id: currentUser.id };

    if (updatedUser.username && updatedUser.username !== currentUser.username) {
      updatedData.username = updatedUser.username;
    }

    if (updatedUser.email && updatedUser.email !== currentUser.email) {
      updatedData.email = updatedUser.email;
    }

    if (updatedUser.password && updatedUser.password.trim() !== '') {
      updatedData.password = updatedUser.password;
    } else {
      delete updatedData.password;
    }

    if (Object.keys(updatedData).length === 1) {
      alert('Aucune modification détectée.');
      return;
    }

    this.detailsService.updateProfile(updatedData).subscribe({
      next: (response) => {
        alert('Profil mis à jour avec succès !');

        if (updatedData.email && updatedData.email !== currentUser.email) {
          this.sessionService.logOut();
          this.router.navigate(['/']);
        } else {
          localStorage.setItem(
            'currentUser',
            JSON.stringify({ ...currentUser, ...updatedData })
          );
        }
      },
      error: (error) => {
        alert(`Erreur lors de la mise à jour : ${error.error}`);
      },
    });
  }

  logOut(): void {
    this.sessionService.logOut();
    this.router.navigate(['/']);
  }
}
