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

public class FavoriteService {
    private final MySQLConnector connector;
    private final FavoriteManager favoriteManager;

    public FavoriteService(MySQLConnector connector, FavoriteManager favoriteManager) {
        this.connector = connector;
        this.favoriteManager = favoriteManager;
    }

    public boolean favoriteAdd(int idRecipe, int idUser)throws SQLException, FridChefException {
        boolean favoriteAdd = false;

        try (Connection con = connector.getMySQLConnection()) {

            favoriteAdd = this.favoriteManager.favoriteAdded(con, idRecipe, idUser);

            favoriteAdd = true;

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return favoriteAdd;
    }

    public boolean favoriteRemove(int idRecipe, int idUser)throws SQLException, FridChefException {
        boolean favoriteRemoved = false;

        try (Connection con = connector.getMySQLConnection()) {

            favoriteRemoved = this.favoriteManager.favoriteRemoved(con, idRecipe, idUser);

            favoriteRemoved = true;

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return favoriteRemoved;
    }



}
