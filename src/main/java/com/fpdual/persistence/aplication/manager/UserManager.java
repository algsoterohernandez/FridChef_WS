package com.fpdual.persistence.aplication.manager;


import com.fpdual.exceptions.UserAlreadyExistsException;
import com.fpdual.persistence.aplication.dao.UserDao;

import java.sql.*;


public class UserManager {

    public UserDao insertUser(Connection con, UserDao userDao) throws UserAlreadyExistsException {
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

            throw new UserAlreadyExistsException("El ususario se ha registrado con anterioridad.");

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

}