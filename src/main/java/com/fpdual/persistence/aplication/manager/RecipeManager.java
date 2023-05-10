package com.fpdual.persistence.aplication.manager;

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

    public RecipeDao insertRecipe(Connection con, RecipeDao recipe, List<IngredientDao> ingredients, IngredientRecipeDao ingredientRecipe) throws RecipeAlreadyExistsException {
        try(PreparedStatement stm = con.prepareStatement("INSERT INTO recipe (name, description, difficulty, time, unit_time, id_category, create_time, image) VALUES (?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)){

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
            if(result.next()) {
                int pk = result.getInt(1);
                recipe.setId(pk);
            }

            //insertar ingredientes

            if(ingredients != null && !ingredients.isEmpty()){
                try(PreparedStatement stm2 = con.prepareStatement("INSERT INTO ingredient (name) VALUE (?)", Statement.RETURN_GENERATED_KEYS)){
                   for(IngredientDao ingredient : ingredients){
                       stm2.setString(1, ingredient.getName());
                       stm2.executeUpdate();

                       ResultSet resultIngredient = stm2.getGeneratedKeys();
                       if(resultIngredient.next()){
                           int pkIngredient = resultIngredient.getInt(1);
                           ingredient.setId(pkIngredient);
                       }
                       try(PreparedStatement stm3 = con.prepareStatement("INSERT INTO ingredient_recipe (id_recipe, id_ingredient, quantity, unit) VALUES (?,?,?,?)")){
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

        }catch (SQLIntegrityConstraintViolationException e){
            throw new RecipeAlreadyExistsException("La receta ya existe en la base de datos");
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    // obtiene receta por id de la base de datos
    public RecipeDao readRecipeById(Connection con, int id){
        try(PreparedStatement stm = con.prepareStatement("SELECT * FROM recipe WHERE id = ?")){
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();
            if(result.next()){
                return new RecipeDao(result);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public RecipeDao updateRecipe(Connection con, RecipeDao recipe){
        try(PreparedStatement stm = con.prepareStatement("UPDATE recipe SET name= ?, description=?, difficulty=?, time=?, unit_time=?, id_category=?, create_time=?, image=? WHERE id=?")){
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
            if(rows>0){
                return recipe;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteRecipe(Connection con, int recipeId){
        boolean success = false;
        try(PreparedStatement stm = con.prepareStatement("DELETE FROM recipe WHERE id=?")){
            stm.setInt(1, recipeId);
            int affectedRows = stm.executeUpdate();
            if(affectedRows>0){
                success = true;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return success;
    }


    public List<RecipeDao> findAll() {
        try (Connection con = new MySQLConnector().getMySQLConnection(); Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from recipe");

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
