package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.ResultSet;
import java.sql.SQLException;


@Data
@NoArgsConstructor
public class RolUserDao {
    private int idRol, idUser;

    public RolUserDao(ResultSet result) throws SQLException {

        try {

            idRol = result.getInt("id_rol");
            idUser = result.getInt("id_user");

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            throw e;

        }
    }
}