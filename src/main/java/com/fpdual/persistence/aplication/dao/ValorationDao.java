package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
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
    private String nameUser;
    private String comment;
    private double valoration;

    private Date createTime;

    public ValorationDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.idRecipe = result.getInt("id_recipe");
            this.idUser = result.getInt("id_user");
            this.nameUser = result.getString("name");
            this.comment = result.getString("comment");
            this.createTime = result.getDate("create_time");
            this.valoration = result.getDouble("valoration");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
