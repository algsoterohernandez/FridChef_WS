package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor

public class FavoriteDao {
    private int id;
    private int idRecipe;
    private int idUser;
    private Date createTime;

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
