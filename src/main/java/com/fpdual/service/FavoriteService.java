package com.fpdual.service;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.exceptions.FridChefException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.FavoriteManager;

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
     * @param connector       Conector a la base de datos MySQL.
     * @param favoriteManager Recetas favoritas.
     */
    public FavoriteService(MySQLConnector connector, FavoriteManager favoriteManager) {
        this.connector = connector;
        this.favoriteManager = favoriteManager;
    }

    /**
     * Busca las recetas favoritas de un usuario.
     *
     * @param idUser ID del usuario.
     * @return Lista de RecipeDto que representan las recetas favoritas del usuario.
     * @throws SQLException           Si ocurre un error de SQL al buscar las recetas favoritas.
     * @throws ClassNotFoundException Si no se encuentra la clase para el conector de MySQL.
     */
    public List<RecipeDto> findRecipeFavorite(int idUser) {
        List<RecipeDto> recipeFavoriteListDto = new ArrayList<>();
        return recipeFavoriteListDto;
    }
    /**
     * Agrega una receta a la lista de favoritos para un usuario específico.
     *
     * @param idRecipe El ID de la receta a agregar a favoritos.
     * @param idUser   El ID del usuario al que se le agregará la receta a favoritos.
     * @return true si la receta se agregó exitosamente a la lista de favoritos, false en caso contrario.
     */
    public boolean favoriteAdd(int idRecipe, int idUser){
        boolean favoriteAdd = false;

        try (Connection con = connector.getMySQLConnection()) {

            favoriteAdd = this.favoriteManager.favoriteAdded(con, idRecipe, idUser);
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return favoriteAdd;
    }

    /**
     * Elimina una receta de la lista de favoritos para un usuario específico.
     *
     * @param idRecipe El ID de la receta a eliminar de favoritos.
     * @param idUser   El ID del usuario al que se le eliminará la receta de favoritos.
     * @return true si la receta se eliminó exitosamente de la lista de favoritos, false en caso contrario.
     */
    public boolean favoriteRemove(int idRecipe, int idUser){
        boolean favoriteRemoved = false;

        try (Connection con = connector.getMySQLConnection()) {

            favoriteRemoved = this.favoriteManager.favoriteRemoved(con, idRecipe, idUser);

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return favoriteRemoved;
    }
}
