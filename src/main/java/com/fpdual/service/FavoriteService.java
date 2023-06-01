package com.fpdual.service;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.UserManager;
import com.fpdual.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para gestionar las recetas favoritas de los usuarios.
 */
public class FavoriteService {
    private final MySQLConnector connector;
    private final UserManager userManager;

    /**
     * Constructor de FavoriteService.
     *
     * @param connector    Conector a la base de datos MySQL.
     * @param userManager Manager de usuarios.
     */
    public FavoriteService(MySQLConnector connector, UserManager userManager) {
        this.connector = connector;
        this.userManager = userManager;
    }

    /**
     * Busca las recetas favoritas de un usuario.
     *
     * @param idUser ID del usuario.
     * @return Lista de RecipeDto que representan las recetas favoritas del usuario.
     * @throws SQLException             Si ocurre un error de SQL al buscar las recetas favoritas.
     * @throws ClassNotFoundException Si no se encuentra la clase para el conector de MySQL.
     */
    public List<RecipeDto> findRecipeFavorite(int idUser) throws SQLException, ClassNotFoundException {
        List<RecipeDto> recipeFavoriteListDto = new ArrayList<>();

        try (Connection con = connector.getMySQLConnection()) {
            List<RecipeDao> recipeFavoriteListDao = userManager.findFavorite(con, idUser);

            for (RecipeDao recipeDao : recipeFavoriteListDao) {
                RecipeDto recipeDto = MappingUtils.mapRecipeToDto(recipeDao);
                recipeFavoriteListDto.add(recipeDto);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return recipeFavoriteListDto;
    }
}