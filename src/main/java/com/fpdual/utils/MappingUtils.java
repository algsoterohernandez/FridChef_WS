package com.fpdual.utils;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.api.dto.IngredientDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;

import java.util.ArrayList;
import java.util.List;

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

        List<IngredientDto> ingredients = new ArrayList<>();

        for (IngredientDao ingredientDao : recipeDao.getIngredients()) {
            ingredients.add(MappingUtils.mapIngredientDto(ingredientDao));
        }

        recipeDto.setIngredients(ingredients);

        return recipeDto;
    }

    public static List<RecipeDto> mapRecipeDto(List<RecipeDao> recipeDaos) {
        List<RecipeDto> recipesDtos = new ArrayList<>();

        for (RecipeDao recipeDao : recipeDaos) {
            RecipeDto recipeDto = mapRecipeDto(recipeDao);
            recipesDtos.add(recipeDto);
        }

        return recipesDtos;
    }

    public static IngredientDto mapIngredientDto(IngredientDao ingredientDao) {
        IngredientDto ingredientDto = new IngredientDto();

        ingredientDto.setId(ingredientDao.getId());
        ingredientDto.setName(ingredientDao.getName());

        if (ingredientDao.getAllergens() != null) {
            List<AllergenDto> allergens = new ArrayList<>();

            for (AllergenDao allergenDao : ingredientDao.getAllergens()) {
                allergens.add(MappingUtils.mapAllergenDto(allergenDao));
            }

            ingredientDto.setAllergens(allergens);
        }

        return ingredientDto;
    }

    public static List<IngredientDto> mapIngredientDto(List<IngredientDao> ingredientDaos) {
        List<IngredientDto> ingredientDtos = new ArrayList<>();

        for (IngredientDao ingredientDao : ingredientDaos) {
            IngredientDto ingredientDto = mapIngredientDto(ingredientDao);
            ingredientDtos.add(ingredientDto);
        }

        return ingredientDtos;
    }

    public static AllergenDto mapAllergenDto(AllergenDao allergenDao) {
        AllergenDto allergenDto = new AllergenDto();

        allergenDto.setId(allergenDao.getId());
        allergenDto.setName(allergenDao.getName());

        return allergenDto;
    }
}
