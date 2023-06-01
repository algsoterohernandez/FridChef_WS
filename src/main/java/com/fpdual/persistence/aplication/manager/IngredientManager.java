package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona las operaciones relacionadas con los ingredientes.
 */
public class IngredientManager {

    private final AllergenManager allergenManager;

    /**
     * Constructor de IngredientManager.
     * Inicializa el AllergenManager.
     */
    public IngredientManager() {
        allergenManager = new AllergenManager();
    }

    /**
     * Inserta un nuevo ingrediente en la base de datos.
     *
     * @param con  Conexión a la base de datos.
     * @param name Nombre del ingrediente.
     * @return Objeto IngredientDao creado.
     */
    public IngredientDao insertIngredient(Connection con, String name) {
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

        } catch (SQLException e) {

            System.out.println(e.getMessage());

            return null;
        }

    }

    /**
     * Elimina un ingrediente de la base de datos.
     *
     * @param con Conexión a la base de datos.
     * @param id  ID del ingrediente a eliminar.
     * @return true si se eliminó correctamente, false en caso contrario.
     * @throws SQLException Si ocurre un error de SQL.
     */
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

    /**
     * Obtiene todos los ingredientes de la base de datos.
     *
     * @param con Conexión a la base de datos.
     * @return Lista de IngredientDao que representa todos los ingredientes.
     */
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

    /**
     * Obtiene el ID de un ingrediente por su nombre.
     *
     * @param con            Conexión a la base de datos.
     * @param ingredientName Nombre del ingrediente.
     * @return ID del ingrediente si existe, null si no se encuentra.
     */
    public Integer getIngredientIdByName(Connection con, String ingredientName) {
        try (Statement stm = con.createStatement()) {

            ResultSet result = stm.executeQuery("select id from ingredient where name = '" + ingredientName + "'");

            if (result.next()) {
                return result.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Obtiene los ingredientes de una receta.
     *
     * @param con      Conexión a la base de datos.
     * @param recipeId ID de la receta.
     * @return Lista de IngredientDao que representa los ingredientes de la receta.
     */
    public List<IngredientDao> findRecipeIngredients(Connection con, int recipeId) {
        try (Statement stm = con.createStatement()) {

            ResultSet result = stm.executeQuery("select i.* from ingredient i inner join ingredient_recipe ir on ir.id_ingredient = i.id where ir.id_recipe = " + recipeId);

            List<IngredientDao> ingredients = new ArrayList<>();

            while (result.next()) {
                IngredientDao ingredientDao = new IngredientDao(result);
                FillIngredientAllergens(con, ingredientDao);

                ingredients.add(ingredientDao);
            }

            return ingredients;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene los ingredientes de una receta junto con su cantidad y unidad de medida.
     *
     * @param con      Conexión a la base de datos.
     * @param recipeId ID de la receta.
     * @return Lista de IngredientRecipeDao que representa los ingredientes de la receta con su cantidad y unidad de medida.
     */
    public List<IngredientRecipeDao> findIngredientsByRecipeId(Connection con, int recipeId) {
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


    /**
     * Rellena los alérgenos de un ingrediente.
     *
     * @param con           Conexión a la base de datos.
     * @param ingredientDao Objeto IngredientDao al que se le asignarán los alérgenos.
     */
    private void FillIngredientAllergens(Connection con, IngredientDao ingredientDao) {
        ingredientDao.setAllergens(allergenManager.findIngredientAllergens(con, ingredientDao.getId()));
    }

    /**
     * Rellena los alérgenos de un ingrediente de una receta.
     *
     * @param con                 Conexión a la base de datos.
     * @param ingredientRecipeDao Objeto IngredientRecipeDao al que se le asignarán los alérgenos.
     */
    private void FillIngredientAllergens(Connection con, IngredientRecipeDao ingredientRecipeDao) {
        ingredientRecipeDao.setAllergens(allergenManager.findIngredientAllergens(con, ingredientRecipeDao.getIdIngredient()));
    }
}