package com.fpdual.controller;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.service.RecipeService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/recipes")
public class RecipeController {

    private final RecipeService recipesService;

    public RecipeController() { recipesService = new RecipeService();
    }

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok().entity("Service online").build();
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        Response rs;

        List<RecipeDto> ingredientsList = recipesService.findAll();

        if (ingredientsList != null) {
            rs = Response.ok().entity(ingredientsList).build();
        }
        else {
            rs = Response.status(500).build();//Server Error
        }

        // Comprobar resultado

        // Convertir a json

        // Construir respuesta

        return rs;
    }

}
