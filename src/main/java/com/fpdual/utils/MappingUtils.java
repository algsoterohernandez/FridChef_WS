package com.fpdual.utils;

import com.fpdual.api.dto.*;
import com.fpdual.persistence.aplication.dao.*;
import com.fpdual.api.dto.IngredientRecipeDto;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;

import javax.sql.rowset.serial.SerialBlob;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class MappingUtils {

    public static RecipeDto mapRecipeToDto(RecipeDao recipeDao) {
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
        recipeDto.setIngredients(mapIngredientRecipeListToDto(recipeDao.getIngredients()));
        recipeDto.setValoration(recipeDao.getValoration());


        Blob imageBlob = recipeDao.getImage();
        if (imageBlob != null) {
            try {
                byte[] imageContent = imageBlob.getBytes(1, (int) imageBlob.length());
                String imageBase64 = Base64.getEncoder().encodeToString(imageContent);

                recipeDto.setImageBase64(imageBase64);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }



        List<IngredientRecipeDto> ingredients = recipeDao.getIngredients().stream()
                .map(MappingUtils::mapIngredientRecipeToDto)
                .collect(Collectors.toList());

        recipeDto.setIngredients(ingredients);

        return recipeDto;
    }
    public static RecipeDao mapRecipeToDao(RecipeDto recipeDto) {

        RecipeDao recipeDao = new RecipeDao();

        recipeDao.setId(recipeDto.getId());
        recipeDao.setName(recipeDto.getName());
        recipeDao.setDescription(recipeDto.getDescription());
        recipeDao.setDifficulty(recipeDto.getDifficulty());
        recipeDao.setTime(recipeDto.getTime());
        recipeDao.setUnitTime(recipeDto.getUnitTime());
        recipeDao.setIdCategory(recipeDto.getIdCategory());
        recipeDao.setCreateTime(new Date(System.currentTimeMillis()));
        recipeDao.setValoration(recipeDto.getValoration());

        if (recipeDto.getImageBase64() != null) {
            byte[] imageContent = Base64.getDecoder().decode(recipeDto.getImageBase64());
            try {
                recipeDao.setImage(new SerialBlob(imageContent));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        recipeDao.setStatus(recipeDto.getStatus());
        recipeDao.setIngredients(mapIngredientRecipeListToDao(recipeDto.getIngredients()));

        return recipeDao;

    }

    public static List<RecipeDto> mapRecipeListToDto(List<RecipeDao> recipeDao) {
        return recipeDao.stream()
                .map(MappingUtils::mapRecipeToDto)
                .collect(Collectors.toList());
    }

    public static IngredientRecipeDto mapIngredientRecipeToDto(IngredientRecipeDao ingredientRecipeDao) {
        IngredientRecipeDto ingredientRecipeDto = new IngredientRecipeDto();

        ingredientRecipeDto.setId(ingredientRecipeDao.getId());
        ingredientRecipeDto.setNameIngredient(ingredientRecipeDao.getNameIngredient());
        ingredientRecipeDto.setIdRecipe(ingredientRecipeDao.getIdRecipe());
        ingredientRecipeDto.setIdIngredient(ingredientRecipeDao.getIdIngredient());
        ingredientRecipeDto.setQuantity(ingredientRecipeDao.getQuantity());
        ingredientRecipeDto.setUnit(ingredientRecipeDao.getUnit());
        if (ingredientRecipeDao.getAllergens() != null) {
            List<AllergenDto> allergens = ingredientRecipeDao.getAllergens().stream()
                    .map(MappingUtils::mapAllergenToDto)
                    .collect(Collectors.toList());
            ingredientRecipeDto.setAllergens(allergens);
        }
        return ingredientRecipeDto;
    }

    public static IngredientRecipeDao mapIngredientRecipeToDao(IngredientRecipeDto ingredientRecipeDto) {
        IngredientRecipeDao ingredientRecipeDao = new IngredientRecipeDao();

        ingredientRecipeDao.setId(ingredientRecipeDto.getId());
        ingredientRecipeDao.setIdRecipe(ingredientRecipeDto.getIdRecipe());
        ingredientRecipeDao.setIdIngredient(ingredientRecipeDto.getIdIngredient());
        ingredientRecipeDao.setQuantity(ingredientRecipeDto.getQuantity());
        ingredientRecipeDao.setUnit(ingredientRecipeDto.getUnit());


        return ingredientRecipeDao;
    }

    public static List<IngredientRecipeDto> mapIngredientRecipeListToDto(List<IngredientRecipeDao> ingredientRecipeDaos){
        return ingredientRecipeDaos.stream()
                .map(MappingUtils::mapIngredientRecipeToDto)
                .collect(Collectors.toList());
    }

    public static List<IngredientRecipeDao> mapIngredientRecipeListToDao(List<IngredientRecipeDto> ingredientRecipeDtos){
        return ingredientRecipeDtos.stream()
                .map(MappingUtils::mapIngredientRecipeToDao)
                .collect(Collectors.toList());
    }


    public static IngredientDto mapIngredientToDto(IngredientDao ingredientDao) {
        IngredientDto ingredientDto = new IngredientDto();

        ingredientDto.setId(ingredientDao.getId());
        ingredientDto.setName(ingredientDao.getName());

        if (ingredientDao.getAllergens() != null) {
            List<AllergenDto> allergens = ingredientDao.getAllergens().stream()
                    .map(MappingUtils::mapAllergenToDto)
                    .collect(Collectors.toList());
            ingredientDto.setAllergens(allergens);
        }

        return ingredientDto;
    }

    public static List<IngredientDto> mapIngredientListToDto(List<IngredientDao> ingredientDaos) {
        return ingredientDaos.stream()
                .map(MappingUtils::mapIngredientToDto)
                .collect(Collectors.toList());
    }


    public static AllergenDto mapAllergenToDto(AllergenDao allergenDao) {

        AllergenDto allergenDto = new AllergenDto();

        allergenDto.setId(allergenDao.getId());
        allergenDto.setName(allergenDao.getName());

        return allergenDto;
    }

    public static List<AllergenDto> mapAllergenListDto(List<AllergenDao> allergenDao) {
        return allergenDao.stream()
                .map(MappingUtils::mapAllergenToDto)
                .collect(Collectors.toList());
    }

    public static UserDto mapUserToDto(UserDao userDao) {

        UserDto userDto = new UserDto();

        userDto.setId(userDao.getId());
        userDto.setName(userDao.getName());
        userDto.setSurname1(userDao.getSurname1());
        userDto.setSurname2(userDao.getSurname2());
        userDto.setEmail(userDao.getEmail());
        userDto.setPassword(userDao.getPassword());

        return userDto;

    }

    public static UserDao mapUserToDao(UserDto userDto) {

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

    public static RolUserDto mapRolUserToDto(RolUserDao rolUserDao) {

        RolUserDto rolUserDto = new RolUserDto();

        rolUserDto.setIdUser(rolUserDao.getIdUser());
        rolUserDto.setIdRol(rolUserDao.getIdRol());

        return rolUserDto;
    }
    public static List<RolUserDto> mapRolUserListDto(List<RolUserDao> rolUserDao) {
        return rolUserDao.stream()
                .map(MappingUtils::mapRolUserToDto)
                .collect(Collectors.toList());
    }
    public static ValorationDto mapValorationToDto(ValorationDao valorationDao){
        ValorationDto valorationDto = new ValorationDto();

        valorationDto.setId(valorationDao.getId());
        valorationDto.setIdRecipe(valorationDao.getIdRecipe());
        valorationDto.setIdUser(valorationDao.getIdUser());
        valorationDto.setComment(valorationDao.getComment());
        valorationDto.setValoration(valorationDao.getValoration());

        return valorationDto;

    }
    public static ValorationDao mapValorationToDao(ValorationDto valorationDto){
        ValorationDao valorationDao = new ValorationDao();

        valorationDao.setId(valorationDto.getId());
        valorationDao.setIdRecipe(valorationDto.getIdRecipe());
        valorationDao.setIdUser(valorationDto.getIdUser());
        valorationDao.setComment(valorationDto.getComment());
        valorationDao.setValoration(valorationDto.getValoration());

        return valorationDao;
    }
}