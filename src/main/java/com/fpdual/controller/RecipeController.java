package com.fpdual.controller;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.api.dto.RecipeFilterDto;
import com.fpdual.service.RecipeService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
@Path("/recipes")
public class RecipeController {

    private final RecipeService recipesService;

    public RecipeController() {
        recipesService = new RecipeService();
    }


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        Response rs;

        List<RecipeDto> ingredientsList = recipesService.findAll();

        if (ingredientsList != null) {
            rs = Response.ok().entity(ingredientsList).build();
        } else {
            rs = Response.status(500).build();//Server Error
        }

        // Comprobar resultado

        // Convertir a json

        // Construir respuesta

        return rs;
    }

    @POST
    @Path("/findbyingredients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByIngredients(RecipeFilterDto recipeFilterDto) {
        Response rs;
        try {
            if (recipeFilterDto == null) {
                rs = Response.status(400).build();
            } else {
                List<RecipeDto> recipeRs = recipesService.findRecipesByIngredients(recipeFilterDto.getIngredients());
                if (recipeRs != null) {
                    rs = Response.ok().entity(recipeRs).build();
                } else {
                    rs = Response.status(500).build();//status No content
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }
        return rs;
    }

    @POST
    @Path("/findSuggestions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRecipeSuggestions(RecipeFilterDto recipeFilterDto) {
        Response rs;

        try {
            if (recipeFilterDto == null) {
                rs = Response.status(400).build();
            } else {
                List<RecipeDto> recipeRs = recipesService.findRecipeSuggestions(recipeFilterDto.getIngredients());
                if (recipeRs == null) {
                    rs = Response.status(500).build();//status No content
                } else {
                    rs = Response.ok().entity(recipeRs).build();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }
        return rs;
    }

    @POST
    @Path("/filterRecipeByAllergen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response filterRecipesByAllergen(int allergenId) {
        Response rs;

        List<RecipeDto> recipeDtoList = recipesService.filterRecipesByAllergen(allergenId);

        if (recipeDtoList != null) {
            rs = Response.ok().entity(recipeDtoList).build();
        } else {
            rs = Response.status(500).build();//Server Error
        }

        return rs;
    }

}


