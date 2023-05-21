package com.fpdual.utils;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.api.dto.IngredientDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.api.dto.UserDto;
import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.dao.UserDao;

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
