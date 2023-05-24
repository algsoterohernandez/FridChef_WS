package com.fpdual.utils;

import com.fpdual.api.dto.*;
import com.fpdual.persistence.aplication.dao.*;

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

    public static UserDto mapUserDto(UserDao userDao) {

        UserDto userDto = new UserDto();

        userDto.setId(userDao.getId());
        userDto.setName(userDao.getName());
        userDto.setSurname1(userDao.getSurname1());
        userDto.setSurname2(userDao.getSurname2());
        userDto.setEmail(userDao.getEmail());
        userDto.setPassword(userDao.getPassword());

        return userDto;

    }

    public static UserDao mapUserDao(UserDto userDto) {

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

    public static RolUserDto mapRolUserDto(RolUserDao rolUserDao) {

        RolUserDto rolUserDto = new RolUserDto();

        rolUserDto.setIdUser(rolUserDao.getIdUser());
        rolUserDto.setIdRol(rolUserDao.getIdRol());

        return rolUserDto;
    }
    public static List<RolUserDto> mapRolUserDto(List<RolUserDao> rolUserDao) {
        return rolUserDao.stream()
                .map(MappingUtils::mapRolUserDto)
                .collect(Collectors.toList());
    }

}