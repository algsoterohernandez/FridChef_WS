package com.fpdual.fridchefapi.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeFilterDto {

    List<String> Ingredients;

    public RecipeFilterDto() {
        this.Ingredients =new ArrayList<>();
    }
}
