package com.fpdual.controller;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.service.IngredientService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController() {
        ingredientService = new IngredientService();
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

        List<IngredientDto> ingredientsList = ingredientService.findAll();

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
