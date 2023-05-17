package com.fpdual.utils;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.api.dto.IngredientDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;

import java.util.List;
import java.util.stream.Collectors;

public class MappingUtils {

    public static RecipeDto mapRecipeDto(RecipeDao recipeDao) {
        RecipeDto recipeDto = new RecipeDto();

        recipeDto.setId(recipeDao.getId());
        recipeDto.setName(recipeDao.getName());
        recipeDto.setDescription(recipeDao.getDescription());
        recipeDto.setDifficulty(recipeDao.getDifficulty());
        recipeDto.setTime(recipeDao.getTime());
        recipeDto.setUnit_time(recipeDao.getUnitTime());
        recipeDto.setId_category(recipeDao.getIdCategory());
        recipeDto.setCreate_time(recipeDao.getCreateTime());

        List<IngredientDto> ingredients = recipeDao.getIngredients().stream()
                .map(MappingUtils::mapIngredientDto)
                .collect(Collectors.toList());

        recipeDto.setIngredients(ingredients);

        return recipeDto;
    }

    public static List<RecipeDto> mapRecipeDto(List<RecipeDao> recipeDaos) {
        return recipeDaos.stream()
                .map(MappingUtils::mapRecipeDto)
                .collect(Collectors.toList());
    }


    public static IngredientDto mapIngredientDto(IngredientDao ingredientDao) {
        IngredientDto ingredientDto = new IngredientDto();

        ingredientDto.setId(ingredientDao.getId());
        ingredientDto.setName(ingredientDao.getName());

        if (ingredientDao.getAllergens() != null) {
            List<AllergenDto> allergens = ingredientDao.getAllergens().stream()
                    .map(MappingUtils::mapAllergenDto)
                    .collect(Collectors.toList());
            ingredientDto.setAllergens(allergens);
        }

        return ingredientDto;
    }

    public static List<IngredientDto> mapIngredientDto(List<IngredientDao> ingredientDaos) {
        return ingredientDaos.stream()
                .map(MappingUtils::mapIngredientDto)
                .collect(Collectors.toList());
    }

    public static AllergenDto mapAllergenDto(AllergenDao allergenDao) {

        AllergenDto allergenDto = new AllergenDto();

        allergenDto.setId(allergenDao.getId());
        allergenDto.setName(allergenDao.getName());

        return allergenDto;
    }

    public static List<AllergenDto> mapAllergenDto(List<AllergenDao> allergenDaos) {
        return allergenDaos.stream()
                .map(MappingUtils::mapAllergenDto)
                .collect(Collectors.toList());
    }
}
