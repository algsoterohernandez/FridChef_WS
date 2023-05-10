package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor

public class UserDto {
    private int id;
    private String name, surname1, surname2, email, password;

    private boolean alreadyExists;


}