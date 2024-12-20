import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss'],
})
export class ButtonComponent {
  @Input() label: string = '';
  @Input() type: 'button' | 'submit' = 'button';

  @Output() onClick = new EventEmitter<void>();

  handleClick(): void {
    this.onClick.emit();
  }
}
