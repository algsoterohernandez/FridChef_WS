package com.fpdual.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IngredientRecipeDto {
    private int id;
    private int idRecipe;
    private int idIngredient;
    private int quantity;
    private String unit;
}
