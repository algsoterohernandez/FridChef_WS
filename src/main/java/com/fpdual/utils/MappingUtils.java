package com.fpdual.utils;

import com.fpdual.api.dto.*;
import com.fpdual.persistence.aplication.dao.*;
import com.fpdual.api.dto.IngredientRecipeDto;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clase que proporciona métodos para mapear entre objetos DTO y DAO en la aplicación.
 */
public class MappingUtils {

    /**
     * Mapea un objeto RecipeDao a un objeto RecipeDto.
     *
     * @param recipeDao El objeto RecipeDao a mapear.
     * @return El objeto RecipeDto mapeado.
     */
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

    /**
     * Mapea un objeto RecipeDto a un objeto RecipeDao.
     *
     * @param recipeDto El objeto RecipeDto a mapear.
     * @return El objeto RecipeDao mapeado.
     */
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


    /**
     * Mapea una lista de objetos RecipeDao a una lista de objetos RecipeDto.
     *
     * @param recipeDao La lista de objetos RecipeDao a mapear.
     * @return La lista de objetos RecipeDto mapeada.
     */
    public static List<RecipeDto> mapRecipeListToDto(List<RecipeDao> recipeDao) {
        return recipeDao.stream()
                .map(MappingUtils::mapRecipeToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapea un objeto IngredientRecipeDao a un objeto IngredientRecipeDto.
     *
     * @param ingredientRecipeDao El objeto IngredientRecipeDao a mapear.
     * @return El objeto IngredientRecipeDto mapeado.
     */
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

    /**
     * Mapea un objeto IngredientRecipeDto a un objeto IngredientRecipeDao.
     *
     * @param ingredientRecipeDto El objeto IngredientRecipeDto a mapear.
     * @return El objeto IngredientRecipeDao mapeado.
     */
    public static IngredientRecipeDao mapIngredientRecipeToDao(IngredientRecipeDto ingredientRecipeDto) {
        IngredientRecipeDao ingredientRecipeDao = new IngredientRecipeDao();

        ingredientRecipeDao.setId(ingredientRecipeDto.getId());
        ingredientRecipeDao.setIdRecipe(ingredientRecipeDto.getIdRecipe());
        ingredientRecipeDao.setIdIngredient(ingredientRecipeDto.getIdIngredient());
        ingredientRecipeDao.setQuantity(ingredientRecipeDto.getQuantity());
        ingredientRecipeDao.setUnit(ingredientRecipeDto.getUnit());

        return ingredientRecipeDao;
    }

    /**
     * Mapea una lista de objetos IngredientRecipeDao a una lista de objetos IngredientRecipeDto.
     *
     * @param ingredientRecipeDaos La lista de objetos IngredientRecipeDao a mapear.
     * @return La lista de objetos IngredientRecipeDto mapeada.
     */
    public static List<IngredientRecipeDto> mapIngredientRecipeListToDto(List<IngredientRecipeDao> ingredientRecipeDaos) {
        return ingredientRecipeDaos.stream()
                .map(MappingUtils::mapIngredientRecipeToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapea una lista de objetos IngredientRecipeDto a una lista de objetos IngredientRecipeDao.
     *
     * @param ingredientRecipeDtos La lista de objetos IngredientRecipeDto a mapear.
     * @return La lista de objetos IngredientRecipeDao mapeada.
     */
    public static List<IngredientRecipeDao> mapIngredientRecipeListToDao(List<IngredientRecipeDto> ingredientRecipeDtos) {
        return ingredientRecipeDtos.stream()
                .map(MappingUtils::mapIngredientRecipeToDao)
                .collect(Collectors.toList());
    }

    /**
     * Mapea un objeto IngredientDao a un objeto IngredientDto.
     *
     * @param ingredientDao El objeto IngredientDao a mapear.
     * @return El objeto IngredientDto mapeado.
     */
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

    /**
     * Mapea una lista de objetos IngredientDao a una lista de objetos IngredientDto.
     *
     * @param ingredientDaos La lista de objetos IngredientDao a mapear.
     * @return La lista de objetos IngredientDto mapeada.
     */
    public static List<IngredientDto> mapIngredientListToDto(List<IngredientDao> ingredientDaos) {
        return ingredientDaos.stream()
                .map(MappingUtils::mapIngredientToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapea un objeto AllergenDao a un objeto AllergenDto.
     *
     * @param allergenDao El objeto AllergenDao a mapear.
     * @return El objeto AllergenDto mapeado.
     */
    public static AllergenDto mapAllergenToDto(AllergenDao allergenDao) {

        AllergenDto allergenDto = new AllergenDto();

        allergenDto.setId(allergenDao.getId());
        allergenDto.setName(allergenDao.getName());

        return allergenDto;
    }

    /**
     * Mapea una lista de objetos AllergenDao a una lista de objetos AllergenDto.
     *
     * @param allergenDao La lista de objetos AllergenDao a mapear.
     * @return La lista de objetos AllergenDto mapeada.
     */
    public static List<AllergenDto> mapAllergenListDto(List<AllergenDao> allergenDao) {
        return allergenDao.stream()
                .map(MappingUtils::mapAllergenToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapea un objeto UserDao a un objeto UserDto.
     *
     * @param userDao El objeto UserDao a mapear.
     * @return El objeto UserDto mapeado.
     */
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

    /**
     * Mapea un objeto UserDto a un objeto UserDao.
     *
     * @param userDto El objeto UserDto a mapear.
     * @return El objeto UserDao mapeado.
     */
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

    /**
     * Mapea un objeto RolUserDao a un objeto RolUserDto.
     *
     * @param rolUserDao El objeto RolUserDao a mapear.
     * @return El objeto RolUserDto mapeado.
     */
    public static RolUserDto mapRolUserToDto(RolUserDao rolUserDao) {

        RolUserDto rolUserDto = new RolUserDto();

        rolUserDto.setIdUser(rolUserDao.getIdUser());
        rolUserDto.setIdRol(rolUserDao.getIdRol());

        return rolUserDto;
    }

    /**
     * Mapea una lista de objetos RolUserDao a una lista de objetos RolUserDto.
     *
     * @param rolUserDao La lista de objetos RolUserDao a mapear.
     * @return La lista de objetos RolUserDto mapeada.
     */
    public static List<RolUserDto> mapRolUserListDto(List<RolUserDao> rolUserDao) {
        return rolUserDao.stream()
                .map(MappingUtils::mapRolUserToDto)
                .collect(Collectors.toList());
    }

    /**
     * Mapea un objeto ValorationDao a un objeto ValorationDto.
     *
     * @param valorationDao El objeto ValorationDao a mapear.
     * @return El objeto ValorationDto mapeado.
     */
    public static ValorationDto mapValorationToDto(ValorationDao valorationDao) {
        ValorationDto valorationDto = new ValorationDto();

        valorationDto.setId(valorationDao.getId());
        valorationDto.setIdRecipe(valorationDao.getIdRecipe());
        valorationDto.setIdUser(valorationDao.getIdUser());
        valorationDto.setNameUser(valorationDao.getNameUser());
        valorationDto.setComment(valorationDao.getComment());
        valorationDto.setValoration(valorationDao.getValoration());
        valorationDto.setCreateTime(valorationDao.getCreateTime());

        return valorationDto;

    }

    /**
     * Mapea un objeto ValorationDto a un objeto ValorationDao.
     *
     * @param valorationDto El objeto ValorationDto a mapear.
     * @return El objeto ValorationDao mapeado.
     */
    public static ValorationDao mapValorationToDao(ValorationDto valorationDto) {
        ValorationDao valorationDao = new ValorationDao();

        valorationDao.setId(valorationDto.getId());
        valorationDao.setIdRecipe(valorationDto.getIdRecipe());
        valorationDao.setIdUser(valorationDto.getIdUser());
        valorationDao.setNameUser(valorationDto.getNameUser());
        valorationDao.setComment(valorationDto.getComment());
        valorationDao.setValoration(valorationDto.getValoration());
        valorationDao.setCreateTime(new Date(System.currentTimeMillis()));
        return valorationDao;
    }
    public static List<ValorationDto> mapValorationListToDto(List<ValorationDao> valorationDaoList) {
        return valorationDaoList.stream()
                .map(MappingUtils::mapValorationToDto)
                .collect(Collectors.toList());
    }
    public static List<ValorationDao> mapValorationDtoListToDao(List<ValorationDto> valorationDtoList) {
        return valorationDtoList.stream()
                .map(MappingUtils::mapValorationToDao)
                .collect(Collectors.toList());
    }
}