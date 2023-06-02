package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientManager {

    private final AllergenManager allergenManager;

    public IngredientManager() {
        allergenManager = new AllergenManager();
    }


    public IngredientDao insertIngredient(Connection con, String name){
        IngredientDao ingredient = new IngredientDao();
        ingredient.setName(name);
        try (PreparedStatement stm = con.prepareStatement("INSERT INTO ingredient (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, name);

            stm.executeUpdate();
            ResultSet result = stm.getGeneratedKeys();
            result.next();
            int pk = result.getInt(1);
            ingredient.setId(pk);

            return ingredient;

        }
        catch (SQLException e) {

            System.out.println(e.getMessage());

            return null;
        }

    }

    public boolean deleteIngredient(Connection con, int id) throws SQLException {
        boolean deleted;

        try (PreparedStatement stm = con.prepareStatement("DELETE FROM ingredient WHERE id = ?")) {

            stm.setInt(1, id);

            int rowsDeleted = stm.executeUpdate();

            deleted = rowsDeleted > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            throw e;

        }

        return deleted;
    }

    public List<IngredientDao> findAll(Connection con) {
        try (Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from ingredient order by name ASC");

            List<IngredientDao> ingredients = new ArrayList<>();

            while (result.next()) {
                ingredients.add(new IngredientDao(result));
            }

            return ingredients;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Integer getIngredientIdByName(Connection con, String ingredientName) {
        try (Statement stm = con.createStatement()) {

            ResultSet result = stm.executeQuery("select id from ingredient where name = '" + ingredientName  + "'");

            if (result.next()) {
                return result.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    // No se usa, probar si es necesario para funcionalidad y si no borrar
//    public List<IngredientDao> findRecipeIngredients(Connection con, int recipeId)
//    {
//        try (Statement stm = con.createStatement()) {
//
//            ResultSet result = stm.executeQuery("select i.* from ingredient i inner join ingredient_recipe ir on ir.id_ingredient = i.id where ir.id_recipe = " + recipeId);
//
//            List<IngredientDao> ingredients = new ArrayList<>();
//
//            while (result.next()) {
//                IngredientDao ingredientDao = new IngredientDao(result);
//                FillIngredientAllergens(con, ingredientDao);
//
//                ingredients.add(ingredientDao);
//            }
//
//            return ingredients;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    public List<IngredientRecipeDao> findIngredientsByRecipeId(Connection con, int recipeId)
    {
        try (Statement stm = con.createStatement()) {

            ResultSet result = stm.executeQuery("" +
                    "select ir.id as id, ir.id_recipe, i.id as id_ingredient, name as name_ingredient, ir.quantity, ir.unit from ingredient i inner join ingredient_recipe ir on ir.id_ingredient = i.id where ir.id_recipe = " + recipeId);

            List<IngredientRecipeDao> ingredients = new ArrayList<>();

            while (result.next()) {
                IngredientRecipeDao ingredientDao = new IngredientRecipeDao(result);
                FillIngredientAllergens(con, ingredientDao);
                ingredients.add(ingredientDao);
            }

            return ingredients;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void FillIngredientAllergens(Connection con, IngredientDao ingredientDao)
    {
        ingredientDao.setAllergens(allergenManager.findIngredientAllergens(con, ingredientDao.getId()));
    }
    private void FillIngredientAllergens(Connection con, IngredientRecipeDao ingredientRecipeDao)
    {
        ingredientRecipeDao.setAllergens(allergenManager.findIngredientAllergens(con, ingredientRecipeDao.getIdIngredient()));
    }
}

