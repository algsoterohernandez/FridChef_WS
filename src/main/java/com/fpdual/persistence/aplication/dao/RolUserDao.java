package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.ResultSet;
import java.sql.SQLException;


@Data
@NoArgsConstructor
/**
 * Clase para la relación de roles y usuarios.
 */
public class RolUserDao {
    private int idRol, idUser;

    /**
     * Crea un objeto RolUserDao a partir de un objeto ResultSet.
     *
     * @param result El objeto ResultSet que contiene los datos del resultado de la consulta.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
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
