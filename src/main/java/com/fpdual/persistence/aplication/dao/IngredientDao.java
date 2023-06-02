package com.fpdual.persistence.aplication.dao;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class IngredientDao {
    private int id;
    private String name;
    private List<AllergenDao> allergens;


    public IngredientDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.name = result.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
