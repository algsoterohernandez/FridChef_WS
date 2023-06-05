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
@Builder

public class IngredientDao {
    private int id;
    private String name;
    private List<AllergenDao> allergens;

    public IngredientDao() {
        allergens = new ArrayList<>();
    }

    public IngredientDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.name = result.getString("name");
            allergens = new ArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
