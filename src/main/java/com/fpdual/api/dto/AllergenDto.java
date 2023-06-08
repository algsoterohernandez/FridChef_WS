package com.fpdual.api.dto;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
/**
 * Clase que representa un alérgeno.
 */
public class AllergenDto {
    private int id;
    private String name;

}
