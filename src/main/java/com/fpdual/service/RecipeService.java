package com.fpdual.service;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
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

    public RecipeService(MySQLConnector connector, RecipeManager recipeManager, IngredientManager ingredientManager) {
        this.recipeManager = recipeManager;
        this.ingredientManager = ingredientManager;
        this.connector = connector;
    }

    public List<RecipeDto> findAll() {
        List<RecipeDto> recipeDtos = null;
        try (Connection con = connector.getMySQLConnection()) {

            List<RecipeDao> recipeDaos = recipeManager.findAll(con);

            if (recipeDaos != null) {
                recipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);
            }


        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recipeDtos;
    }

    public List<RecipeDto> findBy(List<String> idsRecipe, int idCategory, boolean orderByPopular, int limit, boolean onlyAccepted) {
        List<RecipeDto> recipeDtos = null;
        try (Connection con = connector.getMySQLConnection()) {

            List<RecipeDao> recipeDaos = recipeManager.findBy(con, idsRecipe, idCategory, orderByPopular, limit, onlyAccepted);

            if (recipeDaos != null) {
                recipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);
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
                recipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);
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
                recipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recipeDtos;
    }

    public RecipeDto createRecipe(RecipeDto recipeDto) throws SQLException, ClassNotFoundException {

        try (Connection con = connector.getMySQLConnection()) {

            RecipeDao recipeDao = this.recipeManager.createRecipe(con, MappingUtils.mapRecipeToDao(recipeDto));
            recipeDto = MappingUtils.mapRecipeToDto(recipeDao);

        } catch (Exception e) {
            throw e;
        }

        return recipeDto;
    }

    public List<RecipeDto> findByStatusPending() throws SQLException, ClassNotFoundException {
        List<RecipeDto> recipeDtoList = new ArrayList<>();

        try (Connection con = connector.getMySQLConnection()) {
            List<RecipeDao> recipeDaoList = recipeManager.findByStatusPending(con);

            if (!recipeDaoList.isEmpty()) {

                recipeDtoList = MappingUtils.mapRecipeListToDto(recipeDaoList);
            }

        } catch (Exception e) {

            throw e;

        }

        return recipeDtoList;
    }

    public RecipeDto updateRecipeStatus(int id, String status) throws SQLException, ClassNotFoundException {
        RecipeDto recipeDto = new RecipeDto();

        try (Connection con = connector.getMySQLConnection()) {

            RecipeDao recipeDao = recipeManager.updateRecipeStatus(con, id, status);

            if (recipeDao != null) {
                if (recipeDao.getStatus().equals(RecipeStatus.ACCEPTED)) {
                    recipeDto = MappingUtils.mapRecipeToDto(recipeDao);

                } else if (recipeDao.getStatus().equals(RecipeStatus.DECLINED)) {
                    recipeDto = MappingUtils.mapRecipeToDto(recipeDao);

                }
            }

        } catch (Exception e) {
            throw e;

        }

        return recipeDto;
    }

}