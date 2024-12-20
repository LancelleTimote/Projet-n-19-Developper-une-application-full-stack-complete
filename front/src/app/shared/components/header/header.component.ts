import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from 'src/app/services/session.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent implements OnInit {
  isAuthenticated = false;

  constructor(private sessionService: SessionService, private router: Router) {}

  ngOnInit(): void {
    this.sessionService.$isLogged().subscribe((loggedIn) => {
      this.isAuthenticated = loggedIn;
    });
  }

  goToProfile(): void {
    this.router.navigate(['/profile']);
  }

  logOut(): void {
    this.sessionService.logOut();
    this.router.navigate(['/login']);
  }
}
