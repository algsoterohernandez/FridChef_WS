package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa una asociación entre rol y usuario.
 */
@Data
@NoArgsConstructor


public class RolUserDto {
    private int idUser, idRol;

}