package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Clase que representa los criterios de filtrado de recetas.
 */
@Data
@NoArgsConstructor

public class RecipeFilterDto {

    List<String> Ingredients;
}