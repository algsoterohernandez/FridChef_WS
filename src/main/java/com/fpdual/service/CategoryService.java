package com.fpdual.service;

import com.fpdual.api.dto.*;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.CategoryManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.utils.MappingUtils;
import lombok.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
/**
 * Servicio para gestionar las categorías de recetas.
 */
public class CategoryService {

    private final Connection connector;
    private final CategoryManager categoryManager;
    private final RecipeManager recipeManager;

    /**
     * Constructor de la clase.
     *
     * @param connector     Conector de base de datos.
     */
    public CategoryService(Connection connector) {
        this.connector = connector;
        this.categoryManager = new CategoryManager();
        this.recipeManager = new RecipeManager();
    }

    /**
     * Busca una categoría por su ID.
     *
     * @param id ID de la categoría.
     * @return Objeto CategoryDto que representa la categoría encontrada.
     */
    public CategoryDto findCategoryById(int id) {
        try{
            return categoryManager.findCategoryById(connector, id);
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Crea una nueva categoría.
     *
     * @param categoryDto Objeto CategoryDto que contiene los datos de la categoría a crear.
     * @return Objeto CategoryDto que representa la categoría creada.
     */
    public CategoryDto createCategory(CategoryDto categoryDto) {
        try{
            return categoryManager.createCategory(connector, categoryDto);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return categoryDto;
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param id          ID de la categoría a actualizar.
     * @param categoryDto Objeto CategoryDto que contiene los nuevos datos de la categoría.
     * @return Objeto CategoryDto que representa la categoría actualizada.
     */
    public CategoryDto updateCategory(int id, CategoryDto categoryDto) {
        try{
            return categoryManager.updateCategory(connector, id, categoryDto);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Elimina una categoría existente.
     *
     * @param id ID de la categoría a eliminar.
     * @return true si la categoría se eliminó correctamente, false en caso contrario.
     */
    public boolean deleteCategory(int id) {
        try{
            return categoryManager.deleteCategory(connector, id);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene todas las categorías existentes.
     *
     * @return Lista de objetos CategoryDto que representan todas las categorías encontradas.
     */
    public List<CategoryDto> findAllCategories() {
        try{
            return categoryManager.findAllCategories(connector);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    /**
     * Busca las recetas asociadas a una categoría.
     *
     * @param categoryDto Objeto CategoryDto que representa la categoría.
     * @return Lista de objetos RecipeDto que representan las recetas asociadas a la categoría.
     */
    public List<RecipeDto> findRecipesByCategory(CategoryDto categoryDto) {
        List<RecipeDto> filteredRecipes = new ArrayList<>();

        int idCategory = categoryDto.getId();
        List<RecipeDao> recipes = recipeManager.findAllRecipesByCategoryId(connector, categoryDto.getId());
        for(RecipeDao recipeDao : recipes){
            RecipeDto recipeDto = MappingUtils.mapRecipeToDto(recipeDao);
            filteredRecipes.add(recipeDto);
        }

        return filteredRecipes;
    }

    private RecipeDto mapRecipeDaoToDto(RecipeDao recipeDao){
        RecipeDto recipeDto = new RecipeDto();

        recipeDto.setId(recipeDao.getId());
        recipeDto.setName(recipeDao.getName());
        recipeDto.setDescription(recipeDao.getDescription());
        recipeDto.setTime(recipeDao.getTime());
        recipeDto.setUnitTime(recipeDao.getUnitTime());
        //recipeDto.setImage(recipeDao.getImage());
        recipeDto.setStatus(recipeDao.getStatus());
        // TODO: cuando se verifique el ingredientRecipeDao, se podrá continuar insertando los ingredientes en la receta
        // recipeDto.setIngredients(mapIngredientRecipeDaoListToDtoList(recipeDao.getIngredients(), recipeDao));

        return recipeDto;
    }

    private List<IngredientRecipeDto> mapIngredientRecipeDaoListToDtoList(List<IngredientDao> ingredients, RecipeDao recipes) {
        List<IngredientRecipeDto> ingredientRecipeDtoList = new ArrayList<>();
        IngredientRecipeDao ingredientRecipeDao = null;

        for (IngredientDao ingredientDao : ingredients) {
            IngredientRecipeDto ingredientRecipeDto = new IngredientRecipeDto();
            ingredientRecipeDto.setId(ingredientDao.getId());
            ingredientRecipeDto.setIdRecipe(recipes.getId());
            ingredientRecipeDto.setIdIngredient(ingredientDao.getId());
            ingredientRecipeDto.setQuantity(ingredientRecipeDao.getQuantity());
            ingredientRecipeDto.setUnit(ingredientRecipeDao.getUnit());

            
            ingredientRecipeDtoList.add(ingredientRecipeDto);
        }

        return ingredientRecipeDtoList;
    }


}