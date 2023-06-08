package com.fpdual.persistence.aplication.manager;


import com.fpdual.exceptions.AlreadyExistsException;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.dao.UserDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Clase que gestiona las operaciones relacionadas con el usuario en la base de datos.
 */
public class UserManager {

    /**
     * Inserta un nuevo usuario en la base de datos.
     *
     * @param con      Conexión a la base de datos.
     * @param userDao  Objeto UserDao que contiene los datos del usuario a insertar.
     * @return El objeto UserDao con el identificador generado por la base de datos.
     * @throws AlreadyExistsException Si el usuario ya existe en la base de datos.
     */
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

    /**
     * Elimina un usuario de la base de datos.
     *
     * @param con   Conexión a la base de datos.
     * @param email Email del usuario a eliminar.
     * @return true si el usuario se eliminó correctamente, false de lo contrario.
     * @throws SQLException Si ocurre un error en la ejecución de la consulta SQL.
     */
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

    /**
     * Busca un usuario en la base de datos por su email y contraseña.
     *
     * @param con      Conexión a la base de datos.
     * @param email    Email del usuario a buscar.
     * @param password Contraseña del usuario a buscar.
     * @return El objeto UserDao correspondiente al usuario encontrado, o null si no se encuentra.
     */
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