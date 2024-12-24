import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss'],
})
export class ButtonComponent {
  @Input() label: string = '';
  @Input() type: 'button' | 'submit' = 'button';
  @Input() navigateTo?: string;
  @Input() disabled: boolean = false;

  @Output() onClick = new EventEmitter<void>();

  constructor(private router: Router) {}

  handleClick(): void {
    this.onClick.emit();
    if (this.navigateTo) {
      this.router.navigate([this.navigateTo]);
    }
  }
}
