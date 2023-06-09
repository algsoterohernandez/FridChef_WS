package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que representa un objeto de acceso a datos (DAO) para la entidad User.
 */
@Data
@NoArgsConstructor

public class UserDao {
    private int id;
    private String name, surname1, surname2, email, password;
    private Date createTime;

    /**
     * Constructor de UserDao que crea un objeto a partir de un ResultSet.
     *
     * @param result El ResultSet que contiene los datos del usuario.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
    public UserDao(ResultSet result) throws SQLException {

        try {
            id = result.getInt("id");
            name = result.getString("name");
            surname1 = result.getString("surname1");
            surname2 = result.getString("surname2");
            email = result.getString("email");
            password = result.getString("password");
            createTime = result.getDate("create_time");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }
}

