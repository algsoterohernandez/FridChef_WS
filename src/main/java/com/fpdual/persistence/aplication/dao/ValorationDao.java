package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Getter
@Setter
@NoArgsConstructor
/**
 * Clase que representa un objeto ValorationDao.
 */
public class ValorationDao {
    private int id;
    private int idRecipe;
    private int idUser;
    private String comment;
    private double valoration;

    /**
     * Constructor que crea un objeto ValorationDao a partir de un objeto ResultSet.
     *
     * @param result El objeto ResultSet que contiene los datos de la valoración.
     */
    public ValorationDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.idRecipe = result.getInt("id_recipe");
            this.idUser = result.getInt("id_user");
            this.comment = result.getString("comment");
            this.valoration = result.getDouble("valoration");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
