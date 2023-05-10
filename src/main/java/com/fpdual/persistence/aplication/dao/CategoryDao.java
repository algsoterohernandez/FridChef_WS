package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor

public class CategoryDao {
    private int id;
    private String name;
    private List<RecipeDao> recipes;

    public CategoryDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.name = result.getString("name");
            this.recipes = new ArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
