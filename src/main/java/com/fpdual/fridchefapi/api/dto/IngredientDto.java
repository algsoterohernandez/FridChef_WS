package com.fpdual.fridchefapi.api.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class IngredientDto {

    private int id;
    private String name;
    private List<AllergenDto> allergens;

}
