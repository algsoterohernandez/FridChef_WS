package com.fpdual.fridchefapi.persistence.aplication.manager;


import com.fpdual.fridchefapi.exceptions.AlreadyExistsException;
import com.fpdual.fridchefapi.persistence.aplication.dao.RecipeDao;
import com.fpdual.fridchefapi.persistence.aplication.dao.UserDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UserManager {

    public UserDao insertUser(Connection con, UserDao userDao) throws AlreadyExistsException {
        try (PreparedStatement stm = con.prepareStatement("INSERT INTO user (name, surname1, " +
                "surname2, email, password, create_time) VALUES (?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setString(1, userDao.getName());
            stm.setString(2, userDao.getSurname1());
            stm.setString(3, userDao.getSurname2());
            stm.setString(4, userDao.getEmail());
            stm.setString(5, userDao.getPassword());
            stm.setDate(6, userDao.getCreateTime());


            stm.executeUpdate();
            ResultSet result = stm.getGeneratedKeys();
            result.next();
            int pk = result.getInt(1);
            userDao.setId(pk);

            return userDao;

        } catch (SQLIntegrityConstraintViolationException sqlicve) {

            throw new AlreadyExistsException("El ususario se ha registrado con anterioridad.");

        } catch (SQLException e) {

            System.out.println(e.getMessage());

            return null;
        }

    }

    public boolean deleteUser(Connection con, String email) throws SQLException {
        boolean deleted;

        try (PreparedStatement stm = con.prepareStatement("DELETE FROM user WHERE EMAIL = ?")) {


            stm.setString(1, email);

            int rowsDeleted = stm.executeUpdate();

            deleted = rowsDeleted > 0;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            throw e;

        }

        return deleted;
    }

    public UserDao findByEmailPassword(Connection con, String email, String password) {

        try (PreparedStatement stm = con.prepareStatement("SELECT * FROM user WHERE email = ? and password = ?;")) {

            stm.setString(1, email);
            stm.setString(2, password);

            ResultSet result = stm.executeQuery();

            UserDao userDao = null;

            while (result.next()) {

                userDao = new UserDao(result);

            }

            return userDao;

        } catch (SQLException e) {

            System.out.println(e.getMessage());

            return null;
        }
    }

    public List<RecipeDao> findFavorite(Connection con, int idUser){
        try (PreparedStatement stm = con.prepareStatement("SELECT recipe.* FROM recipe WHERE recipe.id IN(SELECT id_recipe FROM favorite WHERE id_user=?)")) {

            stm.setInt(1, idUser);

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

}