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

public class CategoryRecipeDao {
    private int idCategory;
    private int idRecipe;

    public CategoryRecipeDao(ResultSet result) {
        try {
            this.idCategory = result.getInt("id_category");
            this.idRecipe = result.getInt("id_recipe");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
