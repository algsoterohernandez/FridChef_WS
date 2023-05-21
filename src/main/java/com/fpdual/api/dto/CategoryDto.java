package com.fpdual.api.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryDto {
    private int id;
    private String name;

    public CategoryDto(String name){this.name = name;}
}
