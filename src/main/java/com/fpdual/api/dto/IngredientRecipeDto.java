package com.fpdual.api.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
/**
 * Clase que representa una asociación entre ingrediente y receta.
 */
public class IngredientRecipeDto {
    private int id;
    private int idRecipe;
    private int idIngredient;
    private String nameIngredient;
    private int quantity;
    private String unit;
    private List<AllergenDto> allergens;

}
