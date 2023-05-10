package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredientManager {

    //buscar todo

    public List<IngredientDao> findAll() {
        try (Connection con = new MySQLConnector().getMySQLConnection(); Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from ingredient order by name ASC");

            result.beforeFirst();

            List<IngredientDao> ingredients = new ArrayList<>();

            while (result.next()) {
                ingredients.add(new IngredientDao(result));
            }

            return ingredients;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
    public List<IngredientDao> findByIngredients() throws SQLException, ClassNotFoundException {
        Connection con = new MySQLConnector().getMySQLConnection();

        try (Statement stm = con.createStatement()) {

            ResultSet result = stm.executeQuery("select * from ingredient order by name ASC");

            result.beforeFirst();

            List<IngredientDao> ingredients = new ArrayList<>();

            while (result.next()) {
                ingredients.add(new IngredientDao(result));
            }

            return ingredients;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            con.close();
        }
    }

    public Integer getIngredientIdByName(String ingredientName) {
        try (Connection con = new MySQLConnector().getMySQLConnection(); Statement stm = con.createStatement()) {

            ResultSet result = stm.executeQuery("select id from ingredient where name = '" + ingredientName  + "'");

            if (result.next()) {
                return result.getInt("id");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}

