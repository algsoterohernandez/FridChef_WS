package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
/**
 * Clase que representa los criterios de filtrado de recetas.
 */
public class RecipeFilterDto {

    List<String> Ingredients;
}