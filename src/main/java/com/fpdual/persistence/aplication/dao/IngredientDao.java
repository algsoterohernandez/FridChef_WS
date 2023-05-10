package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor

public class IngredientDao {
    private int id;
    private String name;

    public IngredientDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.name = result.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
