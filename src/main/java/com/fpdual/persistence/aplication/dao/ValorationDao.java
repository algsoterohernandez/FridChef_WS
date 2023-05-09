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

public class ValorationDao {
    private int id;
    private int idRecipe;
    private int idUser;
    private String title;
    private String description;
    private double valoration;

    public ValorationDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.idRecipe = result.getInt("id_recipe");
            this.idUser = result.getInt("id_User");
            this.title = result.getString("title");
            this.description = result.getString("description");
            this.valoration = result.getDouble("valoration");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
