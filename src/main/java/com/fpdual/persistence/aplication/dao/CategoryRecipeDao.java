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

/**
 * Clase que representa una asociación entre una categoría y una receta en la base de datos.
 */
public class CategoryRecipeDao {
    private int idCategory;
    private int idRecipe;

    /**
     * Constructor que crea un objeto CategoryRecipeDao a partir de un objeto ResultSet.
     *
     * @param result Objeto ResultSet que contiene los datos de la asociación categoría-receta.
     */
    public CategoryRecipeDao(ResultSet result) {
        try {
            this.idCategory = result.getInt("id_category");
            this.idRecipe = result.getInt("id_recipe");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}