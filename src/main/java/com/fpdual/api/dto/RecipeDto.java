package com.fpdual.api.dto;

import lombok.*;

import java.sql.Blob;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder

public class RecipeDto {
    private int id;
    private String name;
    private String description;
    private int difficulty;
    private int time;
    private String unit_time;
    private int id_category;
    private Date create_time;
    private Blob image;

    public RecipeDto(int id, String name, String description, int difficulty, int time, String unit_time, int id_category, Date crete_time) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.time = time;
        this.unit_time = unit_time;
        this.id_category = id_category;
        this.create_time = crete_time;
    }
}
