package com.fpdual.persistence;

import com.fpdual.javaweb.persistence.controller.RecipeController;
import com.fpdual.javaweb.persistence.manager.impl.RecipeManagerImpl;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        RecipeController recipeController = new RecipeController(new RecipeManagerImpl());
        System.out.println(recipeController.findAllRecipes());
        System.out.println(recipeController.findRecipesByCategory(3).size());
    }
}
