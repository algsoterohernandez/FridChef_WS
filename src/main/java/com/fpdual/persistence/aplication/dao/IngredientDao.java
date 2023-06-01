package com.fpdual.persistence.aplication.dao;

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
/**
 * Clase que representa un objeto IngredientDao.
 */
public class IngredientDao {
    private int id;
    private String name;
    private List<AllergenDao> allergens;

    /**
     * Constructor de la clase IngredientDao.
     *
     * @param result ResultSet que contiene los datos del ingrediente.
     */
    public IngredientDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.name = result.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}