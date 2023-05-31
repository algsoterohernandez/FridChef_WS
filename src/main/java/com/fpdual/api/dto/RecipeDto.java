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
    private String imageBase64;
    private RecipeStatus status;
    private List<IngredientRecipeDto> ingredients;

    private double valoration;

}
