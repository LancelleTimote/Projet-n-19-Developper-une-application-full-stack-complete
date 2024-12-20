import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-back-button',
  templateUrl: './back-button.component.html',
  styleUrls: ['./back-button.component.scss'],
})
export class BackButtonComponent {
  constructor(private location: Location, private router: Router) {}

  goBack(): void {
    const currentRoute = this.router.url;
    if (
      ['/login', '/register'].includes(currentRoute) ||
      !window.history.length
    ) {
      this.router.navigate(['/']);
    } else {
      this.location.back();
    }
  }
}
