package com.fpdual.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRecipeDto {
    private int id;
    private int idRecipe;
    private int idIngredient;
    private String nameIngredient;
    private int quantity;
    private String unit;
    private List<AllergenDto> allergens;

}
