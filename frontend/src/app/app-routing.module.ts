import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import {HomepageComponent} from "./components/homepage/homepage.component";
import {AllRecipesComponent} from "./components/all-recipes/all-recipes.component";
import {AboutUsComponent} from "./components/about-us/about-us.component";

const routes: Routes = [
  { path: 'home', component: HomepageComponent},
  { path: '', redirectTo: '/home', pathMatch: 'full'},
  { path: 'recipes', component: AllRecipesComponent },
  { path: 'about', component: AboutUsComponent }
]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
