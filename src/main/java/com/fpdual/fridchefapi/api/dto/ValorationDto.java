package com.fpdual.fridchefapi.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValorationDto {
    private int id;
    private int idRecipe;
    private int idUser;
    private String comment;
    private double valoration;
}
