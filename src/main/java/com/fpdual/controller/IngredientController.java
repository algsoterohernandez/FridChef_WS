package com.fpdual.controller;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.enums.HttpStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.service.IngredientService;
import jakarta.ws.rs.*;
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
    //método comentado para entender su funcionamiento e implementación con lambda
    public Response findAll() {
        // Llamada al método findAll() del servicio de ingredientes para obtener la lista de ingredientes
        List<IngredientDto> ingredientsList = ingredientService.findAll();

        // Verificar si la lista de ingredientes es nula o vacía usando lambda
        return Optional.ofNullable(ingredientsList)
                // Si la lista no es nula, construir una respuesta exitosa con la lista de ingredientes como entidad
                .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                // Si la lista es nula, devolver una respuesta de estado 500 (Error interno del servidor)
                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
    }

    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteIngredient(@PathParam("id") int id) {
        Response rs;
        try {
            boolean deleted = ingredientService.deleteIngredient(id);

            rs = Response.status(HttpStatus.OK.getStatusCode()).entity(deleted).build();

        } catch (Exception e) {
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return rs;
    }


    @POST
    @Path("/create/{name}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createIngredient(@PathParam("name") String name) {
        Response rs = null;

        try {
            IngredientDto ingredientDto = ingredientService.createIngredient(name);

            if (ingredientDto != null){
                rs = Response.status(HttpStatus.OK.getStatusCode()).entity(ingredientDto).build();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.NO_CONTENT.getStatusCode()).build();
        }

        return rs;
    }



}