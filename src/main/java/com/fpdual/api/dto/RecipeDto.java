package com.fpdual.api.dto;

import com.fpdual.enums.RecipeStatus;
import lombok.*;

import java.sql.Blob;
import java.util.Date;
import java.util.List;

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
    private String unitTime;
    private int idCategory;
    private Date createTime;
    private Blob image;
    private RecipeStatus status;
    private List<IngredientRecipeDto> ingredients;

    public RecipeDto(int id, String name, String description, int difficulty, int time, String unitTime, int idCategory, Date createTime, RecipeStatus status, List<IngredientRecipeDto> ingredients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.time = time;
        this.unitTime = unitTime;
        this.idCategory = idCategory;
        this.createTime = createTime;
        this.status = status;
        this.ingredients = ingredients;
    }
}
