import { Component, ViewEncapsulation } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { AuthSuccess } from '../../interfaces/authSuccess.interface';
import { User } from 'src/app/interfaces/user.interface';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { passwordValidator } from '../../../../validators/password.validator';
import { usernameValidator } from '../../../../validators/username.validator';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class RegisterComponent {
  hide: boolean = true;
  public onError = false;
  isMobile: boolean = false;

  public form = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    username: ['', [Validators.required, usernameValidator]],
    password: ['', [Validators.required, passwordValidator]],
  });

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router,
    private sessionService: SessionService,
    private breakpointObserver: BreakpointObserver
  ) {}

  ngOnInit(): void {
    this.breakpointObserver
      .observe([Breakpoints.Handset])
      .subscribe((result) => {
        this.isMobile = result.matches;
      });
  }

  submit(): void {
    const registerRequest = this.form.value as RegisterRequest;

    this.authService.register(registerRequest).subscribe(
      (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.authService.me().subscribe(
          (user: User) => {
            this.sessionService.logIn(user);
            this.router.navigate(['/posts']);
          },
          (error) => console.error('Error fetching user profile:', error)
        );
      },
      (error) => {
        console.error('Registration error:', error);
        this.onError = true;
      }
    );
  }
}
