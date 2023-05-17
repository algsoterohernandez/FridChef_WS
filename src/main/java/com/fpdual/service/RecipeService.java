package com.fpdual.service;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.utils.MappingUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipeService {

    private final RecipeManager recipeManager;
    private final IngredientManager ingredientManager;

    public RecipeService() {
        recipeManager = new RecipeManager();
        ingredientManager = new IngredientManager();
    }

    public List<RecipeDto> findAll() {
        List<RecipeDao> recipeDaos = recipeManager.findAll();

        List<RecipeDto> recipeDtos = null;

        if (recipeDaos != null) {
            recipeDtos = MappingUtils.mapRecipeDto(recipeDaos);
        }

        return recipeDtos;
    }

    public List<RecipeDto> findRecipesByIngredients(List<String> recipeIngredients) {
        List<Integer> ingredientIds = new ArrayList<>();

        for (String ingredientName: recipeIngredients) {
            Integer ingredientId = ingredientManager.getIngredientIdByName(ingredientName);

            if (ingredientId == null) {
                return null;
            }

            ingredientIds.add(ingredientId);
        }

        List<RecipeDto> recipeDtos = null;

        List<RecipeDao> recipeDaos = recipeManager.findRecipesByIngredients(ingredientIds);

        if (recipeDaos != null) {
            recipeDtos = MappingUtils.mapRecipeDto(recipeDaos);
        }

        return recipeDtos;
    }

    public List<RecipeDto> findRecipeSuggestions(List<String> recipeIngredients) {
        List<Integer> ingredientIds = new ArrayList<>();

        for (String ingredientName: recipeIngredients) {
            Integer ingredientId = ingredientManager.getIngredientIdByName(ingredientName);

            if (ingredientId == null) {
                return null;
            }

            ingredientIds.add(ingredientId);
        }

        List<RecipeDto> recipeDtos = null;

        List<RecipeDao> recipeDaos = recipeManager.findRecipeSuggestions(ingredientIds);

        if (recipeDaos != null) {
            recipeDtos = MappingUtils.mapRecipeDto(recipeDaos);
        }

        return recipeDtos;
    }
}
