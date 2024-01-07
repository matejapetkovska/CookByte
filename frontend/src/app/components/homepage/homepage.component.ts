import {Component, OnInit, ElementRef, Renderer2, HostListener, AfterViewInit} from '@angular/core';
import {RecipeService} from "../../services/recipe.service";
import {Recipe} from "../../interfaces/recipe";
import {Router} from "@angular/router";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit, AfterViewInit {

  recipes: Recipe[] | undefined

  constructor(private recipeService: RecipeService, private router: Router, private el: ElementRef, private renderer: Renderer2) {
  }

  @HostListener('window:scroll', ['$event'])
  onScroll() {
    this.checkCardsVisibility();
  }

  ngOnInit(): void {
    this.recipeService.getMostFavouriteRecipes().subscribe({
      next: (recipes) => {
        this.recipes = recipes
        for(const recipe of this.recipes) {
          recipe.title = this.recipeService.cleanText(recipe.title)
          recipe.description = this.recipeService.cleanText(recipe.description)
        }
      }, error: () => {
        console.log("error in getting recipes")
      }
    })
  }

  checkCardsVisibility() {
    const cards = this.el.nativeElement.querySelectorAll('.cards-container');
    for (let i = 0; i < cards.length; i++) {
      const windowHeight = window.innerHeight;
      const elementTop = cards[i].getBoundingClientRect().top;
      const elementVisible = 150;
      if (elementTop < windowHeight - elementVisible) {
        cards[i].classList.add("active", "animate__animated", "animate__fadeInUp");
      } else {
        cards[i].classList.remove("active", "animate__animated", "animate__fadeInUp");
      }

    }
  }


  onClick(){
    this.router.navigateByUrl("/recipes")
  }

  ngAfterViewInit(): void {
    const logo = this.el.nativeElement.querySelector('.logo');
    logo.addEventListener('animationend', () => {
      this.startButtonAnimation();
    });
  }

  startButtonAnimation() {
    const button = this.el.nativeElement.querySelector('.text-over-image > button');
    this.renderer.addClass(button, 'fadeIn');
  }
}
