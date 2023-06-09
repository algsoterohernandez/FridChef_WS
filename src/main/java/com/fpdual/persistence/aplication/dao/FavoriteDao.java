package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Objeto de acceso a datos para la tabla de Favoritos.
 */


@Data
@NoArgsConstructor

public class FavoriteDao {
    private int id;
    private int idRecipe;
    private int idUser;
    private Date createTime;

    /**
     * Crea un objeto FavoriteDao a partir de los datos de un ResultSet.
     *
     * @param result El ResultSet que contiene los datos de la fila.
     */
    public FavoriteDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.idRecipe = result.getInt("id_recipe");
            this.idUser = result.getInt("id_user");
            this.createTime = result.getDate("create_time");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
