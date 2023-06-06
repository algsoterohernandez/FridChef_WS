package com.fpdual.persistence.aplication.dao;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AllergenDao {
    private int id;
    private String name;
    private List<IngredientDao> ingredients; // ¿Se utiliza en algún momento?

    /**
     * Constructor que crea un objeto AllergenDao a partir de un ResultSet.
     *
     * @param result El ResultSet que contiene los datos del alérgeno.
     */
    public AllergenDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.name = result.getString("name");
            this.ingredients = new ArrayList<>();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
