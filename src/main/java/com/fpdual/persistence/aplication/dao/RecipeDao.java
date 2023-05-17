package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor

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
    private List<IngredientDao> ingredients;


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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setIngredients(List<IngredientDao> ingredientList) {
        this.ingredients = ingredientList;
    }

    public List<IngredientDao> getIngredients() {
        return ingredients;
    }

    public Date getCreateTime() {
        return createTime;
    }
}
