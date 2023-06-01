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

/**
 * Clase que representa la relación entre un alérgeno y un ingrediente en la base de datos.
 */
public class IngredientAllergenDao {
    private int idAllergen;
    private int idIngredient;
    private List<AllergenDao> allergens;
    private List<IngredientDao> ingredients;

    /**
     * Constructor de la clase IngredientAllergenDao.
     *
     * @param result Objeto ResultSet que contiene los datos de la relación entre alérgeno e ingrediente.
     */
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