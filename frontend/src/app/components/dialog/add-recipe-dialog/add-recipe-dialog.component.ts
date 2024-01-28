import {Component, Input, Output, EventEmitter, OnInit, NgZone} from '@angular/core';
import {Router} from "@angular/router";
import {CategoryService} from "../../../services/category.service";
import {Category} from "../../../interfaces/category";
import {Ingredient} from "../../../interfaces/ingredient";
import {RecipeService} from "../../../services/recipe.service";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";

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
  addRecipe: FormGroup

  closeDialog() {
    this.isVisible = false;
    this.onClose.emit();
  }

  constructor(private categoryService: CategoryService, private recipeService: RecipeService,
              private router: Router, private formBuilder: FormBuilder) {
    this.addRecipe = this.createForm();
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
    const token = localStorage.getItem('token');
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
      formData.append('ingredients',  JSON.stringify(this.ingredients));
      formData.append('categoryIds', JSON.stringify(selectedCategoryIds));
      if(token != null)
        formData.append('token', token.toString());
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
          this.router.navigate(['/user-profile'])
        }
      });
  }
}
