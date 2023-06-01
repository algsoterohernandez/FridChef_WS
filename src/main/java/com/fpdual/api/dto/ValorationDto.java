package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
/**
 * Clase que representa una valoración.
 */
public class ValorationDto {
    private int id;
    private int idRecipe;
    private int idUser;
    private String comment;
    private double valoration;
}
