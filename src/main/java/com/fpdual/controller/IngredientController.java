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

/**
 * Controlador para manejar las operaciones relacionadas con los ingredientes.
 */
@Path("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    /**
     * Constructor de la clase IngredientController.
     * Inicializa el servicio de ingredientes utilizando una implementación concreta de la capa de acceso a datos.
     */
    public IngredientController() {
        ingredientService = new IngredientService(new MySQLConnector(), new IngredientManager());
    }

    /**
     * Obtiene todos los ingredientes.
     *
     * @return Una respuesta HTTP con la lista de ingredientes en formato JSON.
     *         Si la lista es nula, se devuelve una respuesta de estado 500 (Error interno del servidor).
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<IngredientDto> ingredientsList = ingredientService.findAll();

        return Optional.ofNullable(ingredientsList)
                .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
    }

    /**
     * Elimina un ingrediente dado su ID.
     *
     * @param id El ID del ingrediente a eliminar.
     * @return Una respuesta HTTP con el resultado de la eliminación en formato JSON.
     *         Si ocurre una excepción, se devuelve una respuesta de estado 500 (Error interno del servidor).
     */
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

    /**
     * Crea un nuevo ingrediente con el nombre proporcionado.
     *
     * @param name El nombre del ingrediente a crear.
     * @return Una respuesta HTTP con el nuevo ingrediente creado en formato JSON.
     *         Si ocurre una excepción, se devuelve una respuesta de estado 204 (No Content).
     */
    @POST
    @Path("/create/{name}/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createIngredient(@PathParam("name") String name) {
        Response rs = null;
        try {
            IngredientDto ingredientDto = ingredientService.createIngredient(name);
            if (ingredientDto != null) {
                rs = Response.status(HttpStatus.OK.getStatusCode()).entity(ingredientDto).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.NO_CONTENT.getStatusCode()).build();
        }
        return rs;
    }
}