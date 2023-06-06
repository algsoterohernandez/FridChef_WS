package com.fpdual.persistence.aplication.manager;

import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import lombok.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
/**
 * Clase que gestiona las operaciones relacionadas con las recetas.
 */
public class RecipeManager {

    private final IngredientManager ingredientManager;

    /**
     * Constructor de la clase RecipeManager.
     * @param ingredientManager
     * Inicializa la instancia de IngredientManager.
     */
    public RecipeManager(IngredientManager ingredientManager)
    {
        this.ingredientManager = ingredientManager;
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
     * @param idsRecipe  Id de la receta.
     * @param orderByPopular  booleano que nos indica si esta ordenada.
     * @param idCategory ID de la categoría de las recetas a buscar.
     * @param limit
     * @param onlyAccepted  para que solo muestre las aceptadas
     * @return Lista de objetos RecipeDao que representan las recetas encontradas para la categoría especificada.
     */
    public List<RecipeDao> findBy(Connection con, List<String> idsRecipe, int idCategory, boolean orderByPopular, int limit, boolean onlyAccepted) {

        try (Statement stm = con.createStatement()) {
            String sql = "select *, (SELECT AVG(valoration) FROM valoration v where v.id_recipe = r.id) as valoration, (SELECT COUNT(valoration) FROM valoration v where v.id_recipe = r.id) as total_valoration from recipe r WHERE TRUE ";
            if( onlyAccepted ){
                sql += "AND status = 'ACCEPTED'";
            }

            if (idsRecipe != null && !idsRecipe.isEmpty()) {
                sql += " AND id in (" + String.join(",", idsRecipe) + ")";
            }

            if (idCategory != 0) {
                sql += " AND id_category = " + idCategory;
            }

            if (orderByPopular) {
                sql += " ORDER BY valoration DESC, total_valoration DESC";
            }
            if (limit > 0) {
                sql += " LIMIT " + limit;
            }

            ResultSet result = stm.executeQuery(sql);

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
        try {
            // Crear la consulta SQL con parámetros de marcador de posición '?'
            String query = "SELECT r.*, COUNT(DISTINCT ir.id_ingredient) AS num_ingredients " +
                    "FROM recipe r " +
                    "INNER JOIN ingredient_recipe ir ON r.id = ir.id_recipe " +
                    "WHERE ir.id_ingredient IN (";
            for (int i = 0; i < ingredientIds.size(); i++) {
                query += "?";
                if (i < ingredientIds.size() - 1) {
                    query += ", ";
                }
            }
            query += ") AND r.status = 'ACCEPTED' GROUP BY r.id, r.name " +
                    "HAVING COUNT(DISTINCT ir.id_ingredient) = ? " +
                    "AND NOT EXISTS (" +
                    "   SELECT 1 " +
                    "   FROM ingredient_recipe ir2 " +
                    "   WHERE ir2.id_recipe = r.id " +
                    "   AND ir2.id_ingredient NOT IN (";
            for (int i = 0; i < ingredientIds.size(); i++) {
                query += "?";
                if (i < ingredientIds.size() - 1) {
                    query += ", ";
                }
            }
            query += "))";

            // Crear un PreparedStatement con la consulta
            PreparedStatement pstmt = con.prepareStatement(query);

            // Establecer los parámetros en el PreparedStatement
            for (int i = 0; i < ingredientIds.size(); i++) {
                pstmt.setInt(i + 1, ingredientIds.get(i));
            }
            pstmt.setInt(ingredientIds.size() + 1, ingredientIds.size());
            for (int i = 0; i < ingredientIds.size(); i++) {
                pstmt.setInt(ingredientIds.size() + 2 + i, ingredientIds.get(i));
            }

            // Ejecutar la consulta preparada
            ResultSet result = pstmt.executeQuery();

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
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("SELECT r.* FROM recipe r, ingredient_recipe ir WHERE r.id = ir.id_recipe AND r.status = 'ACCEPTED'");
            int count = 0;
            for (int i = 0; i < ingredientIds.size(); i++) {
                queryBuilder.append(" AND EXISTS (SELECT * FROM ingredient_recipe ir").append(i)
                        .append(" WHERE ir").append(i).append(".id_recipe = r.id AND ir").append(i).append(".id_ingredient = ?) ");
                count++;
            }
            queryBuilder.append("GROUP BY r.id HAVING COUNT(*) > ?");

            String query = queryBuilder.toString();
            PreparedStatement pstmt = con.prepareStatement(query);
            for (int i = 0; i < ingredientIds.size(); i++) {
                pstmt.setInt(i + 1, ingredientIds.get(i));
            }
            pstmt.setInt(count + 1, ingredientIds.size());
            ResultSet result = pstmt.executeQuery();

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