import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FormComponent } from './components/form/form.component';
import { DetailsComponent } from './components/details/details.component';
import { ListComponent } from './components/list/list.component';

const routes: Routes = [
  { title: 'Posts', path: '', component: ListComponent },
  {
    title: 'Posts - details',
    path: 'details/:id',
    component: DetailsComponent,
  },
  { title: 'Posts - create', path: 'create', component: FormComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class PostRoutingModule {}
