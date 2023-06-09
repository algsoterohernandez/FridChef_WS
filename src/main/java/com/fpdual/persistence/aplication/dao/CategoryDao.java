package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una categoría en la capa de acceso a datos.
 */
@Data
@NoArgsConstructor

public class CategoryDao {
    private int id;
    private String name;
    private List<RecipeDao> recipes;

    /**
     * Constructor de la clase CategoryDao.
     *
     * @param result ResultSet que contiene los datos de la categoría.
     */
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