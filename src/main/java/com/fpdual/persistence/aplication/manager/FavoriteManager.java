package com.fpdual.persistence.aplication.manager;
import com.fpdual.exceptions.FridChefException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {
    public boolean favoriteAdded(Connection con, int idRecipe, int idUser) throws FridChefException, SQLException {
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


    public boolean favoriteRemoved(Connection con, int idRecipe, int idUser) throws FridChefException, SQLException {
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

    public List<Integer> findFavoriteList(Connection con, int idUser) throws FridChefException, SQLException{
        List<Integer> recipeIds = new ArrayList<>();

        try(PreparedStatement stm = con.prepareStatement("SELECT id_recipe FROM favorite WHERE id_user = ?")){
            stm.setInt(1, idUser);

            ResultSet result = stm.executeQuery();
            while(result.next()){
                int idRecipe = result.getInt("id_recipe");
                recipeIds.add(idRecipe);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return recipeIds;
        }
}