package com.fpdual.api.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor


/**
 * Clase que representa un DTO (Data Transfer Object) para filtrar recetas por categoría.
 */
public class CategoryFilterDto {

    private List<RecipeDto> recipes;

    /**
     * Constructor de la clase CategoryFilterDto.
     * Inicializa la lista de recetas.
     */
    public CategoryFilterDto() {
        this.recipes = new ArrayList<>();
    }

    /**
     * Obtiene la lista de recetas.
     *
     * @return La lista de recetas.
     */
    public List<RecipeDto> getRecipes(){
        return recipes;
    }

    /**
     * Filtra las recetas por categoría.
     *
     * @param idCategory El ID de la categoría por la cual filtrar las recetas.
     * @return La lista de recetas filtradas por categoría.
     */
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
