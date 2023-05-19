package com.fpdual.controller;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.enums.HttpStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.service.IngredientService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/ingredients")

public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController() {
        ingredientService = new IngredientService(new MySQLConnector(), new IngredientManager());
    }


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<IngredientDto> ingredientsList = ingredientService.findAll();
        return Optional.ofNullable(ingredientsList)
                .map(list -> Response.ok().entity(list).build())
                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
    }

}