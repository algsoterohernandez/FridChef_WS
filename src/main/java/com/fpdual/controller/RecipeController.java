package com.fpdual.controller;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.api.dto.RecipeFilterDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.service.RecipeService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/recipes")
public class RecipeController {

    private final RecipeService recipesService;

    public RecipeController() {
        recipesService = new RecipeService(new MySQLConnector(), new RecipeManager(), new IngredientManager());
    }


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<RecipeDto> recipeList = recipesService.findAll();
        return Optional.ofNullable(recipeList)
                .map(list -> Response.ok().entity(list).build())
                .orElse(Response.status(500).build());
    }


    @POST
    @Path("/findbyingredients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByIngredients(RecipeFilterDto recipeFilterDto) {
        try {
            if (recipeFilterDto == null) {
                return Response.status(400).build();
            }
            List<RecipeDto> recipeList = recipesService.findRecipesByIngredients(recipeFilterDto.getIngredients());
            return Optional.ofNullable(recipeList)
                    .map(list -> Response.ok().entity(list).build())
                    .orElse(Response.status(204).build()); // No content
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.serverError().build();
        }
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


}


