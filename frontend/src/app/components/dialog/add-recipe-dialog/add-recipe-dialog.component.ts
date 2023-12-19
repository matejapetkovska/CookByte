import {Component, Input, Output, EventEmitter, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {CategoryService} from "../../../services/category.service";
import {Category} from "../../../interfaces/category";
import {Ingredient} from "../../../interfaces/ingredient";
import {RecipeService} from "../../../services/recipe.service";

@Component({
  selector: 'app-add-recipe-dialog',
  templateUrl: './add-recipe-dialog.component.html',
  styleUrls: ['./add-recipe-dialog.component.css']
})
export class AddRecipeDialogComponent implements OnInit {

  @Input() isVisible: boolean = true;
  @Output() onClose: EventEmitter<void> = new EventEmitter<void>();
  categories: Category[] | undefined
  title: String = ''
  description: String = ''
  cookTime: number | undefined
  calories: String = ''
  carbohydrates: String = ''
  proteins: String = ''
  fats: String = ''
  instructions: String = ''
  selectedFile: File | null = null
  selectedCategoryIds: number[] = []
  ingredient: Ingredient = {
    name: ''
  }
  ingredients: Ingredient[] = []
  imagePreview: string | ArrayBuffer | null = null;

  closeDialog() {
    this.isVisible = false;
    this.onClose.emit();
  }

  constructor(private categoryService: CategoryService, private recipeService: RecipeService,
              private router: Router) { }

  ngOnInit(): void {
    this.categoryService.getAllCategories()
      .subscribe({
        next: (categories) => {
          this.categories = categories;
        },
        error: () => {
          console.log('error in getting categories');
        }
      });
  }

  onFileChange(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile) {
      this.previewImage(this.selectedFile);
    }
  }
  previewImage(file: File): void {
    const reader = new FileReader();
    reader.onload = (e) => {
      this.imagePreview = e.target?.result || null;
    };

    const container = document.getElementById('image-preview-container');
    if (container) {
      container.style.backgroundImage = `url(${this.imagePreview})`;
      container.classList.add('has-image');
    }
    reader.readAsDataURL(file);
  }

  onAddIngredient() {
    if (this.ingredient.name != "") {
      let copyOfIngredient = {...this.ingredient}
      this.ingredients.push(copyOfIngredient)
      this.ingredient.name = ''
    }
  }

  createFormData(): FormData {
    const formData = new FormData();
    const token = localStorage.getItem('token');
    if (this.title != null && this.description != null && this.selectedFile != null &&
      this.cookTime != undefined && this.calories != undefined && this.carbohydrates != undefined &&
      this.fats != undefined && this.proteins != undefined && this.instructions != null &&
      this.selectedCategoryIds != null && this.ingredients != null && token != null) {
      formData.append('title', this.title.toString());
      formData.append('description', this.description.toString());
      formData.append('file', this.selectedFile);
      formData.append('cookTime', this.cookTime.toString());
      formData.append('calories', this.calories.toString());
      formData.append('carbohydrates', this.carbohydrates.toString());
      formData.append('fats', this.fats.toString());
      formData.append('proteins', this.proteins.toString());
      formData.append('instructions', this.instructions.toString());
      formData.append('ingredients',  JSON.stringify(this.ingredients));
      formData.append('categoryIds', JSON.stringify(this.selectedCategoryIds));
      formData.append('token', token.toString());
    }
    return formData;
  }

  onAddRecipe() {
    const formData = this.createFormData()
    this.recipeService.addRecipe(formData)
      .subscribe({
        next: () => {
          this.router.navigate(['/recipes'])
        },
        error: (error) => {
          if (error.status === 400) {
            console.log("Error")
          }
          this.router.navigate(['/login'])
        }
      });
  }
}
