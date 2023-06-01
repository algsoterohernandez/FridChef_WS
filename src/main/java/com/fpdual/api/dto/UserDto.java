package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor

/**
 * Clase que representa un usuario.
 */
public class UserDto {
    private int id;
    private String name, surname1, surname2, email, password;

    private boolean alreadyExists;
    private List<RolUserDto> rolUserDto;

}