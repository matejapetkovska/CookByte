import { NgModule } from '@angular/core';
import { RouterModule, Routes } from "@angular/router";
import {HomepageComponent} from "./components/homepage/homepage.component";
import {AllRecipesComponent} from "./components/all-recipes/all-recipes.component";
import {AboutUsComponent} from "./components/about-us/about-us.component";
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {AuthGuard} from "./auth.guard";
import {RecipeDetailsComponent} from "./components/recipe-details/recipe-details.component";
import {UserProfileComponent} from "./components/user-profile/user-profile.component";

const routes: Routes = [
  { path: 'home', component: HomepageComponent },
  { path: '', redirectTo: '/home', pathMatch: 'full' },
  { path: 'recipes', component: AllRecipesComponent },
  { path: 'recipes/:recipeId', component: RecipeDetailsComponent },
  { path: 'about', component: AboutUsComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'user-profile', component: UserProfileComponent }
]

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
