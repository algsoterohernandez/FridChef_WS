package com.fpdual.controller;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.api.dto.IngredientRecipeDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.service.FavoriteService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Path("/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(){
        Connection con = null;
        try {
            con = new MySQLConnector().getMySQLConnection();
        } catch (Exception e){
        } finally {
            favoriteService = new FavoriteService(con);
        }
    }
    @GET
    @Path("/{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recipe(@PathParam("idUser") int idUser){

        AllergenDto allergen = new AllergenDto(1, "gluten");
        List<AllergenDto> allergens = new ArrayList<>();
        allergens.add(allergen);
        IngredientRecipeDto ingredientRecipe = new IngredientRecipeDto();
        RecipeDto recipe = new RecipeDto();
        recipe.setId(1);
        recipe.setName("carne con tomates");
        recipe.setDescription("esto es carne con tomates");
        recipe.setDifficulty(3);
        recipe.setTime(2);
        recipe.setUnitTime("h");
        recipe.setIdCategory(3);
        List<IngredientRecipeDto> ingredients = new ArrayList<>();
        ingredients.add(ingredientRecipe);
        recipe.setIngredients(ingredients);

//        List<RecipeDto> recipeList = recipeService.findAll();
//        return Optional.ofNullable(recipeList)
//                .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
//                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());

        return Optional.ofNullable(recipe)
                .map(dto -> Response.ok().entity(dto).build())
                .orElse(Response.status(404).build());
    }

}
