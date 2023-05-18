package com.fpdual.persistence.aplication.manager;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.api.dto.RecipeFilterDto;
import com.fpdual.exceptions.RecipeAlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import lombok.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeManager {

    private final IngredientManager ingredientManager;

    public RecipeManager() {
        ingredientManager = new IngredientManager();
    }

    public RecipeDao insertRecipe(Connection con, RecipeDao recipe, List<IngredientDao> ingredients, IngredientRecipeDao ingredientRecipe) throws RecipeAlreadyExistsException {
        try (PreparedStatement stm = con.prepareStatement("INSERT INTO recipe (name, description, difficulty, time, unit_time, id_category, create_time, image) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, recipe.getName());
            stm.setString(2, recipe.getDescription());
            stm.setInt(3, recipe.getDifficulty());
            stm.setInt(4, recipe.getTime());
            stm.setString(5, recipe.getUnitTime());
            stm.setInt(6, recipe.getIdCategory());
            stm.setDate(7, (Date) recipe.getCreateTime());
            stm.setBlob(8, recipe.getImage());

            stm.executeUpdate();
            ResultSet result = stm.getGeneratedKeys();
            if (result.next()) {
                int pk = result.getInt(1);
                recipe.setId(pk);
            }

            //insertar ingredientes

            if (ingredients != null && !ingredients.isEmpty()) {
                try (PreparedStatement stm2 = con.prepareStatement("INSERT INTO ingredient (name) VALUE (?)", Statement.RETURN_GENERATED_KEYS)) {
                    for (IngredientDao ingredient : ingredients) {
                        stm2.setString(1, ingredient.getName());
                        stm2.executeUpdate();

                        ResultSet resultIngredient = stm2.getGeneratedKeys();
                        if (resultIngredient.next()) {
                            int pkIngredient = resultIngredient.getInt(1);
                            ingredient.setId(pkIngredient);
                        }
                        try (PreparedStatement stm3 = con.prepareStatement("INSERT INTO ingredient_recipe (id_recipe, id_ingredient, quantity, unit) VALUES (?,?,?,?)")) {
                            stm3.setInt(1, recipe.getId());
                            stm3.setInt(2, ingredient.getId());
                            stm3.setInt(3, ingredientRecipe.getQuantity());
                            stm3.setString(4, ingredientRecipe.getUnit());
                            stm3.executeUpdate();
                        }
                    }
                }
            }
            return recipe;

        } catch (SQLIntegrityConstraintViolationException e) {
            throw new RecipeAlreadyExistsException("La receta ya existe en la base de datos");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    // obtiene receta por id de la base de datos
    public RecipeDao readRecipeById(Connection con, int id) {
        try (PreparedStatement stm = con.prepareStatement("SELECT * FROM recipe WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                return new RecipeDao(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public RecipeDao updateRecipe(Connection con, RecipeDao recipe) {
        try (PreparedStatement stm = con.prepareStatement("UPDATE recipe SET name= ?, description=?, difficulty=?, time=?, unit_time=?, id_category=?, create_time=?, image=? WHERE id=?")) {
            stm.setString(1, recipe.getName());
            stm.setString(2, recipe.getDescription());
            stm.setInt(3, recipe.getDifficulty());
            stm.setInt(4, recipe.getTime());
            stm.setString(5, recipe.getUnitTime());
            stm.setInt(6, recipe.getIdCategory());
            stm.setDate(7, (Date) recipe.getCreateTime());
            stm.setBlob(8, recipe.getImage());
            stm.setInt(9, recipe.getId());

            int rows = stm.executeUpdate();
            if (rows > 0) {
                return recipe;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteRecipe(Connection con, int recipeId) {
        boolean success = false;
        try (PreparedStatement stm = con.prepareStatement("DELETE FROM recipe WHERE id=?")) {
            stm.setInt(1, recipeId);
            int affectedRows = stm.executeUpdate();
            if (affectedRows > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return success;
    }

    // Metodos creados y usados por Asun
    public List<RecipeDao> findAll(Connection con) {

        try (Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from recipe");

            result.beforeFirst();

            List<RecipeDao> recipes = new ArrayList<>();

            while (result.next()) {
                RecipeDao recipe = new RecipeDao(result);
                FillRecipeIngredients(con, recipe);

                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<RecipeDao> findRecipesByIngredients(Connection con, List<Integer> ingredientIds) {
        List<RecipeDao>  recipes = new ArrayList<>();
        try (Statement stm = con.createStatement()) {
            String query = "SELECT r.*, COUNT(DISTINCT ir.id_ingredient) AS num_ingredients " +
                    "FROM recipe r " +
                    "INNER JOIN ingredient_recipe ir ON r.id = ir.id_recipe " +
                    "WHERE ir.id_ingredient IN (";
            for (int i = 0; i < ingredientIds.size(); i++) {
                query += "'" + ingredientIds.get(i) + "'";
                if (i < ingredientIds.size() - 1) {
                    query += ", ";
                }
            }
            query += ") GROUP BY r.id, r.name " +
                    "HAVING COUNT(DISTINCT ir.id_ingredient) = " + ingredientIds.size() +
                    " AND NOT EXISTS (" +
                    "   SELECT 1 " +
                    "   FROM ingredient_recipe ir2 " +
                    "   WHERE ir2.id_recipe = r.id " +
                    "   AND ir2.id_ingredient NOT IN (";
            for (int i = 0; i < ingredientIds.size(); i++) {
                query += "'" + ingredientIds.get(i) + "'";
                if (i < ingredientIds.size() - 1) {
                    query += ", ";
                }
            }
            query += ")" +
                    ")";

            ResultSet result = stm.executeQuery(query);

            while (result.next()) {
                RecipeDao recipe = new RecipeDao(result);
                FillRecipeIngredients(con, recipe);

                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException e) {
            e.printStackTrace();

            return recipes;
        }
    }

    public List<RecipeDao> findRecipeSuggestions(Connection con, List<Integer> ingredientIds) {
        List<RecipeDao> recipesSuggestions = new ArrayList<>();
        try {

            String query = "SELECT r.* FROM recipe r, ingredient_recipe ir WHERE r.id = ir.id_recipe ";
            int count = 0;
            for (int i = 0; i < ingredientIds.size(); i++) {
                query += "AND EXISTS (SELECT * FROM ingredient_recipe ir" + i +
                        " WHERE ir" + i + ".id_recipe = r.id AND ir" + i + ".id_ingredient = ?) ";
                count++;
            }
            query += "GROUP BY r.id HAVING COUNT(*) > ?";
            PreparedStatement ps = con.prepareStatement(query);
            for (int i = 0; i < ingredientIds.size(); i++) {
                ps.setInt(i + 1, ingredientIds.get(i));
            }
            ps.setInt(count + 1, ingredientIds.size());
            ResultSet result = ps.executeQuery();



            while (result.next()) {
                RecipeDao recipe = new RecipeDao(result);
                FillRecipeIngredients(con, recipe);

                recipesSuggestions.add(recipe);
            }

            return recipesSuggestions;

        } catch (SQLException  e) {
            e.printStackTrace();
            return recipesSuggestions;
        }
    }

    private void FillRecipeIngredients(Connection con, RecipeDao recipeDao)
    {
        recipeDao.setIngredients(ingredientManager.findRecipeIngredients(con, recipeDao.getId()));
    }
}
