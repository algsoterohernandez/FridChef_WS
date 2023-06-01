package com.fpdual.persistence.aplication.manager;

import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import lombok.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Data
/**
 * Clase que gestiona las operaciones relacionadas con las recetas.
 */
public class RecipeManager {

    private final IngredientManager ingredientManager;

    /**
     * Constructor de la clase RecipeManager.
     * Inicializa la instancia de IngredientManager.
     */
    public RecipeManager() {
        ingredientManager = new IngredientManager();
    }

    /**
     * Crea una nueva receta en la base de datos.
     *
     * @param con    Conexión a la base de datos.
     * @param recipe Objeto RecipeDao que contiene los datos de la receta a crear.
     * @return Objeto RecipeDao que representa la receta creada.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     */
    public RecipeDao createRecipe(Connection con, RecipeDao recipe) throws SQLException {

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

            PreparedStatement ingredientRecipeStm = con.prepareStatement("INSERT INTO ingredient_recipe (id_recipe, id_ingredient, quantity, unit) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);

            for (IngredientRecipeDao ingredientRecipe : recipe.getIngredients()) {
                ingredientRecipeStm.setInt(1, recipe.getId());
                ingredientRecipeStm.setInt(2, ingredientRecipe.getIdIngredient());
                ingredientRecipeStm.setDouble(3, ingredientRecipe.getQuantity());
                ingredientRecipeStm.setString(4, ingredientRecipe.getUnit());
                ingredientRecipeStm.executeUpdate();
                ResultSet res = stm.getGeneratedKeys();
                if (res.next()) {
                    int pk = res.getInt(1);
                    ingredientRecipe.setId(pk);
                    ingredientRecipe.setIdRecipe(recipe.getId());
                }
            }
            ingredientRecipeStm.close();
            stm.close();

            return recipe;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    /**
     * Obtiene una receta por su ID desde la base de datos.
     *
     * @param con Conexión a la base de datos.
     * @param id  ID de la receta a buscar.
     * @return Objeto RecipeDao que representa la receta encontrada, o null si no se encontró ninguna receta con ese ID.
     */
    public RecipeDao getRecipeById(Connection con, int id) {
        try (PreparedStatement stm = con.prepareStatement("SELECT recipe.*, (SELECT ROUND(AVG(valoration),2) FROM valoration WHERE id_recipe = ?) AS valoration FROM recipe WHERE id = ?;")) {
            stm.setInt(1, id);
            stm.setInt(2, id);
            ResultSet result = stm.executeQuery();
            if (result.next()) {
                RecipeDao recipeDao = new RecipeDao(result);
                fillRecipeIngredients(con, recipeDao);
                return recipeDao;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Elimina una receta de la base de datos.
     *
     * @param con      Conexión a la base de datos.
     * @param recipeId ID de la receta a eliminar.
     * @return true si la receta se eliminó correctamente, false en caso contrario.
     */
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

    /**
     * Obtiene todas las recetas de la base de datos.
     *
     * @param con Conexión a la base de datos.
     * @return Lista de objetos RecipeDao que representan todas las recetas encontradas.
     */
    public List<RecipeDao> findAll(Connection con) {

        try (Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from recipe");

            List<RecipeDao> recipes = new ArrayList<>();

            while (result.next()) {
                RecipeDao recipe = new RecipeDao(result);
                fillRecipeIngredients(con, recipe);

                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene todas las recetas de una categoría específica desde la base de datos.
     *
     * @param con        Conexión a la base de datos.
     * @param idCategory ID de la categoría de las recetas a buscar.
     * @return Lista de objetos RecipeDao que representan las recetas encontradas para la categoría especificada.
     */
    public List<RecipeDao> findAllRecipesByCategoryId(Connection con, Integer idCategory) {

        try (PreparedStatement stm = con.prepareStatement("SELECT * FROM recipe WHERE id_category = ?")) {
            stm.setInt(1, idCategory);
            ResultSet result = stm.executeQuery();

            List<RecipeDao> recipes = new ArrayList<>();

            while (result.next()) {
                RecipeDao recipe = new RecipeDao(result);
                fillRecipeIngredients(con, recipe);

                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Obtiene las recetas que contienen los ingredientes especificados desde la base de datos.
     *
     * @param con            Conexión a la base de datos.
     * @param ingredientIds  Lista de IDs de ingredientes para buscar las recetas.
     * @return Lista de objetos RecipeDao que representan las recetas encontradas que contienen los ingredientes especificados.
     */
    public List<RecipeDao> findRecipesByIngredients(Connection con, List<Integer> ingredientIds) {
        List<RecipeDao> recipes = new ArrayList<>();
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
            query += ") AND r.status = 'ACCEPTED' GROUP BY r.id, r.name " +
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
                fillRecipeIngredients(con, recipe);

                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException e) {
            e.printStackTrace();

            return recipes;
        }
    }

    /**
     * Obtiene sugerencias de recetas basadas en los ingredientes especificados desde la base de datos.
     *
     * @param con            Conexión a la base de datos.
     * @param ingredientIds  Lista de IDs de ingredientes para buscar las sugerencias de recetas.
     * @return Lista de objetos RecipeDao que representan las sugerencias de recetas encontradas basadas en los ingredientes especificados.
     */
    public List<RecipeDao> findRecipeSuggestions(Connection con, List<Integer> ingredientIds) {
        List<RecipeDao> recipesSuggestions = new ArrayList<>();
        try {

            String query = "SELECT r.* FROM recipe r, ingredient_recipe ir WHERE r.id = ir.id_recipe AND r.status = 'ACCEPTED'";
            int count = 0;
            for (int i = 0; i < ingredientIds.size(); i++) {
                query += " AND EXISTS (SELECT * FROM ingredient_recipe ir" + i +
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
                fillRecipeIngredients(con, recipe);

                recipesSuggestions.add(recipe);
            }

            return recipesSuggestions;

        } catch (SQLException e) {
            e.printStackTrace();
            return recipesSuggestions;
        }
    }

    /**
     * Rellena los ingredientes de una receta.
     *
     * @param con       La conexión a la base de datos.
     * @param recipeDao El objeto RecipeDao de la receta a rellenar.
     */
    private void fillRecipeIngredients(Connection con, RecipeDao recipeDao) {
        recipeDao.setIngredients(ingredientManager.findIngredientsByRecipeId(con, recipeDao.getId()));
    }

    /**
     * Busca recetas por ID de categoría.
     *
     * @param idCategory El ID de la categoría.
     * @return Una lista de objetos RecipeDao que coinciden con el ID de categoría especificado.
     */
    public List<RecipeDao> findRecipesByIdCategory(Integer idCategory) {
        try (Connection con = new MySQLConnector().getMySQLConnection()) {
            String query = "SELECT * FROM recipe WHERE id_category = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, idCategory);
            ResultSet result = ps.executeQuery();

            List<RecipeDao> recipes = new ArrayList<>();

            while (result.next()) {
                RecipeDao recipe = new RecipeDao(result);
                fillRecipeIngredients(con, recipe);

                recipes.add(recipe);
            }

            return recipes;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca recetas por estado pendiente.
     *
     * @param con La conexión a la base de datos.
     * @return Una lista de objetos RecipeDao con estado pendiente.
     */
    public List<RecipeDao> findByStatusPending(Connection con) {

        try (PreparedStatement stm = con.prepareStatement("SELECT * FROM recipe WHERE status LIKE ?")) {

            stm.setString(1, RecipeStatus.PENDING.getStatus());

            ResultSet result = stm.executeQuery();

            RecipeDao recipeDao;

            List<RecipeDao> recipeDaoList = new ArrayList<>();

            while (result.next()) {

                recipeDao = new RecipeDao(result);
                recipeDaoList.add(recipeDao);

            }

            return recipeDaoList;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

            return null;
        }
    }

    /**
     * Actualiza el estado de una receta.
     *
     * @param con    La conexión a la base de datos.
     * @param id     El ID de la receta a actualizar.
     * @param status El nuevo estado de la receta.
     * @return El objeto RecipeDao actualizado, o null si ocurre un error.
     */
    public RecipeDao updateRecipeStatus(Connection con, int id, String status) {
        RecipeDao recipeDao = null;

        try (PreparedStatement updateStm = con.prepareStatement("UPDATE recipe SET status = ? WHERE id = ?")) {
            if (status.equals(RecipeStatus.ACCEPTED.name())) {
                updateStm.setString(1, RecipeStatus.ACCEPTED.getStatus());
            } else if (status.equals(RecipeStatus.DECLINED.name())) {
                updateStm.setString(1, RecipeStatus.DECLINED.getStatus());
            }
            updateStm.setInt(2, id);
            updateStm.executeUpdate();

            // Consulta adicional para obtener los datos actualizados
            try (PreparedStatement selectStm = con.prepareStatement("SELECT * FROM recipe WHERE id = ?")) {
                selectStm.setInt(1, id);
                try (ResultSet result = selectStm.executeQuery()) {
                    while (result.next()) {
                        recipeDao = new RecipeDao(result);
                    }
                }
            }

            return recipeDao;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;

        }
    }
}