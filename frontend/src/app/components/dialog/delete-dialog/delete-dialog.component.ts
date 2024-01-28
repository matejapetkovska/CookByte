import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {RecipeService} from "../../../services/recipe.service";
import {Router} from "@angular/router";
import { Location } from '@angular/common';

@Component({
  selector: 'app-delete-dialog',
  templateUrl: './delete-dialog.component.html',
  styleUrls: ['./delete-dialog.component.css']
})
export class DeleteDialogComponent {

  recipeId: number
  recipeTitle: string

  constructor(public dialogRef: MatDialogRef<DeleteDialogComponent>,
              private recipeService: RecipeService,
              private router: Router,
              @Inject(MAT_DIALOG_DATA) public data: { recipeId: number, recipeTitle: string }) {
    this.recipeId = data.recipeId
    this.recipeTitle = data.recipeTitle
  }

  deleteRecipe() {
    this.recipeService.deleteRecipe(this.recipeId.toString()).subscribe(
      {
        next: () => {
          location.reload();
        },
        error: (error) => {
          console.log('Error in deleting recipe:', error);
        }
      }
    );
  }
}
