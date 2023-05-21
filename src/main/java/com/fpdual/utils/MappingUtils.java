package com.fpdual.utils;

import com.fpdual.api.dto.*;
import com.fpdual.persistence.aplication.dao.*;
import com.fpdual.api.dto.IngredientRecipeDto;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;


import java.sql.Date;
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
        recipeDto.setUnitTime(recipeDao.getUnitTime());
        recipeDto.setIdCategory(recipeDao.getIdCategory());
        recipeDto.setCreateTime(recipeDao.getCreateTime());
        recipeDto.setStatus(recipeDao.getStatus());
        recipeDto.setIngredients(mapToDto(recipeDao.getIngredients()));


        List<IngredientRecipeDto> ingredients = recipeDao.getIngredients().stream()
                .map(MappingUtils::mapToDto)
                .collect(Collectors.toList());

        recipeDto.setIngredients(ingredients);

        return recipeDto;
    }
    public RecipeDao mapToDao(RecipeDto recipeDto) {

        RecipeDao recipeDao = new RecipeDao();

        recipeDao.setId(recipeDto.getId());
        recipeDao.setName(recipeDto.getName());
        recipeDao.setDescription(recipeDto.getDescription());
        recipeDao.setDifficulty(recipeDto.getDifficulty());
        recipeDao.setTime(recipeDto.getTime());
        recipeDao.setUnitTime(recipeDto.getUnitTime());
        recipeDao.setIdCategory(recipeDto.getIdCategory());
        recipeDao.setCreateTime(new Date(System.currentTimeMillis()));
        recipeDao.setImage(recipeDto.getImage());
        recipeDao.setStatus(recipeDto.getStatus());
        recipeDao.setIngredients(mapToDao(recipeDto.getIngredients()));
        return recipeDao;

    }

    public static List<RecipeDto> mapRecipeDto(List<RecipeDao> recipeDaos) {
        return recipeDaos.stream()
                .map(MappingUtils::mapRecipeDto)
                .collect(Collectors.toList());
    }

    public static IngredientRecipeDto mapToDto(IngredientRecipeDao ingredientRecipeDao) {
        IngredientRecipeDto ingredientRecipeDto = new IngredientRecipeDto();

        ingredientRecipeDto.setId(ingredientRecipeDao.getId());
        ingredientRecipeDto.setNameIngredient(ingredientRecipeDao.getNameIngredient());
        ingredientRecipeDto.setIdRecipe(ingredientRecipeDao.getIdRecipe());
        ingredientRecipeDto.setIdIngredient(ingredientRecipeDao.getIdIngredient());
        ingredientRecipeDto.setQuantity(ingredientRecipeDao.getQuantity());
        ingredientRecipeDto.setUnit(ingredientRecipeDao.getUnit());
        if (ingredientRecipeDto.getAllergens() != null) {
            List<AllergenDto> allergens = ingredientRecipeDao.getAllergens().stream()
                    .map(MappingUtils::mapAllergenDto)
                    .collect(Collectors.toList());
            ingredientRecipeDto.setAllergens(allergens);
        }
        return ingredientRecipeDto;
    }

    public static IngredientRecipeDao mapToDao(IngredientRecipeDto ingredientRecipeDto) {
        IngredientRecipeDao ingredientRecipeDao = new IngredientRecipeDao();

        ingredientRecipeDao.setId(ingredientRecipeDto.getId());
        ingredientRecipeDao.setIdRecipe(ingredientRecipeDto.getIdRecipe());
        ingredientRecipeDao.setIdIngredient(ingredientRecipeDto.getIdIngredient());
        ingredientRecipeDao.setQuantity(ingredientRecipeDto.getQuantity());
        ingredientRecipeDao.setUnit(ingredientRecipeDto.getUnit());

        return ingredientRecipeDao;
    }

    public static List<IngredientRecipeDto>mapToDto(List<IngredientRecipeDao> ingredientRecipeDaos){
        return ingredientRecipeDaos.stream()
                .map(MappingUtils::mapToDto)
                .collect(Collectors.toList());
    }

    public static List<IngredientRecipeDao>mapToDao(List<IngredientRecipeDto> ingredientRecipeDtos){
        return ingredientRecipeDtos.stream()
                .map(MappingUtils::mapToDao)
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

    public UserDto mapToDto(UserDao userDao) {

        UserDto userDto = new UserDto();

        userDto.setId(userDao.getId());
        userDto.setName(userDao.getName());
        userDto.setSurname1(userDao.getSurname1());
        userDto.setSurname2(userDao.getSurname2());
        userDto.setEmail(userDao.getEmail());
        userDto.setPassword(userDao.getPassword());

        return userDto;

    }

    public UserDao mapToDao(UserDto userDto) {

        UserDao userDao = new UserDao();

        userDao.setId(userDto.getId());
        userDao.setName(userDto.getName());
        userDao.setSurname1(userDto.getSurname1());
        userDao.setSurname2(userDto.getSurname2());
        userDao.setEmail(userDto.getEmail());
        userDao.setPassword(userDto.getPassword());
        userDao.setCreateTime(new Date(System.currentTimeMillis()));

        return userDao;

    }

}
