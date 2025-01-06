import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './components/header/header.component';
import { BackButtonComponent } from './components/back-button/back-button.component';
import { MatIconModule } from '@angular/material/icon';
import { ButtonComponent } from './components/button/button.component';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [HeaderComponent, BackButtonComponent, ButtonComponent],
  imports: [CommonModule, MatIconModule, RouterModule],
  exports: [HeaderComponent, BackButtonComponent, ButtonComponent],
})
export class SharedModule {}
