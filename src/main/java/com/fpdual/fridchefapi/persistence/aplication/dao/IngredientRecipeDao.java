package com.fpdual.fridchefapi.persistence.aplication.dao;

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

public class IngredientRecipeDao {
    private int id;
    private int idRecipe;
    private int idIngredient;
    private String nameIngredient;
    private int quantity;
    private String unit;
    private List<AllergenDao> allergens;

    public IngredientRecipeDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.idRecipe = result.getInt("id_recipe");
            this.nameIngredient = result.getString("name_ingredient");
            this.idIngredient = result.getInt("id_ingredient");
            this.quantity = result.getInt("quantity");
            this.unit = result.getString("unit");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
