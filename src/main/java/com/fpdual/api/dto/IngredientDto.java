package com.fpdual.api.dto;

import lombok.*;

import java.util.List;

/**
 * Clase que representa un Ingrediente.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class IngredientDto {

    private int id;
    private String name;
    private List<AllergenDto> allergens;

}
