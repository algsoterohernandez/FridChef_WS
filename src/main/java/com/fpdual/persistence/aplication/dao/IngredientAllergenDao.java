package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor

public class IngredientAllergenDao {
    private int idAllergen;
    private int idIngredient;
    private List<AllergenDao> allergens;
    private List<IngredientDao>ingredients;

    public IngredientAllergenDao(ResultSet result) {
        try {
            this.idAllergen = result.getInt("id_allergen");
            this.idIngredient = result.getInt("id_ingredient");
            this.allergens = new ArrayList<>();
            this.ingredients = new ArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
