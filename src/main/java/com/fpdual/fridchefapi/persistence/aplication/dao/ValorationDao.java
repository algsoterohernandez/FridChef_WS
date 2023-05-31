package com.fpdual.fridchefapi.persistence.aplication.dao;

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

public class ValorationDao {
    private int id;
    private int idRecipe;
    private int idUser;
    private String comment;
    private double valoration;

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
