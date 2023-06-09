package com.fpdual.api.dto;

import lombok.*;

/**
 * Clase que representa un alérgeno.
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder

public class AllergenDto {
    private int id;
    private String name;

}
