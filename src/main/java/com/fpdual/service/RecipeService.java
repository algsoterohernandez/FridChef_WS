package com.fpdual.service;

import com.fpdual.api.dto.IngredientRecipeDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.utils.MappingUtils;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecipeService {

    private final MySQLConnector connector;
    private final RecipeManager recipeManager;
    private final IngredientManager ingredientManager;
    private final MappingUtils mapper;

    public RecipeService(MySQLConnector connector, RecipeManager recipeManager, IngredientManager ingredientManager) {
        this.recipeManager = recipeManager;
        this.ingredientManager = ingredientManager;
        this.connector = connector;
        this.mapper = new MappingUtils();
    }

    public List<RecipeDto> findAll() {
        List<RecipeDto> recipeDtos = null;
        try (Connection con = connector.getMySQLConnection()) {

            List<RecipeDao> recipeDaos = recipeManager.findAll(con);

            if (recipeDaos != null) {
                recipeDtos = MappingUtils.mapRecipeDto(recipeDaos);
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recipeDtos;
    }

    public List<RecipeDto> findRecipesByIngredients(List<String> recipeIngredients) {
        List<RecipeDto> recipeDtos = new ArrayList<>();

        try (Connection con = connector.getMySQLConnection()) {
            List<Integer> ingredientIds = new ArrayList<>();

            for (String ingredientName : recipeIngredients) {
                Integer ingredientId = ingredientManager.getIngredientIdByName(con, ingredientName);

                if (ingredientId == null) {
                    return recipeDtos;
                }

                ingredientIds.add(ingredientId);
            }

            List<RecipeDao> recipeDaos = recipeManager.findRecipesByIngredients(con, ingredientIds);

            if (recipeDaos != null) {
                recipeDtos = MappingUtils.mapRecipeDto(recipeDaos);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return recipeDtos;
    }

    public List<RecipeDto> findRecipeSuggestions(List<String> recipeIngredients) {
        List<RecipeDto> recipeDtos = new ArrayList<>();

        try (Connection con = connector.getMySQLConnection()) {
            List<Integer> ingredientIds = new ArrayList<>();
            for (String ingredientName : recipeIngredients) {
                Integer ingredientId = ingredientManager.getIngredientIdByName(con, ingredientName);

                if (ingredientId == null) {
                    return recipeDtos;
                }

                ingredientIds.add(ingredientId);
            }

            List<RecipeDao> recipeDaos = recipeManager.findRecipeSuggestions(con, ingredientIds);

            if (recipeDaos != null) {
                recipeDtos = MappingUtils.mapRecipeDto(recipeDaos);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recipeDtos;
    }

    public RecipeDto createRecipe(RecipeDto recipeDto) throws SQLException, ClassNotFoundException {

        try (Connection con = connector.getMySQLConnection()) {

            RecipeDao recipeDao = this.recipeManager.createRecipe(con, mapper.mapToDao(recipeDto));
            recipeDto = MappingUtils.mapRecipeDto(recipeDao);

        } catch (Exception e) {
            throw e;
        }

        return recipeDto;
    }



}