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
public class CategoryService {

    private final Connection connector;
    private final CategoryManager categoryManager;
    private final RecipeManager recipeManager;

    public CategoryService(Connection connector) {
        this.connector = connector;
        this.categoryManager = new CategoryManager();
        this.recipeManager = new RecipeManager();
    }

    public CategoryDto findCategoryById(int id) {
        try{
            return categoryManager.findCategoryById(connector, id);
        }catch(Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public CategoryDto createCategory(CategoryDto categoryDto) {
        try{
            return categoryManager.createCategory(connector, categoryDto);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return categoryDto;
    }

    public CategoryDto updateCategory(int id, CategoryDto categoryDto) {
        try{
            return categoryManager.updateCategory(connector, id, categoryDto);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public boolean deleteCategory(int id) {
        try{
            return categoryManager.deleteCategory(connector, id);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public List<CategoryDto> findAllCategories() {
        try{
            return categoryManager.findAllCategories(connector);
        }catch (Exception e){
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public List<RecipeDto> findRecipesByCategory(CategoryDto categoryDto, int limit) {
        List<RecipeDto> filteredRecipes = new ArrayList<>();

        int idCategory = categoryDto.getId();
        List<RecipeDao> recipes = recipeManager.findBy(connector, new ArrayList<>(), categoryDto.getId(), false, limit);
        for(RecipeDao recipeDao : recipes){
            RecipeDto recipeDto = MappingUtils.mapRecipeToDto(recipeDao);
            filteredRecipes.add(recipeDto);
        }

        return filteredRecipes;
    }
}




