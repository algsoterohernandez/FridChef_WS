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

/**
 * Servicio para gestionar recetas.
 */
public class RecipeService {

    private final MySQLConnector connector;
    private final RecipeManager recipeManager;
    private final IngredientManager ingredientManager;

    /**
     * Constructor de la clase RecipeService.
     *
     * @param connector         Conector MySQL para acceder a la base de datos.
     * @param recipeManager     Gestor de recetas.
     * @param ingredientManager Gestor de ingredientes.
     */
    public RecipeService(MySQLConnector connector, RecipeManager recipeManager, IngredientManager ingredientManager) {
        this.recipeManager = recipeManager;
        this.ingredientManager = ingredientManager;
        this.connector = connector;
    }

    /**
     * Obtiene todas las recetas.
     *
     * @return Lista de RecipeDto que contiene todas las recetas encontradas.
     */
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

    /**
     * Recupera una lista de objetos RecipeDto que coinciden con los criterios de búsqueda especificados.
     *
     * @param idsRecipe      Una lista de IDs de recetas específicas a recuperar. Puede ser nula o vacía para no aplicar este filtro.
     * @param idCategory     El ID de la categoría de receta para filtrar. Un valor de 0 indica que no se aplica este filtro.
     * @param orderByPopular Indica si los resultados deben ser ordenados por popularidad.
     * @param limit          El número máximo de recetas a recuperar.
     * @param onlyAccepted   Indica si solo se deben recuperar recetas aceptadas.
     * @return Una lista de objetos RecipeDto que coinciden con los criterios de búsqueda especificados. Puede ser nula si no se encontraron coincidencias.
     */
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

    /**
     * Busca recetas por ingredientes.
     *
     * @param recipeIngredients Lista de nombres de ingredientes.
     * @return Lista de RecipeDto que contiene las recetas encontradas.
     */
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

    /**
     * Obtiene sugerencias de recetas según los ingredientes proporcionados.
     *
     * @param recipeIngredients Lista de nombres de ingredientes.
     * @return Lista de RecipeDto que contiene las recetas sugeridas.
     */
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

    /**
     * Crea una nueva receta.
     *
     * @param recipeDto Objeto RecipeDto que contiene los datos de la receta.
     * @return RecipeDto que representa la receta creada.
     * @throws SQLException            Si ocurre un error al acceder a la base de datos.
     * @throws ClassNotFoundException Si no se encuentra la clase especificada.
     */
    public RecipeDto createRecipe(RecipeDto recipeDto) throws SQLException, ClassNotFoundException {

        try (Connection con = connector.getMySQLConnection()) {

            RecipeDao recipeDao = this.recipeManager.createRecipe(con, MappingUtils.mapRecipeToDao(recipeDto));
            recipeDto = MappingUtils.mapRecipeToDto(recipeDao);

        } catch (Exception e) {
            throw e;
        }

        return recipeDto;
    }

    /**
     * Obtiene las recetas pendientes de aprobación.
     *
     * @return Lista de RecipeDto que contiene las recetas pendientes de aprobación.
     * @throws SQLException            Si ocurre un error al acceder a la base de datos.
     * @throws ClassNotFoundException Si no se encuentra la clase especificada.
     */
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

    /**
     * Actualiza el estado de una receta.
     *
     * @param id     ID de la receta a actualizar.
     * @param status Nuevo estado de la receta.
     * @return RecipeDto que representa la receta actualizada.
     * @throws SQLException            Si ocurre un error al acceder a la base de datos.
     * @throws ClassNotFoundException Si no se encuentra la clase especificada.
     */
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