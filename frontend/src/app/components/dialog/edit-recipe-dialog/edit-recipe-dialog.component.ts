import {Component, Input, Output, EventEmitter, OnInit, Inject} from '@angular/core';
import {Router} from "@angular/router";
import {CategoryService} from "../../../services/category.service";
import {Category} from "../../../interfaces/category";
import {Ingredient} from "../../../interfaces/ingredient";
import {RecipeService} from "../../../services/recipe.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {Recipe} from "../../../interfaces/recipe";
import {User} from "../../../interfaces/user";
import {Review} from "../../../interfaces/review";
import {IngredientsService} from "../../../services/ingredients.service";

@Component({
  selector: 'app-edit-recipe-dialog',
  templateUrl: './edit-recipe-dialog.component.html',
  styleUrls: ['./edit-recipe-dialog.component.css']
})
export class EditRecipeDialogComponent implements OnInit {

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
  addRecipe: FormGroup
  recipeId: number
  recipe: Recipe | undefined
  user: User | undefined
  reviews: Review[] | undefined
  recipeRatingMap = new Map<number, { totalRating: number, count: number }>();
  ingredients: Ingredient[] = []

  closeDialog() {
    this.dialogRef.close()
  }

  constructor(public dialogRef: MatDialogRef<EditRecipeDialogComponent>,
              private recipeService: RecipeService,
              private categoryService: CategoryService,
              private ingredientService: IngredientsService,
              private router: Router,
              private formBuilder: FormBuilder,
              @Inject(MAT_DIALOG_DATA) public data: { recipeId: number, recipeTitle: string }) {
    this.recipeId = data.recipeId
    this.addRecipe = this.createForm()
  }

  createForm(): FormGroup {
    return this.formBuilder.group({
      title: ['', Validators.required],
      image: [null],
      description: ['', Validators.required],
      ingredientName: [''],
      ingredients: this.formBuilder.array([]),
      instructions: ['', Validators.required],
      cookTime: [null, Validators.required],
      calories: [null, Validators.required],
      carbohydrates: [null, Validators.required],
      fats: [null, Validators.required],
      proteins: [null, Validators.required],
      selectedCategoryIds: [[]],
    });
  }


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
    if (!this.recipeId) {
      return;
    }
    this.recipeService.getRecipeDetails(this.recipeId).subscribe((recipe) => {
        this.recipe = recipe;
        this.addRecipe.patchValue({
          title: this.recipe.title,
          description: this.recipe.description,
          instructions: this.recipe.instructions,
          cookTime: this.recipe.cookTime,
          calories: this.recipe.calories,
          carbohydrates: this.recipe.carbohydrates,
          fats: this.recipe.fats,
          proteins: this.recipe.proteins,
          selectedCategoryIds: this.recipe.categories.map(category => category.id)
        });
        if (!this.recipe.imageUrl.startsWith('http://') && !this.recipe.imageUrl.startsWith('https://')) {
          this.addPathToRecipeImages(recipe);
        }

        if (this.recipe.datePublished) {
          const originalDate = new Date(this.recipe.datePublished);
          this.recipe.datePublished = originalDate.toLocaleDateString('en-US', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
          });
        }
        this.ingredientService.getIngredientsForRecipe(this.recipeId).subscribe({
          next: (ingredients) => {
            this.ingredients = ingredients;
            const ingredientNames = ingredients.map(ingredient => ingredient.name);
            this.addRecipe.get('ingredientName')?.setValue(ingredientNames.join(', '));
          }
        });
      },
      (error) => {
        console.error('Error fetching recipe details:', error);
      });
  }


  onFileChange(event: any) {
    const file = event.target.files[0];
    this.addRecipe.setValue({
      image: file
    });
  }

  onAddIngredient() {
    const ingredientName = this.addRecipe.get('ingredientName')?.value;
    if (ingredientName !== "") {
      this.ingredients.push({ name: ingredientName });
      this.addRecipe.get('ingredientName')?.setValue('');
    }
  }

  createFormData(): FormData {
    const formData = new FormData();
    const {
      title,
      description,
      image,
      instructions,
      cookTime,
      calories,
      carbohydrates,
      fats,
      proteins,
      selectedCategoryIds,
    } = this.addRecipe.value;
    formData.append('title', title.toString());
    formData.append('description', description.toString());
    formData.append('image', image);
    formData.append('cookTime', cookTime.toString());
    formData.append('calories', calories.toString());
    formData.append('carbohydrates', carbohydrates.toString());
    formData.append('fats', fats.toString());
    formData.append('proteins', proteins.toString());
    formData.append('instructions', instructions.toString());
    formData.append('ingredientIds',  JSON.stringify(this.ingredients));
    formData.append('categoryIds', JSON.stringify(selectedCategoryIds));
    formData.append('datePublished', new Date().toString());
    return formData;
  }

  onEditRecipe() {
    const formData = this.createFormData();
    this.recipeService.editRecipe(this.recipeId, formData).subscribe({
      next: () => {
        this.dialogRef.close()
        this.router.navigate([`/recipes/${this.recipeId}`]);
      },
      error: (error) => {
        if (error.status === 400) {
          console.log("Error");
        }
        this.router.navigate(['/user-profile']);
      }
    });
  }


  addPathToRecipeImages(recipes: Recipe) {
    recipes.imageUrl = "../../../assets/user-uploaded-images/" + recipes.imageUrl;
  }
}
