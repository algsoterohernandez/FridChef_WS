package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import lombok.Data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeManager {

    public List<RecipeDao> findAll() {
        try (Connection con = new MySQLConnector().getMySQLConnection(); Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from recipe order by name ASC");

            result.beforeFirst();

            List<RecipeDao> recipes = new ArrayList<>();

            while (result.next()) {
                recipes.add(new RecipeDao(result));
            }

            return recipes;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
