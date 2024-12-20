import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { OwnerInfoComponent } from './components/owner-info/owner-info.component';
import { HeaderComponent } from './components/header/header.component';
import { BackButtonComponent } from './components/back-button/back-button.component';
import { MatIconModule } from '@angular/material/icon';
import { ButtonComponent } from './components/button/button.component';

@NgModule({
  declarations: [
    OwnerInfoComponent,
    HeaderComponent,
    BackButtonComponent,
    ButtonComponent,
  ],
  imports: [CommonModule, MatIconModule],
  exports: [
    OwnerInfoComponent,
    HeaderComponent,
    BackButtonComponent,
    ButtonComponent,
  ],
})
export class SharedModule {}
