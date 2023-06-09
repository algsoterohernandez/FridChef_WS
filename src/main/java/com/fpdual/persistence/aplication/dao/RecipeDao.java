package com.fpdual.persistence.aplication.dao;

import com.fpdual.enums.RecipeStatus;
import lombok.*;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que representa un objeto RecipeDao que mapea los datos de una receta desde una consulta a la base de datos.
 */
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor

public class RecipeDao {
    private int id;
    private String name;
    private String description;
    private int difficulty;
    private int time;
    private String unitTime;
    private int idCategory;
    private Date createTime;
    private Blob image;
    private RecipeStatus status;
    private List<IngredientRecipeDao> ingredients;
    private double valoration;


    public RecipeDao() {
        ingredients = new ArrayList<>();
    }

    /**
     * Constructor de la clase RecipeDao que crea un objeto RecipeDao a partir de un ResultSet.
     *
     * @param result El ResultSet que contiene los datos de la receta.
     */
    public RecipeDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.name = result.getString("name");
            this.description = result.getString("description");
            this.difficulty = result.getInt("difficulty");
            this.time = result.getInt("time");
            this.unitTime = result.getString("unit_time");
            this.idCategory = result.getInt("id_category");
            this.createTime = result.getDate("create_time");
            this.image = result.getBlob("image");
            this.ingredients = new ArrayList<>();
            // Asignar el estado de la receta utilizando el método fromString() del enum RecipeStatus
            this.status = RecipeStatus.fromString(result.getString("status"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Establece la lista de ingredientes de la receta.
     *
     * @param ingredientList La lista de IngredientRecipeDao que representa los ingredientes de la receta.
     */

    public void setIngredients(List<IngredientRecipeDao> ingredientList) {
        this.ingredients = ingredientList;
    }

    /**
     * Obtiene la lista de ingredientes de la receta.
     *
     * @return La lista de IngredientRecipeDao que representa los ingredientes de la receta.
     */
    public List<IngredientRecipeDao> getIngredients() {
        return ingredients;
    }

    /**
     * Obtiene la fecha de creación de la receta.
     *
     * @return La fecha de creación de la receta.
     */
    public Date getCreateTime() {
        return createTime;
    }
}