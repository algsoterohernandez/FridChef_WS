package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Clase que representa un usuario.
 */
@Data
@NoArgsConstructor


public class UserDto {
    private int id;
    private String name, surname1, surname2, email, password;
    private boolean alreadyExists;
    private List<RolUserDto> rolUserDto;
    private List<Integer> favoriteList;

}