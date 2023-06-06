package com.fpdual.controller;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.enums.HttpStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.AllergenManager;
import com.fpdual.service.AllergenService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * Controlador para manejar las operaciones relacionadas con los alérgenos.
 * Permite obtener todos los alérgenos disponibles.
 */
@Path("/allergens")
public class AllergenController {

    private final AllergenService allergenService;

    /**
     * Constructor de la clase AllergenController.
     * Inicializa el servicio de alérgenos con un conector MySQL y un gestor de alérgenos.
     */
    public AllergenController() {
        allergenService = new AllergenService(new MySQLConnector(), new AllergenManager());
    }

    /**
     * Obtiene todos los alérgenos disponibles.
     *
     * @return Response con una lista de objetos AllergenDto en formato JSON si se encuentra algún alérgeno,
     *         o un Response con un estado de error interno del servidor si no se encuentran alérgenos.
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllAllergens() {
        Response rs;

        List<AllergenDto> allergenDtoList = allergenService.findAllAllergens();

        if (allergenDtoList != null) {
            rs = Response.status(HttpStatus.OK.getStatusCode()).entity(allergenDtoList).build();
        } else {
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build(); // Error interno del servidor
        }

        return rs;
    }
}