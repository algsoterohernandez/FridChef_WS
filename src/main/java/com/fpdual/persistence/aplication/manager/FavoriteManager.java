package com.fpdual.persistence.aplication.manager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona las operaciones relacionadas con los favoritos.
 */
public class FavoriteManager {

    /**
     * Agrega una receta favorita para un usuario en la base de datos.
     *
     * @param con       la conexión a la base de datos
     * @param idRecipe  el ID de la receta a agregar como favorita
     * @param idUser    el ID del usuario que agrega la receta como favorita
     * @return true si la receta favorita se agregó correctamente, false en caso contrario
     * @throws SQLException si ocurre algún error al interactuar con la base de datos
     */
    public boolean favoriteAdded(Connection con, int idRecipe, int idUser) throws SQLException {
        boolean favoriteAdded = false;

        try (PreparedStatement stm = con.prepareStatement("INSERT INTO favorite (id_recipe, id_user, create_time) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setInt(1, idRecipe);
            stm.setInt(2, idUser);
            stm.setDate(3, new Date(System.currentTimeMillis()));

            stm.executeUpdate();

            favoriteAdded = true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return  favoriteAdded;
    }

    /**
     * Elimina una receta favorita de un usuario en la base de datos.
     *
     * @param con     la conexión a la base de datos
     * @param idRecipe el ID de la receta a eliminar de favoritos
     * @param idUser   el ID del usuario
     * @return true si se eliminó la receta de favoritos correctamente, false en caso contrario
     * @throws SQLException si ocurre un error al interactuar con la base de datos
     */
    public boolean favoriteRemoved(Connection con, int idRecipe, int idUser) throws SQLException {
        boolean favoriteRemoved = false;

        try (PreparedStatement stm = con.prepareStatement("DELETE FROM favorite WHERE id_recipe=? AND id_user=?")) {

            stm.setInt(1, idRecipe);
            stm.setInt(2, idUser);

            int rowsDeleted = stm.executeUpdate();

            favoriteRemoved = rowsDeleted > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            throw e;
        }
        return favoriteRemoved;
    }

    /**
     * Encuentra la lista de recetas favoritas de un usuario.
     *
     * @param con    La conexión a la base de datos.
     * @param idUser El ID del usuario.
     * @return Una lista de enteros que representa los ID de las recetas favoritas del usuario.
     */
    public List<Integer> findFavoriteList(Connection con, int idUser) {
        List<Integer> recipeIds = new ArrayList<>();

        try (PreparedStatement stm = con.prepareStatement("SELECT id_recipe FROM favorite WHERE id_user = ?")) {
            stm.setInt(1, idUser);

            ResultSet result = stm.executeQuery();
            while (result.next()) {
                int idRecipe = result.getInt("id_recipe");
                recipeIds.add(idRecipe);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recipeIds;
    }
}