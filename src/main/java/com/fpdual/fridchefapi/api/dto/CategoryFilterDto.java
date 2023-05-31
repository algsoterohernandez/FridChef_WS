package com.fpdual.fridchefapi.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class CategoryFilterDto {

    List<RecipeDto> recipes;

    public CategoryFilterDto() {

        this.recipes =new ArrayList<>();
    }

    public List<RecipeDto> getRecipes(){
        return recipes;
    }

    public List<RecipeDto> filterRecipesByCategory(int idCategory) {
        List<RecipeDto> filteredRecipes = new ArrayList<>();
        for(RecipeDto recipe : recipes){
            if(recipe.getIdCategory() == idCategory){
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }
}