package com.fpdual.fridchefapi.controller;

import com.fpdual.fridchefapi.api.dto.AllergenDto;
import com.fpdual.fridchefapi.enums.HttpStatus;
import com.fpdual.fridchefapi.persistence.aplication.connector.MySQLConnector;
import com.fpdual.fridchefapi.persistence.aplication.manager.AllergenManager;
import com.fpdual.fridchefapi.service.AllergenService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
@Path("/allergens")
public class AllergenController {

    private final AllergenService allergenService;

    public AllergenController() {
        allergenService = new AllergenService(new MySQLConnector(), new AllergenManager());
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllAllergens() {
        Response rs;

        List<AllergenDto> allergenDtoList = allergenService.findAllAllergens();

        if (allergenDtoList != null) {
            rs = Response.status(HttpStatus.OK.getStatusCode()).entity(allergenDtoList).build();
        }
        else {
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();//Server Error
        }

        return rs;
    }

}
