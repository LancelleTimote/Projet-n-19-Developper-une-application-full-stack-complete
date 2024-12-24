import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { DetailsService } from '../../services/details.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/features/auth/services/auth.service';
import { passwordValidator } from 'src/app/validators/password.validator';

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
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {
    this.profileForm = this.formBuilder.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', passwordValidator], // Optional or conditional validation
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
        console.log('User profile loaded:', user);
        this.profileForm.patchValue({
          username: user.username,
          email: user.email,
          password: '', // Do not expose the password for update
        });

        this.profileForm.addControl('id', this.formBuilder.control(user.id));
      },
      error: (err) => {
        console.error('Error loading profile:', err);
      },
    });
  }

  onSubmit(): void {
    const updatedUser = this.profileForm.value;
    const currentUser = this.sessionService.getCurrentUser();

    if (!updatedUser.id) {
      console.error('User ID missing');
      return;
    }

    const updatedData: any = {
      id: updatedUser.id,
    };

    if (updatedUser.username !== currentUser.username) {
      updatedData.username = updatedUser.username;
    }

    if (updatedUser.email !== currentUser.email) {
      updatedData.email = updatedUser.email;
    }

    if (updatedUser.password && updatedUser.password !== '') {
      updatedData.password = updatedUser.password;
    }

    if (Object.keys(updatedData).length === 0) {
      alert('No changes detected.');
      return;
    }

    this.detailsService.updateProfile(updatedData).subscribe({
      next: (response) => {
        console.log('Profile updated successfully:', response);
        alert('Profile updated!');

        const updatedUserData = { ...currentUser, ...updatedData };
        localStorage.setItem('currentUser', JSON.stringify(updatedUserData));
      },
      error: (error) => {
        console.error('Error updating profile:', error);
        alert(`Failed to update profile. Error: ${error.error}`);
      },
    });
  }

  logOut(): void {
    this.sessionService.logOut();
    this.router.navigate(['/login']);
  }
}
