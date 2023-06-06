package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

/**
 * Clase que representa una asociación entre rol y usuario.
 */
public class RolUserDto {
    private int idUser, idRol;

}