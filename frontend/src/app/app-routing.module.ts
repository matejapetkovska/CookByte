import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import {HomepageComponent} from "./components/homepage/homepage.component";
import {AllRecipesComponent} from "./components/all-recipes/all-recipes.component";

const routes: Routes = [
  { path: 'home', component: HomepageComponent},
  { path: '', redirectTo: '/home', pathMatch: 'full'},
  { path: 'recipes', component: AllRecipesComponent }
]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
