package com.fpdual.service;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.exceptions.FridChefException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.FavoriteManager;
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
    private final FavoriteManager favoriteManager;

    /**
     * Constructor de FavoriteService.
     *
     * @param connector    Conector a la base de datos MySQL.
     * @param userManager Manager de usuarios.
     */
    public FavoriteService(MySQLConnector connector, UserManager userManager) {
    public FavoriteService(MySQLConnector connector, FavoriteManager favoriteManager) {
        this.connector = connector;
        this.favoriteManager = favoriteManager;
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
    public boolean favoriteAdd(int idRecipe, int idUser)throws SQLException, FridChefException {
        boolean favoriteAdd = false;

        try (Connection con = connector.getMySQLConnection()) {

            favoriteAdd = this.favoriteManager.favoriteAdded(con, idRecipe, idUser);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return favoriteAdd;
    }

    public boolean favoriteRemove(int idRecipe, int idUser)throws SQLException, FridChefException {
        boolean favoriteRemoved = false;

        try (Connection con = connector.getMySQLConnection()) {

            favoriteRemoved = this.favoriteManager.favoriteRemoved(con, idRecipe, idUser);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return favoriteRemoved;
    }



}
