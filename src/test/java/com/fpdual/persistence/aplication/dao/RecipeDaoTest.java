package com.fpdual.persistence.aplication.dao;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RecipeDaoTest {

    @Test
    void testRecipeDaoConstructor() throws SQLException {
        // Arrange
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("testRecipe");
        when(mockResultSet.getString("description")).thenReturn("testDescription");
        when(mockResultSet.getInt("difficulty")).thenReturn(2);
        when(mockResultSet.getInt("time")).thenReturn(30);
        when(mockResultSet.getString("unit_time")).thenReturn("minutes");
        when(mockResultSet.getInt("id_category")).thenReturn(1);
        when(mockResultSet.getDate("create_time")).thenReturn(new java.sql.Date(2023, 05, 17));
        Blob mockBlob = Mockito.mock(Blob.class);
        when(mockResultSet.getBlob("image")).thenReturn(mockBlob);

        // Act
        RecipeDao recipe = new RecipeDao(mockResultSet);

        // Assert
        assertEquals(1, recipe.getId());
        assertEquals("testRecipe", recipe.getName());
        assertEquals("testDescription", recipe.getDescription());
        assertEquals(2, recipe.getDifficulty());
        assertEquals(30, recipe.getTime());
        assertEquals("minutes", recipe.getUnitTime());
        assertEquals(1, recipe.getIdCategory());
        assertEquals(new java.sql.Date(2023, 05, 17), recipe.getCreateTime());
        assertEquals(mockBlob, recipe.getImage());
        assertEquals(new ArrayList<IngredientDao>(), recipe.getIngredients());
    }

    @Test
    void testSetAndGetIngredients() {
        // Arrange
        RecipeDao recipe = new RecipeDao();
        IngredientRecipeDao ingredient1 = new IngredientRecipeDao();
        IngredientRecipeDao ingredient2 = new IngredientRecipeDao();
        List<IngredientRecipeDao> ingredientList = new ArrayList<>();
        ingredientList.add(ingredient1);
        ingredientList.add(ingredient2);

        // Act
        recipe.setIngredients(ingredientList);

        // Assert
        assertEquals(ingredientList, recipe.getIngredients());
    }

    @Test
    void testGetCreateTime() {
        // Arrange
        RecipeDao recipe = new RecipeDao();
        recipe.setCreateTime(new java.sql.Date(2023, 05, 17));

        // Act
        Date createTime = recipe.getCreateTime();

        // Assert
        assertEquals(new java.sql.Date(2023, 05, 17), createTime);
    }
}
