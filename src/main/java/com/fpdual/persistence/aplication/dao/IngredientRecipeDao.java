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

public class IngredientRecipeDao {
    private int id;
    private int idRecipe;
    private int idIngredient;
    private int quantity;
    private String unit;
    private List<RecipeDao> recipes;
    private List<IngredientDao> ingredients;

    public IngredientRecipeDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.idRecipe = result.getInt("id_recipe");
            this.idIngredient = result.getInt("id_idIngredient");
            this.quantity = result.getInt("quantity");
            this.unit = result.getString("unit");
            this.recipes = new ArrayList<>();
            this.ingredients = new ArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
