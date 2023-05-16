package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class IngredientManager {

    private final AllergenManager allergenManager;

    public IngredientManager() {
        allergenManager = new AllergenManager();
    }

    //buscar todo

    public List<IngredientDao> findAll() {
        try (Connection con = new MySQLConnector().getMySQLConnection(); Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from ingredient order by name ASC");

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

    public List<IngredientDao> findRecipeIngredients(int recipeId)
    {
        try (Connection con = new MySQLConnector().getMySQLConnection(); Statement stm = con.createStatement()) {

            ResultSet result = stm.executeQuery("select i.* from ingredient i inner join ingredient_recipe ir on ir.id_ingredient = i.id where ir.id_recipe = " + recipeId);

            List<IngredientDao> ingredients = new ArrayList<>();

            while (result.next()) {
                IngredientDao ingredientDao = new IngredientDao(result);
                FillIngredientAllergens(ingredientDao);

                ingredients.add(ingredientDao);
            }

            return ingredients;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void FillIngredientAllergens(IngredientDao ingredientDao)
    {
        ingredientDao.setAllergens(allergenManager.findIngredientAllergens(ingredientDao.getId()));
    }
}

