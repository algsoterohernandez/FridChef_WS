package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Clase que representa la relación entre un ingrediente y una receta en la base de datos.
 */
@Data
@NoArgsConstructor

public class IngredientRecipeDao {
    private int id;
    private int idRecipe;
    private int idIngredient;
    private String nameIngredient;
    private int quantity;
    private String unit;
    private List<AllergenDao> allergens;

    /**
     * Constructor de la clase IngredientRecipeDao.
     *
     * @param result Objeto ResultSet que contiene los datos de la consulta de la base de datos.
     */
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

