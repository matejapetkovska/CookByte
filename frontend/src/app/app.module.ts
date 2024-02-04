import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { HomepageComponent } from './components/homepage/homepage.component';
import { AppRoutingModule } from './app-routing.module';
import {HttpClientModule} from "@angular/common/http";
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { AllRecipesComponent } from './components/all-recipes/all-recipes.component';
import { AboutUsComponent } from './components/about-us/about-us.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import {FormsModule} from "@angular/forms";
import { RecipeDetailsComponent } from './components/recipe-details/recipe-details.component';
import { UserProfileComponent } from './components/user-profile/user-profile.component';
import {AddRecipeDialogComponent} from "./components/dialog/add-recipe-dialog/add-recipe-dialog.component";
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatIconModule } from '@angular/material/icon';
import {MatExpansionModule} from "@angular/material/expansion";
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import {MatChipsModule} from '@angular/material/chips';
import { ReactiveFormsModule} from '@angular/forms';
import {MatCardModule} from "@angular/material/card";
import { NgxMatFileInputModule } from '@angular-material-components/file-input';
import { DeleteDialogComponent } from './components/dialog/delete-dialog/delete-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTabsModule } from '@angular/material/tabs';
import { EditRecipeDialogComponent } from './components/dialog/edit-recipe-dialog/edit-recipe-dialog.component';
import { MatListModule } from '@angular/material/list';

@NgModule({
  declarations: [
    AppComponent,
    HomepageComponent,
    HeaderComponent,
    FooterComponent,
    AllRecipesComponent,
    AboutUsComponent,
    LoginComponent,
    RegisterComponent,
    RecipeDetailsComponent,
    UserProfileComponent,
    AddRecipeDialogComponent,
    DeleteDialogComponent,
    EditRecipeDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatSelectModule,
    MatIconModule,
    MatExpansionModule,
    MatButtonModule,
    MatInputModule,
    MatChipsModule,
    MatCardModule,
    ReactiveFormsModule,
    NgxMatFileInputModule,
    MatDialogModule,
    MatTabsModule,
    MatListModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
