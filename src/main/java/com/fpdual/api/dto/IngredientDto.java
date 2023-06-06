package com.fpdual.api.dto;

import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

/**
 * Clase que representa un Ingrediente.
 */
public class IngredientDto {

    private int id;
    private String name;
    private List<AllergenDto> allergens;

}
